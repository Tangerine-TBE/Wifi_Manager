package com.example.wifi_manager.repository.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.repository.api
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/13 13:41:17
 * @class describe
 */
interface ApiService {
    @Streaming
    @GET("qqweb/QQ_1/android_apk/Android_8.5.5.5105_537066978.apk")
     fun downFile(): Call<ResponseBody>

}