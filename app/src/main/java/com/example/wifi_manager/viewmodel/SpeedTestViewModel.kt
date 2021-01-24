package com.example.wifi_manager.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseViewModel
import com.example.module_base.utils.LogUtils
import com.example.wifi_manager.domain.ValueNetWorkSpeed
import com.example.module_base.extensions.exAwait
import com.example.module_base.utils.startCountDown
import com.example.wifi_manager.repository.NetSpeedTestRepository
import com.example.wifi_manager.utils.*
import kotlinx.coroutines.*
import okhttp3.ResponseBody


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.viewmodel
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/13 13:58:16
 * @class describe
 */
class SpeedTestViewModel: BaseViewModel() {
    companion object{
        const val millisinfuture=8000L
        const val pingTime=2000L
        const val countDownInterval=1000L
    }

    private var mDownJob: Job?=null
    private var beginTime=0L
    private var beginRxBytes=0L


    val totalRxBytes by lazy {
        MutableLiveData<ValueNetWorkSpeed>()
    }

    val downState by lazy {
        MutableLiveData<Boolean>()
    }

    val pingValue by lazy {
        MutableLiveData<Int>()
    }


    //10582382
    //10594013

    //开始下载文件测速
   private fun startSpeedTest(){
        beginTime=System.currentTimeMillis()
        beginRxBytes=WifiSpeedTestUtil.getTotalRxBytes()
        LogUtils.i("----byteStream------${beginRxBytes}--------------------")
        viewModelScope.launch {
        NetSpeedTestRepository.getNetSpeed().exAwait({},
                    { it ->
                        if (it.code()==NET_SUCCESS) {
                            it.body()?.let {
                                startSaveFile(it)
                            }
                        }
            })
        }
    }


    //开始速度统计
    private var downFinish=false
    private var start:CountDownTimer?=null
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
                        downFinish= true
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
        start = startCountDown(millisinfuture, countDownInterval, {
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

    //停止速度统计
    fun stopSaveFile(){
        mDownJob?.cancel()
    }

    fun startPing(){
        startCountDown(pingTime,countDownInterval,{
            viewModelScope.launch{
                withContext(Dispatchers.IO){
                pingValue.postValue(PingUtils.getAvgRTT(ConstantsUtil.PING_URL, 4, 1))
                }
                startSpeedTest()
            }

        },{
        }).start()
    }




}