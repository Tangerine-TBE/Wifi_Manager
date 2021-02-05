package com.feisukj.cleaning.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_ad.advertisement.FeedHelper
import com.example.module_base.cleanbase.RecyclerViewHolder


import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.AppAdapter_
import com.feisukj.cleaning.bean.AppBean
import com.feisukj.cleaning.utils.isInstallApp
import com.feisukj.cleaning.viewmodel.AppViewModel
import kotlinx.android.synthetic.main.activity_cooling_complete.*
import kotlinx.android.synthetic.main.fragment_app_clean.*
import java.util.*

abstract class AbstractAppFragment:Fragment(R.layout.fragment_app_clean) {
    protected val appAdapter by lazy { getAdapter() }
    protected val chooseApp= Stack<AppBean>()
    private var isRunUnloadingApp=false
    protected val viewModel by lazy { ViewModelProvider(activity?:return@lazy null).get(AppViewModel::class.java) }

    abstract fun getAdapter(): AppAdapter_

    abstract fun onBindView(vh: RecyclerViewHolder, appBean: AppBean)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appRecyclerView.layoutManager=LinearLayoutManager(context)
        appRecyclerView.adapter=appAdapter
        appAdapter.bindView={vh,appBean->
            onBindView(vh, appBean)
        }
        onListenerKeyword()
        initListener()
        viewModel?.scanAllAppState?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it==true){
                appRecyclerView.scrollToPosition(0)
            }
        })
        viewModel?.scanAllAppState2?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it==true){
                appRecyclerView.scrollToPosition(0)
            }
        })
        viewModel?.scanAllAppState3?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it==true){
                appRecyclerView.scrollToPosition(0)
            }
        })

        val f= FrameLayout(context as Activity).apply { this.layoutParams= ViewGroup.LayoutParams((resources.displayMetrics.density*300).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT) }
        appAdapter?.adView=f



    }

    private val feedHelper by lazy {
        FeedHelper(activity,frameLayout)
    }


    protected open fun onListenerKeyword(){
        viewModel?.keyword?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {keyword->
            val data= viewModel?.listData?.filter {
                it.label.contains(keyword,true)
            }?:return@Observer
            val upChooseApp=Stack<AppBean>()
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

    protected open fun initListener(){
        appClean.setOnClickListener {
            isRunUnloadingApp=true
            unloadingApp()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isRunUnloadingApp){
            unloadingApp()
        }
        val list=appAdapter.getData().filter { it.isCheck }
        if (list.size!=chooseApp.size||!list.containsAll(chooseApp)){
            chooseApp.clear()
            chooseApp.addAll(list)
        }
        if (list.isNotEmpty()){
            updateCleanText()
        }
    }

    protected fun toAppSetting(packageName:String){
        //跳到设置页面中
        val intent= Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        intent.data= Uri.fromParts("package", packageName, null)
        startActivity(intent)
    }

    private fun unloadingApp(){
        if (chooseApp.isEmpty()){
            isRunUnloadingApp=false
            return
        }else{
            val runUnloadingApp=chooseApp.pop()?:return
            val packageName=runUnloadingApp.packageName?:return

            runUnloadingApp.isCheck=false
            appAdapter.changeItem(runUnloadingApp)
            updateCleanText()
            if (isInstallApp(packageName,context?:return)){
                val uri= Uri.parse("package:$packageName")
                val intent = Intent(Intent.ACTION_DELETE)
                intent.data = uri
                startActivity(intent)
            }
        }
    }

    protected fun updateCleanText(){
        if (chooseApp.isEmpty()){
            appClean.visibility=View.GONE
        }else{
            appClean.visibility=View.VISIBLE
            appClean.text="卸载${chooseApp.size}个软件"
        }
    }

    open fun onPackageAdd(appBean: AppBean){
        appAdapter.addItem(appBean)
    }

    open fun onPackageRemove(appBean: AppBean){
        appAdapter.removeItem(appBean)
    }

    open fun onPackageRemove(packageName: String){
        appAdapter.removeItem(packageName)
    }
}