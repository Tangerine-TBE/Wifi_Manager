package com.example.wifi_manager.viewmodel

import androidx.core.content.contentValuesOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseViewModel
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.getCurrentThreadName
import com.example.wifi_manager.db.SQliteHelper
import com.example.wifi_manager.domain.DeviceBean
import com.example.wifi_manager.domain.ValueScanDevice
import com.example.wifi_manager.utils.ProgressState
import com.example.wifi_manager.utils.WifiUtils
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.FileReader
import java.lang.Exception
import java.net.InetAddress

/**
 * @author: 铭少
 * @date: 2021/1/16 0016
 * @description：
 */
class CheckDeviceViewModel: BaseViewModel() {
    companion object{
        const val CMD = "/system/bin/ping -c 1 %s"
        const val RANGE_IP=255
        const val FILE_NAME="/proc/net/arp"
    }
    private val mDeviceInfo:MutableList<DeviceBean> = ArrayList()
    private var task:Deferred<Unit>?=null
    val scanDeviceState by lazy {
        MutableLiveData<ValueScanDevice>()
    }

    private var oldSignList:List<DeviceBean> = ArrayList()
    fun scanDevice(){
        mDeviceInfo.clear()
        scanDeviceState.value= ValueScanDevice(ProgressState.BEGIN,mDeviceInfo)
        viewModelScope.launch(Dispatchers.IO) {
            oldSignList=withContext(Dispatchers.IO){
              SQliteHelper.findAllData()
            }
            val ipAddressString = WifiUtils.getIpAddressString()
            val last = ipAddressString.lastIndexOf(".")
            val segmentIp = ipAddressString.substring(0, last)
            repeat(RANGE_IP){
                task = async {
                     val host = "${segmentIp}.$it"
                     try {
                         val exec = Runtime.getRuntime().exec(String.format(CMD, host))
                         val waitFor = exec.waitFor()
                         if (waitFor == 0) {
                             val info = InetAddress.getByName(host)
                             addDeviceInfo(info)
                             LogUtils.i("--scanDevice---${info.hostAddress}-------------${info.hostName}--${mDeviceInfo.size}-")
                         }
                     } catch (e: Exception) {
                         addDeviceInfo(InetAddress.getByName(host))
                     }
                 }

            }
            task?.await().let {
                scanDeviceState.postValue(ValueScanDevice(ProgressState.END,mDeviceInfo))
                LogUtils.i("--scanDevice-------11111111----*****${mDeviceInfo.size}*************---")
            }
        }
    }


    private fun addDeviceInfo(info:InetAddress){
        var hostSign=""
        val hostName = info.hostName
        val hostAddress = info.hostAddress
        val hostMac = WifiUtils.getMacFromArpCache(hostAddress)
        oldSignList.apply {
            if (isNotEmpty()) {
                forEach {
                    if (it.deviceMac==hostMac) {
                        hostSign=it.deviceSign
                    }
                }
            }
        }
        mDeviceInfo.add(0,DeviceBean(hostName,hostAddress,hostMac,hostSign))
        scanDeviceState.postValue(ValueScanDevice(ProgressState.BEGIN,mDeviceInfo))
    }


    fun getSignName(position:Int):String=mDeviceInfo.run {
        if (size > position) {
            this[position].deviceSign
        } else {
            ""
        }
    }


    fun saveSign(name:String, position:Int) {
            viewModelScope.launch(Dispatchers.Main) {
                if (mDeviceInfo.size>position){
                    val deviceBean = mDeviceInfo[position]
                    deviceBean.deviceSign=name
                    if (SQliteHelper.saveData<DeviceBean>("deviceMac=?","${deviceBean.deviceMac}",deviceBean) { contentValuesOf("deviceSign" to name) }) {
                        mDeviceInfo[position] = deviceBean
                        scanDeviceState.value=ValueScanDevice(ProgressState.NONE,mDeviceInfo)
                    }
            }
        }

    }





}