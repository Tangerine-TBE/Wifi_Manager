package com.feisukj.cleaning.ui.fragment

import android.annotation.SuppressLint
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.format.Formatter
import android.view.View
import android.widget.TextView
import com.example.module_base.cleanbase.GuideActivity
import com.example.module_base.cleanbase.RecyclerViewHolder
import com.example.module_base.cleanbase.SureCancelDialog

import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.AppAdapter_
import com.feisukj.cleaning.bean.AppBean
import kotlinx.android.synthetic.main.fragment_app_clean.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FrequencySortAppFragment:AbstractAppFragment() {
    companion object{
        private const val USAGE_ACCESS_SETTINGS_PERMISSION_CODE=111
    }

    private var changeCurrentShowView:(()->Unit)?=null
    private var job: Job?=null
    private val listData=ArrayList<AppBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadInfrequentApplication()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeCurrentShowView?.invoke()
    }

    override fun onListenerKeyword() {
        viewModel?.keyword?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {keyword->
            val data= listData.filter {
                it.label.contains(keyword,true)
            }
            val upChooseApp= Stack<AppBean>()
            chooseApp.forEach {app->
                val t=data.find { it.packageName==app.packageName }
                if (t!=null){
                    t.isCheck=true
                    upChooseApp.add(t)
                }
            }
            chooseApp.clear()
            chooseApp.addAll(upChooseApp)
            updateCleanText()
            appAdapter.setData(data)
        })
    }

    override fun initListener() {
        super.initListener()
        gotoOpen.setOnClickListener {
            SureCancelDialog(context?:return@setOnClickListener)
                    .setContent("是否前往开启使用情况访问权限？")
                    .setOnNegativeClick({
                        it.dismiss()
                    },"否")
                    .setOnPositiveClick({
                        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
                            startActivityForResult(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), USAGE_ACCESS_SETTINGS_PERMISSION_CODE)
                            startActivity(Intent(context, GuideActivity::class.java))
                        }
                        it.dismiss()
                    },"是")
                    .show()
        }
    }

    @SuppressLint("WrongConstant")
    private fun loadInfrequentApplication(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            val ua= when {
                Build.VERSION.SDK_INT== Build.VERSION_CODES.LOLLIPOP -> {
                    context?.getSystemService("usagestats") as? UsageStatsManager
                }
                Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP_MR1 -> {
                    context?.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
                }
                else -> {
                    null
                }
            }
            if (ua==null){
                changeCurrentShowView={
                    if (isAdded){
                        if (viewSwitcher.currentView!=notPermissionView){
                            viewSwitcher.showNext()
                            notInfrequentAppTitle.text="暂不支持该功能"
                            notInfrequentAppDes.text="只有Android 5.0及以上的系统支持该功能~"
                            gotoOpen.visibility=View.GONE
                        }
                    }
                }
                return
            }
            val usageStats=ua.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY,0,System.currentTimeMillis())
            if (usageStats.isNullOrEmpty()){
                changeCurrentShowView={
                    if (isAdded){
                        if (viewSwitcher.currentView!=notPermissionView){
                            viewSwitcher.showNext()
                        }
                    }
                }
                return
            }
            changeCurrentShowView={
                if (isAdded){
                    if (viewSwitcher.currentView==notPermissionView){
                        viewSwitcher.showNext()
                    }
                }
            }
            job = GlobalScope.launch {
                val useAppTime=HashMap<String,Long>()
                usageStats.forEach {
                    if (!isActive){
                        return@launch
                    }
                    if (it.totalTimeInForeground>0L){
                        useAppTime[it.packageName] = it.lastTimeUsed
                    }
                }
                useAppTime.forEach { entry ->
                    if (isActive){
                        val appBean=AppBean.getAppBean(context ?: return@launch, entry.key)?.also {
                            it.lastUseTimeInterval=System.currentTimeMillis()-entry.value
                            activity?.runOnUiThread {
                                appAdapter.addItem(it)
                            }
                        }
                        if (appBean!=null){
                            listData.add(appBean)
                        }
                    }
                }
                viewModel?.scanAllAppState2?.postValue(true)
            }
        }else{
            changeCurrentShowView={
                if (isAdded){
                    if (viewSwitcher.currentView!=notPermissionView){
                        viewSwitcher.showNext()
                        notInfrequentAppTitle.text="暂不支持该功能"
                        notInfrequentAppDes.text="只有Android 5.0及以上的系统支持该功能~"
                        gotoOpen.visibility=View.GONE
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== USAGE_ACCESS_SETTINGS_PERMISSION_CODE){
            loadInfrequentApplication()
            changeCurrentShowView?.invoke()
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewSwitcher.currentView == notPermissionView){
            loadInfrequentApplication()
            changeCurrentShowView?.invoke()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }





    override fun getAdapter(): AppAdapter_ {
        return AppAdapter_(R.layout.item_software2_clean, emptyList(), Comparator { o1, o2 ->
            if (o1.lastUseTimeInterval>o2.lastUseTimeInterval){
                -1
            }else{
                1
            }
        })
    }

    override fun onBindView(vh: RecyclerViewHolder, appBean: AppBean) {
        appBean.icon?.get().let {
            if (it==null){
                vh.setImage(R.id.appIcon,android.R.mipmap.sym_def_app_icon)
            }else{
                vh.setImage(R.id.appIcon,it)
            }
        }
        vh.setText(R.id.appLabel,appBean.label)
        vh.setText(R.id.appDes, Formatter.formatFileSize(context,appBean.appBytes+appBean.dataBytes+appBean.cacheBytes+appBean.fileSize))
        vh.getView<View>(R.id.lastUseTip).visibility= View.VISIBLE
        val d= appBean.lastUseTimeInterval /86400000//86400000,一天毫秒数
        vh.getView<TextView>(R.id.lastUse).let {
            it.visibility= View.VISIBLE
            if (d==0L){
                val h=appBean.lastUseTimeInterval %86400000/3600000
                if (h==0L){
                    it.text="1小时内"
                }else{
                    it.text="${h}小时前"
                }
            }else{
                it.text="${d}天前"
            }
        }

        val appChooseView=vh.getView<View>(R.id.appChoose)
        appChooseView.isSelected=appBean.isCheck
        appChooseView.setOnClickListener {
            it.isSelected=!it.isSelected
            appBean.isCheck=it.isSelected
            val isChange=if (it.isSelected){
                chooseApp.add(appBean)
            }else{
                chooseApp.remove(appBean)
            }
            if (isChange){
                updateCleanText()
            }
        }
        vh.itemView.setOnClickListener {
            toAppSetting(appBean.packageName?:return@setOnClickListener)
        }
    }

    override fun onPackageAdd(appBean: AppBean) {

    }
}