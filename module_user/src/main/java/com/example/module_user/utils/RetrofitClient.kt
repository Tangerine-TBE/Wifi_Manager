package com.example.module_user.utils

import com.example.module_base.BuildConfig
import com.example.module_user.repository.api.UserService
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author: 铭少
 * @date: 2021/1/24 0024
 * @description：
 */
object RetrofitClient {
    //域名
    const val USER_URL = "https://www.aisou.club"


    private val userRetrofit = Retrofit.Builder()
        .baseUrl(USER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(createClient())
        .build()


    private fun createClient(): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
        //add log record
        if (BuildConfig.DEBUG) {
            //打印网络请求日志
            val httpLoggingInterceptor = LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("请求")
                .response("响应")
                .build()
            httpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        return httpClientBuilder.build()
    }


    fun createUserService(): UserService = userRetrofit.create(UserService::class.java)


}