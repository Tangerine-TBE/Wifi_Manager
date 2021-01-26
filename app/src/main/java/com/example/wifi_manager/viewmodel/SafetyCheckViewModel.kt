package com.example.wifi_manager.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseViewModel
import com.example.module_base.extensions.exAwait
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.calLastedTime
import com.example.module_base.utils.gsonHelper
import com.example.module_base.utils.startCountDown
import com.example.wifi_manager.db.SQliteHelper
import com.example.wifi_manager.domain.*
import com.example.wifi_manager.repository.NetSpeedTestRepository
import com.example.wifi_manager.utils.ConstantsUtil
import com.example.wifi_manager.utils.ProgressState
import com.example.wifi_manager.utils.WifiSpeedTestUtil
import com.example.wifi_manager.utils.WifiUtils
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import java.lang.Exception
import java.net.InetAddress
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author: 铭少
 * @date: 2021/1/17 0017
 * @description：
 */
class SafetyCheckViewModel : BaseViewModel() {

    //信号
    val currentWifiLevel by lazy {
        MutableLiveData<Int>()
    }
    private var oldWifiLevel = 0
    private var upSignalLevel = 0
    fun getWifiSignalLevel() {
        val level = WifiUtils.getConnectWifiSignalLevel()
        oldWifiLevel = when (level) {
            in 0 downTo -50 -> 100 + (0.2 * level).toInt()
            in -51 downTo -100 -> 90 + ((level + 50) * 1.8).toInt()
            else -> 0
        }
        val info = sp.getString(ConstantsUtil.SP_SIGNAL_INFO)
        if (info != null) {
            gsonHelper<SpSignalBean>(info)?.let {
                if (it.currentWifiName == WifiUtils.getConnectWifiName() && it.oldLevel == oldWifiLevel) {
                    currentWifiLevel.value = it.newLevel
                } else {
                    currentWifiLevel.value = oldWifiLevel
                }
            }
        } else {
            currentWifiLevel.value = oldWifiLevel
        }
    }

    //设备
    private val mDeviceInfo: MutableList<DeviceBean> = ArrayList()
    private var task: Deferred<Unit>? = null
    val scanDeviceState by lazy {
        MutableLiveData<ValueScanDevice>()
    }
    private var oldSignList: List<DeviceBean> = ArrayList()
    fun scanDevice() {
        mDeviceInfo.clear()
        scanDeviceState.value = ValueScanDevice(ProgressState.BEGIN, mDeviceInfo)
        viewModelScope.launch(Dispatchers.IO) {
            oldSignList = withContext(Dispatchers.IO) {
                SQliteHelper.findAllData()
            }
            val ipAddressString = WifiUtils.getIpAddressString()
            val last = ipAddressString.lastIndexOf(".")
            val segmentIp = ipAddressString.substring(0, last)
            repeat(CheckDeviceViewModel.RANGE_IP) {
                task = async {
                    val host = "${segmentIp}.$it"
                    try {
                        val exec = Runtime.getRuntime().exec(String.format(CheckDeviceViewModel.CMD, host))
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
                scanDeviceState.postValue(ValueScanDevice(ProgressState.END, mDeviceInfo))
                LogUtils.i("--scanDevice-------11111111----*****${mDeviceInfo.size}*************---")
            }
        }
    }

    private fun addDeviceInfo(info: InetAddress) {
        var hostSign = ""
        val hostName = info.hostName
        val hostAddress = info.hostAddress
        val hostMac = WifiUtils.getMacFromArpCache(hostAddress)
        oldSignList.apply {
            if (isNotEmpty()) {
                forEach {
                    if (it.deviceMac == hostMac) {
                        hostSign = it.deviceSign
                    }
                }
            }
        }
        mDeviceInfo.add(0, DeviceBean(hostName, hostAddress, hostMac, hostSign))
        scanDeviceState.postValue(ValueScanDevice(ProgressState.BEGIN, mDeviceInfo))
    }


    //保镖
    val protectState by lazy {
        MutableLiveData<ValueProtect>()
    }

    fun getProtectInfo() {
        sp.apply {
            val openState = getBoolean(ConstantsUtil.SP_WIFI_PROTECT_OPEN)
            protectState.value = if (openState) {
                val endTime = getLong(ConstantsUtil.SP_WIFI_PROTECT_TIME, 0L)
                ValueProtect(true, "${WifiUtils.getConnectWifiName()}已保护${
                    calLastedTime(
                            Date(endTime),
                            Date(System.currentTimeMillis())
                    )
                }天")
            } else {
                ValueProtect(false, "未开启")
            }
        }
    }


    //测速
    private var beginTime = 0L
    private var beginRxBytes = 0L
    fun startSpeedTest() {
        beginTime = System.currentTimeMillis()
        beginRxBytes = WifiSpeedTestUtil.getTotalRxBytes()
        LogUtils.i("----byteStream------${beginRxBytes}--------------------")
        viewModelScope.launch {
            NetSpeedTestRepository.getNetSpeed().exAwait({},
                    { it ->
                        if (it.code() == NET_SUCCESS) {
                            it.body()?.let {
                                startSaveFile(it)
                            }
                        }
                    })
        }
    }

    //开始速度统计
    private var mDownJob: Job? = null
    val totalRxBytes by lazy {
        MutableLiveData<ValueNetWorkSpeed>()
    }
    val downState by lazy {
        MutableLiveData<Boolean>()
    }
    private var downFinish = false
    private var start: CountDownTimer? = null
    private fun startSaveFile(response: ResponseBody) {
        mDownJob = viewModelScope.launch(Dispatchers.IO) {
            response.byteStream().apply {
                try {
                    val buf = ByteArray(1024)
                    while (read(buf, 0, buf.size).also { it } != -1) {
                        if (!isActive) {
                            break
                        }
                    }
                    if (isActive) {
                        downFinish = true
                        start?.cancel()
                        downState.postValue(true)
                    }
                    LogUtils.i("-----end--byteStream4--------${WifiSpeedTestUtil.getTotalRxBytes() - beginRxBytes}--------${System.currentTimeMillis() - beginTime}------------")
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    close()
                }
            }
        }
        //测速倒计时
        start = startCountDown(5000, 1000, {
            LogUtils.i("----byteStream3------${WifiSpeedTestUtil.getTotalRxBytes() - beginRxBytes}--------------------")
            if (!downFinish) {
                mDownJob?.cancel()
                downState.value = true
            }
        },
                {
                    totalRxBytes.value =
                            ValueNetWorkSpeed(
                                    WifiSpeedTestUtil.getTotalRxBytes() - beginRxBytes,
                                    System.currentTimeMillis() - beginTime)
                    LogUtils.i("----byteStream2------${WifiSpeedTestUtil.getTotalRxBytes() - beginRxBytes}--------------------")
                }).start()
    }




}