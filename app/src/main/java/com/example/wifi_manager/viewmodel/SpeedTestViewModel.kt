package com.example.wifi_manager.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.module_base.utils.LogUtils
import com.example.wifi_manager.domain.NetWorkSpeedBean
import com.example.wifi_manager.extensions.exAwait
import com.example.wifi_manager.repository.NetSpeedTestRepository
import com.example.wifi_manager.utils.*
import com.tamsiree.rxkit.RxConstTool
import com.tamsiree.rxkit.RxTimeTool
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.util.*


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.viewmodel
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/13 13:58:16
 * @class describe
 */
class SpeedTestViewModel:ViewModel() {
    companion object{
        const val millisinfuture=8000L
        const val pingTime=2000L
        const val countDownInterval=1000L
    }

    private var mDownJob: Job?=null
    private var beginTime=0L
    private var beginRxBytes=0L


    val totalRxBytes by lazy {
        MutableLiveData<NetWorkSpeedBean>()
    }

    val downState by lazy {
        MutableLiveData<Boolean>(false)
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
                        if (it.code()==HttpURLConnection.HTTP_OK) {
                            it.body()?.let {

                                startSaveFile(it)
                            }
                        }
            })
        }
    }

    //开始速度统计
     private fun startSaveFile(response: ResponseBody) {
        mDownJob = viewModelScope.launch(Dispatchers.IO) {
            response.byteStream().apply {
                try {
                    val buf = ByteArray(1024)
                    while (read(buf, 0, buf.size).also { it } != -1) {
                        if (!isActive) {
                            LogUtils.i("----byteStream4------${WifiSpeedTestUtil.getTotalRxBytes()}--------------------")
                            break
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    close()
                }
            }
        }

        //测速倒计时
        startCountDown(millisinfuture, countDownInterval, {
            LogUtils.i("----byteStream3------${WifiSpeedTestUtil.getTotalRxBytes()-beginRxBytes}--------------------")
            mDownJob?.cancel()
            downState.value = true

        },
            {
                totalRxBytes.value=
                    NetWorkSpeedBean(
                        WifiSpeedTestUtil.getTotalRxBytes() - beginRxBytes,
                        System.currentTimeMillis() - beginTime)
                LogUtils.i("----byteStream2------${WifiSpeedTestUtil.getTotalRxBytes() - beginRxBytes}--------------------")
            })

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
        })
    }




}