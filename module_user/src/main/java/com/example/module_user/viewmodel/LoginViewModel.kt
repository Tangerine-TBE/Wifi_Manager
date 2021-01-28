package com.example.module_user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.module_base.base.BaseApplication
import com.example.module_base.base.BaseViewModel
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.getCurrentThreadName
import com.example.module_user.domain.login.LoginBean
import com.example.module_user.repository.UserRepository
import com.example.module_user.utils.ApiMapUtil
import com.example.module_user.utils.Constants
import com.example.module_user.utils.GsonUtil
import com.example.module_user.utils.UserInfoHelper

/**
 * @author: 铭少
 * @date: 2021/1/24 0024
 * @description：
 */
class LoginViewModel : BaseViewModel() {



    val loginState by lazy {
        MutableLiveData<Boolean>()
    }

    fun getVerCode(number: String) {
        doRequest({
            val userVerCode = UserRepository.getUserVerCode(
                    UserInfoHelper.userEvent(Constants.GET_CODE, mapOf(Constants.PACKAGE to BaseApplication.mPackName, Constants.MOBILE to number))
            )

        }, {
            LogUtils.i("-----getVerCode-- ${getCurrentThreadName()}-----$it------------------")
        })
    }


    //登录
    fun toLocalLogin(number: String, pwd: String) {
        val md5Pwd = ApiMapUtil.md5(pwd)
        doRequest({
            UserRepository.userLogin(UserInfoHelper.userEvent(Constants.LOGIN,
                    mapOf(Constants.MOBILE to number, Constants.PASSWORD to md5Pwd)))?.string()?.let { it ->
                GsonUtil.setUserResult<LoginBean>(it, {
                    loginState.postValue(true)
                    LogUtils.i("-----toLocalLogin-111- ${getCurrentThreadName()}-----${it.msg}------------------")
                }, {
                    LogUtils.i("-----toLocalLogin-111- ${getCurrentThreadName()}-----${it.msg}------------------")
                    loginState.postValue(false)
                })
            }
        }) {
            loginState.postValue(false)
        }
    }


}