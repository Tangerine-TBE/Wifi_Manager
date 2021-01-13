package com.example.wifi_manager.utils

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
   private const val  URL = "https://dl.softmgr.qq.com/"



    val textWifiSpeedRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    inline fun <reified T> createNetSpeed(): T = textWifiSpeedRetrofit.create(T::class.java)

}