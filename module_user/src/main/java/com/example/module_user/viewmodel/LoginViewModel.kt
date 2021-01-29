package com.example.module_user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.module_base.base.BaseApplication
import com.example.module_base.base.BaseViewModel
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.getCurrentThreadName
import com.example.module_user.domain.ValueResult
import com.example.module_user.domain.ValueUserInfo
import com.example.module_user.domain.login.LoginBean
import com.example.module_user.livedata.UserInfoLiveData
import com.example.module_user.repository.UserRepository
import com.example.module_user.utils.*

/**
 * @author: 铭少
 * @date: 2021/1/24 0024
 * @description：
 */
class LoginViewModel : BaseViewModel() {



    val loginState by lazy {
        MutableLiveData<ValueResult>()
    }


    //登录
    fun toLocalLogin(number: String, pwd: String) {
        loginState.postValue(ValueResult(NetState.LOADING,""))
        val md5Pwd = ApiMapUtil.md5(pwd)
        doRequest({
            UserRepository.userLogin(UserInfoHelper.userEvent(Constants.LOGIN,
                    mapOf(Constants.MOBILE to number, Constants.PASSWORD to md5Pwd)))?.string()?.let { it ->
                GsonUtil.setUserResult<LoginBean>(it, {
                    UserInfoUtil.saveUserMsg(it)
                    UserInfoLiveData.setUserInfo(ValueUserInfo(true,it))
                    loginState.postValue(ValueResult(NetState.SUCCESS,"登录成功！"))
                    LogUtils.i("-----toLocalLogin-111- ${getCurrentThreadName()}-----${it.msg}------------------")
                }, {
                    LogUtils.i("-----toLocalLogin-111- ${getCurrentThreadName()}-----${it.msg}------------------")
                    loginState.postValue(ValueResult(NetState.ERROR,it.msg))
                })
            }
        }) {
            loginState.postValue(ValueResult(NetState.ERROR, NET_ERROR))
        }
    }


}