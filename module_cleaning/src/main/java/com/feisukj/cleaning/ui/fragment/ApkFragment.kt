package com.feisukj.cleaning.ui.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.module_base.widget.LoadingDialog


import com.feisukj.cleaning.BuildConfig
import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.AppAdapter
import com.feisukj.cleaning.bean.AppBean
import com.feisukj.cleaning.bean.FileBean
import com.feisukj.cleaning.file.FileContainer
import com.feisukj.cleaning.file.FileManager
import com.feisukj.cleaning.file.FileType
import com.feisukj.cleaning.file.NextFileCallback
import com.feisukj.cleaning.utils.toAppOpenFile
import com.feisukj.cleaning.view.SeeDetailsDialog
import kotlinx.android.synthetic.main.frag_we_and_qq_pic_clean.*
import kotlinx.android.synthetic.main.fragment_apk_clean.*
import kotlinx.android.synthetic.main.fragment_apk_clean.allChoose
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ApkFragment : Fragment(), NextFileCallback {
    companion object{
        private const val APK_KEY="apk_key"
        private const val INSTALL_APK="install_apk"
        private const val NOT_INSTALL_APK="not_install_apk"

        fun getInstance(isInstall:Boolean):ApkFragment{
            val f=ApkFragment()
            f.arguments= Bundle().also {
                it.putString(APK_KEY,if (isInstall) INSTALL_APK else NOT_INSTALL_APK)
            }
            return f
        }
    }
    private var appAdapter: AppAdapter?=null
    private val chooseApp= Stack<AppBean>()
    private val flag by lazy {
        arguments?.getString(APK_KEY)?: NOT_INSTALL_APK
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FileManager.addCallBack(this)
        return inflater.inflate(R.layout.fragment_apk_clean,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appAdapter= AppAdapter(R.layout.item_software2_clean, FileContainer.getApkFileList().filter { (flag==INSTALL_APK)==it.isInstall }.sortedBy { -it.fileSize })
        appAdapter?.bindView={viewHolder,appBean->
            appBean.icon?.get().let {
                if (it==null){
                    viewHolder.setImage(R.id.appIcon,android.R.mipmap.sym_def_app_icon)
                }else{
                    viewHolder.setImage(R.id.appIcon,it)
                }
            }
            viewHolder.setText(R.id.appLabel,appBean.label)
            viewHolder.setText(R.id.appDes,appBean.fileSizeString)

            val appChooseView=viewHolder.getView<View>(R.id.appChoose)
            appChooseView.isSelected=appBean.isCheck
            appChooseView.setOnClickListener {
                it.isSelected=!it.isSelected
                val isChange=if (it.isSelected){
                    chooseApp.add(appBean)
                }else{
                    chooseApp.remove(appBean)
                }
                if (isChange){
                    updateCleanText()
                }
            }
            viewHolder.itemView.setOnClickListener {
                SeeDetailsDialog(context?:return@setOnClickListener)
                        .setTime(SimpleDateFormat.getInstance().format(Date(appBean.fileLastModified)))
                        .setPath(appBean.absolutePath.removePrefix("/storage/emulated/0"))
                        .setOnNegativeClick({
                            it.dismiss()
                        },"取消")
                        .setOnPositiveClick({
                            toAppOpenFile(this, File(appBean.absolutePath))
                            it.dismiss()
                        },"安装")
                        .show()
            }
        }
        apkRecyclerView.layoutManager= LinearLayoutManager(context)
        apkRecyclerView.adapter=appAdapter
        if (appAdapter?.getData()?.isEmpty()!!){
            noData.visibility = View.VISIBLE
        }else{
            noData.visibility = View.GONE
        }
        initListener()

        val f= FrameLayout(context as Activity).apply { this.layoutParams= ViewGroup.LayoutParams((resources.displayMetrics.density*300).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT) }
        appAdapter?.adView=f

    }

    private fun initListener(){
        apkClean.setOnClickListener {
            if (chooseApp.isEmpty()){
                return@setOnClickListener
            }
            val loadingDelete= LoadingDialog(context?:return@setOnClickListener)
                    .setCancelable(false)
                    .setTitleText("正在删除...")
            loadingDelete.show()
            GlobalScope.launch {
                while (chooseApp.isNotEmpty()){
                    val c=chooseApp.pop()?:continue
                    if (!BuildConfig.DEBUG){
                        File(c.absolutePath).delete()
                        FileContainer.removeApk(c)
                    }
                    activity?.runOnUiThread {
                        appAdapter?.removeItem(c)
                    }
                }
                activity?.runOnUiThread {
                    loadingDelete.dismiss()
                    updateCleanText()
                }
            }
        }

        allChoose.setOnClickListener { view ->
            view.isSelected=!view.isSelected
            if (appAdapter?.getData()?.any { it?.isCheck!=view.isSelected }==true){
                appAdapter?.getData()?.forEach {
                    it.isCheck=view.isSelected
                    if (view.isSelected)
                        chooseApp.add(it)
                    else
                        chooseApp.remove(it)
                }
                appAdapter?.notifyDataSetChanged()
            }
            updateCleanText()
        }
    }

    private fun updateCleanText(){
        if (chooseApp.isEmpty()){
            apkClean.visibility=View.GONE
        }else{
            apkClean.visibility=View.VISIBLE
            apkClean.text="删除${chooseApp.size}个安装包"
        }
    }

    override fun onNextFile(type: FileType, fileBean: FileBean) {
        if (type!=FileType.apk||fileBean!is AppBean||fileBean.isInstall!=(flag== INSTALL_APK))
            return
        activity?.runOnUiThread {
            appAdapter?.addItem(fileBean)
        }
    }

    override fun onNextFiles(type: FileType, fileBeans: List<FileBean>){
        if (type!=FileType.apk)
            return
        activity?.runOnUiThread {
            fileBeans.forEach {
                if (it !is AppBean)
                    return@runOnUiThread
                if (it.isInstall==(flag== INSTALL_APK)){
                    appAdapter?.addItem(it)
                }
            }
        }
    }

    override fun onComplete() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        FileManager.removeCallBack(this)
    }
}