package com.example.wifi_manager.utils

import com.example.wifi_manager.repository.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.utils
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/13 13:36:03
 * @class describe
 */
object RetrofitClient {
    //https://down.qq.com/qqweb/QQ_1/android_apk/Android_8.5.5.5105_537066978.apk
    private const val SPEED_URL = "https://down.qq.com/"
    private const val WIFI_URL = "http://wifi.aisou.club/"

    private val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()

    private val textWifiSpeedRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl(SPEED_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    private val wifiManagerRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl(WIFI_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


    fun createNetSpeed(): ApiService = textWifiSpeedRetrofit.create(ApiService::class.java)
    fun createWifiManager(): ApiService = wifiManagerRetrofit.create(ApiService::class.java)

}