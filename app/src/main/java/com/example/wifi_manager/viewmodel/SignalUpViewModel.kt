package com.example.wifi_manager.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseViewModel
import com.example.module_base.domain.AppInfo
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.PackageUtil
import com.example.wifi_manager.domain.SpSignalBean
import com.example.wifi_manager.utils.ConstantsUtil
import com.example.wifi_manager.utils.ProgressState
import com.example.wifi_manager.utils.WifiUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.viewmodel
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/22 10:41:31
 * @class describe
 */
class SignalUpViewModel : BaseViewModel() {

    private val mAppList:MutableList<AppInfo> = ArrayList()

    val appDataList by lazy {
        MutableLiveData<MutableList<AppInfo>>()
    }


    val animationState by lazy {
        MutableLiveData<ProgressState>()
    }

    val optimizeCount by lazy {
        MutableLiveData<Int>()
    }

    val wifiSignalLevel by lazy {
        MutableLiveData<Int>()
    }

    val wifiSignalUp by lazy {
        MutableLiveData<Int>()
    }



    fun getAppInfo(){
        mAppList.clear()
        viewModelScope.launch(Dispatchers.IO){
            LogUtils.i("*----wjm---------begin-------------------------")
            context.packageManager.getInstalledPackages(0)?.onEach {
                    if (PackageUtil.isConnectNetWorkApp(context, it.packageName)) {
                            mAppList.add(0,
                                    AppInfo(it.applicationInfo.loadLabel(context.packageManager).toString(),
                                            it.packageName, it.versionName, it.versionCode,
                                            it.applicationInfo.loadIcon(context.packageManager)
                                    )
                            )
                        appDataList.postValue(mAppList)
                        }
                }
            setOptimizeCount(8)
            delay(500)
            animationState.postValue(ProgressState.END)
            getSignalUpLevel()
            }
        }


    private var oldWifiLevel=0
    private var upSignalLevel=0
    fun getWifiSignalLevel(){
        val level = WifiUtils.getConnectWifiSignalLevel()
        oldWifiLevel = when (level) {
            in 0 downTo -50 ->100+(0.2*level).toInt()
            in -51 downTo -100->90+((level+50)*1.8).toInt()
            else -> 0
        }
        val info = sp.getString(ConstantsUtil.SP_SIGNAL_INFO)
        if (info != null) {
          Gson().fromJson(info, SpSignalBean::class.java)?.let {
              if (it.currentWifiName == WifiUtils.getConnectWifiName()&&it.oldLevel==oldWifiLevel) {
                  wifiSignalLevel.value = it.newLevel
              } else {
                  wifiSignalLevel.value=oldWifiLevel
              }
            }
        } else {
            wifiSignalLevel.value=oldWifiLevel
        }
        setSignalUpLevel()
    }

   private fun setSignalUpLevel(){
       upSignalLevel = when(oldWifiLevel) {
            in 90 until 100->99-oldWifiLevel
            in 75 until 90->Random().nextInt(10)
            in 50 until 75->Random().nextInt(15)
             in 0 until 50->Random().nextInt(20)
           else -> 6
       }

       wifiSignalUp.value =upSignalLevel
    }


    private fun getSignalUpLevel(){
        val level = oldWifiLevel + upSignalLevel
        wifiSignalLevel.postValue(if (level < 99) level else 99)
        sp.putString(ConstantsUtil.SP_SIGNAL_INFO,Gson().toJson(SpSignalBean(WifiUtils.getConnectWifiName(),
            oldWifiLevel,
            level)))
    }



    fun setAnimationState(state:ProgressState){
        animationState.value=state
    }


    fun setOptimizeCount(count:Int){
        optimizeCount.postValue(count)
    }



}