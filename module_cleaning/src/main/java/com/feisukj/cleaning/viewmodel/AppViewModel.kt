package com.feisukj.cleaning.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.util.Log
import com.example.module_base.cleanbase.BaseConstant

import com.feisukj.cleaning.bean.AppBean
import java.io.File
import java.lang.ref.SoftReference

class AppViewModel:ViewModel() {
    val scanAllAppState=MutableLiveData<Boolean>(false)
    val scanAllAppState2=MutableLiveData<Boolean>(false)
    val scanAllAppState3=MutableLiveData<Boolean>(false)
    val keyword=MutableLiveData<String>()
    val listData=ArrayList<AppBean>()
    private var isCleared=false

    fun doScanAllApp(context: Context, call:((AppBean)->Unit)?=null){
        listData.clear()
        val pm=context.packageManager
        for (installedPackage in pm.getInstalledPackages(0)) {
            if (installedPackage.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM!=0){
                continue
            }
            val packageName=installedPackage.packageName
            val icon=installedPackage.applicationInfo.loadIcon(pm)
            val label=installedPackage.applicationInfo.loadLabel(pm)
            val versionName=installedPackage.versionName
            val sourceApk=installedPackage.applicationInfo.sourceDir
            val appBean=AppBean(File(sourceApk)).also {
                it.packageName=packageName
                it.label=label.toString()
                it.versionName=versionName
                it.icon= SoftReference(icon)
            }
            try {
                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                    var appBytes=0L
                    var cacheBytes=0L
                    var dataBytes=0L
                    AppBean.getAppCache(context,installedPackage.applicationInfo.uid).forEach {
                        appBytes+=it.appBytes
                        cacheBytes+=it.cacheBytes
                        dataBytes+=it.dataBytes
                    }
                    appBean.appBytes=appBytes
                    appBean.cacheBytes=cacheBytes
                    appBean.dataBytes=dataBytes
                }
            }catch (e:SecurityException){
                e.printStackTrace()
            }
            Log.d("目标版本","${label}:${installedPackage.applicationInfo.targetSdkVersion}")
            if (isCleared){
                return
            }else{
                call?.invoke(appBean)
            }
            listData.add(appBean)
        }
        BaseConstant.mainHandler.post {
            scanAllAppState.value=true
        }
    }

    fun doSearch(text:String){
        keyword.value=text
    }

    override fun onCleared() {
        super.onCleared()
        isCleared=true
        //iLog("onCleared")
    }
}