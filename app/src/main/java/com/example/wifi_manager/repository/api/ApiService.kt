package com.example.wifi_manager.repository.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
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
    @GET("original/im/QQ9.2.2.26569.exe")
     fun downFile(): Call<ResponseBody>

}