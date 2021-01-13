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
import com.tamsiree.rxkit.RxTimeTool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection


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
        mDownJob = viewModelScope.launch(Dispatchers.IO) {
            val inputStream: InputStream = response.byteStream()
            try {
                val buf = ByteArray(1024)
                while (inputStream.read(buf, 0, buf.size).also { it } != -1) {
                //    outputStream.write(buf, 0, len)
                    totalRxBytes.postValue(NetWorkSpeedBean(WifiSpeedTestUtil.getTotalRxBytes()-beginRxBytes,System.currentTimeMillis()-beginTime))
                }
                LogUtils.i("---beginTime------$beginTime---------")

                LogUtils.i("-------saveFile------完成--------耗时-${(System.currentTimeMillis()-beginTime)/1000}---")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inputStream.close()
            }
        }
    }


    //停止保存
    fun stopSaveFile(){
        totalRxBytes.postValue(NetWorkSpeedBean(WifiSpeedTestUtil.getTotalRxBytes(),System.currentTimeMillis()-beginTime))
        mDownJob?.cancel()
    }



}