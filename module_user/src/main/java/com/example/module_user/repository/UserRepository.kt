package com.example.module_user.repository
import com.example.module_user.utils.RetrofitClient
import kotlinx.coroutines.Dispatchers

/**
 * @author: 铭少
 * @date: 2021/1/24 0024
 * @description：
 */
object UserRepository {

    suspend fun getUserVerCode(params: Map<String, Any>)=
         RetrofitClient.createUserService().getVerCode(params)

    suspend fun userRegister(params: Map<String, Any>)
            =RetrofitClient.createUserService().toRegister(params)

    suspend  fun userFindPwd(params: Map<String, Any>)
            =RetrofitClient.createUserService().toFindPwd(params)

    suspend fun userLogin(params: Map<String, Any>)
            =RetrofitClient.createUserService().toLogin(params)

    suspend fun userLogOut(params: Map<String, Any>)
            =RetrofitClient.createUserService().toLogout(params)

}