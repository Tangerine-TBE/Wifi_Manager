package com.example.wifi_manager.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.module_base.utils.LogUtils
import com.example.wifi_manager.domain.NetWorkSpeedBean
import com.example.wifi_manager.extensions.exAwait
import com.example.wifi_manager.repository.NetSpeedTestRepository
import com.example.wifi_manager.utils.FileUtil
import com.example.wifi_manager.utils.WifiSpeedTestUtil
import com.example.wifi_manager.utils.calLastedTime
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

    private var mDownJob: Job?=null
    private var beginTime=0L
    private var beginRxBytes=0L

    val totalRxBytes by lazy {
        MutableLiveData<NetWorkSpeedBean>()
    }


    //开始下载文件测速
    fun startSpeedTest(){
        beginTime=System.currentTimeMillis()
        LogUtils.i("---beginTime------$beginTime---------")
        beginRxBytes=WifiSpeedTestUtil.getTotalRxBytes()
        totalRxBytes.value= NetWorkSpeedBean(beginRxBytes,beginTime)
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


    //保存文件
    private fun startSaveFile(response: ResponseBody){
        mDownJob = viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                val byteStream = response.byteStream()
                try {
                                val buf = ByteArray(1024)
                                while (byteStream.read(buf, 0, buf.size).also { it } != -1) {
                                    if (isActive) {
                                        totalRxBytes.postValue(NetWorkSpeedBean(WifiSpeedTestUtil.getTotalRxBytes() - beginRxBytes, System.currentTimeMillis() - beginTime))
                                    } else {
                                        LogUtils.i("-------saveFile------${beginTime}---------------${System.currentTimeMillis()}--完成--------耗时-${calLastedTime(Date(System.currentTimeMillis()), Date(beginTime))}---")
                                        break
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            } finally {
                            byteStream.close()
                            }


            }

        }
    }


    //停止保存
    fun stopSaveFile(){
        mDownJob?.cancel()
    }



}