package com.example.wifi_manager.repository.api

import com.example.wifi_manager.domain.CancelShareResult
import com.example.wifi_manager.domain.RequestWifiBean
import com.example.wifi_manager.domain.querywifi.queryShareWifiResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

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


     @POST("customer/using/infoUpload")
     fun shareWifiInfo(@Query("name") name:String,@Query("address") address:String,@Query("password") password:String,@Query("method") method:String): Call<ResponseBody>

    @POST("customer/using/cancelShare")
    fun cancelShareWifiInfo(@Query("name") name:String?,@Query("address") address:String?,@Query("password") password:String?): Call<CancelShareResult>

    @POST("customer/using/shareQuery")
    fun queryShareWifiInfo(@Query("name") name:String?,@Query("address") address:String?,@Query("password") password:String?): Call<queryShareWifiResult>

    @POST("customer/using/historyList")
    fun getShareWifiContent(@Query("address") address:String?):Call<RequestWifiBean>

}