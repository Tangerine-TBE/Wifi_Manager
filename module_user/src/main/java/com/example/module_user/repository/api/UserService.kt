package com.example.module_user.repository.api


import com.example.module_user.domain.RegisterBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.QueryMap

/**
 * @author: 铭少
 * @date: 2021/1/24 0024
 * @description：
 */
interface UserService {

    @POST("/api.php")
    suspend fun getVerCode(@QueryMap params: Map<String, @JvmSuppressWildcards Any>):RegisterBean


    @POST("/api.php")
    suspend  fun toRegister(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): RegisterBean


    @POST("/api.php")
    suspend fun toFindPwd(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): RegisterBean


    @POST("/api.php")
    suspend fun toLogin(@QueryMap params: Map<String, @JvmSuppressWildcards Any>):ResponseBody


    @POST("/api.php")
    suspend  fun toLogout(@QueryMap params: Map<String,@JvmSuppressWildcards Any>): RegisterBean

}