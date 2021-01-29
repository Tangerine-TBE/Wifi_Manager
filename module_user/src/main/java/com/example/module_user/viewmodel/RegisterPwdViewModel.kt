package com.example.module_user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.module_base.base.BaseApplication
import com.example.module_base.base.BaseViewModel
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.PackageUtil
import com.example.module_user.domain.ValueResult
import com.example.module_user.domain.ValueUserInfo
import com.example.module_user.domain.login.LoginBean
import com.example.module_user.livedata.UserInfoLiveData
import com.example.module_user.repository.UserRepository
import com.example.module_user.utils.*

/**
 * @name Wifi_Manager
 * @class name：com.example.module_user.viewmodel
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/29 10:42:47
 * @class describe
 */
class RegisterPwdViewModel : BaseViewModel() {

    val codeState by lazy {
        MutableLiveData<ValueResult>()
    }

    val loginState by lazy {
        MutableLiveData<ValueResult>()
    }

    val registerState by lazy {
        MutableLiveData<ValueResult>()
    }

    val findPwdState by lazy {
        MutableLiveData<ValueResult>()
    }

    //获取验证码
    fun getVerCode(number: String) {
        doRequest({
            codeState.postValue(ValueResult(NetState.LOADING, ""))
            UserRepository.getUserVerCode(
                UserInfoHelper.userEvent(
                    Constants.GET_CODE,
                    mapOf(
                        Constants.PACKAGE to BaseApplication.mPackName,
                        Constants.MOBILE to number
                    )
                )
            )?.let {
                if (it.ret == 200) {
                    codeState.postValue(ValueResult(NetState.SUCCESS, "验证码获取成功！"))
                } else {
                    codeState.postValue(ValueResult(NetState.ERROR, it.msg))
                }
            }
        }, {
            codeState.postValue(ValueResult(NetState.ERROR, NET_ERROR))
        })
    }


    //登录
    private fun toLocalLogin(number: String, pwd: String) {
        val md5Pwd = ApiMapUtil.md5(pwd)
        doRequest({
            UserRepository.userLogin(
                UserInfoHelper.userEvent(
                    Constants.LOGIN,
                    mapOf(Constants.MOBILE to number, Constants.PASSWORD to md5Pwd)
                )
            )?.string()?.let { it ->
                GsonUtil.setUserResult<LoginBean>(it, {
                    //  UserInfoUtil.saveUserInfo(it,  UserInfoUtil.saveUserType(Constants.LOCAL_TYPE, number, md5Pwd, ""))
                    UserInfoUtil.saveUserMsg(it)
                    UserInfoLiveData.setUserInfo(ValueUserInfo(true, it))
                    loginState.postValue(ValueResult(NetState.SUCCESS, "注册成功！"))

                }, {
                    loginState.postValue(ValueResult(NetState.ERROR, it.msg))
                })
            }
        }) {
            loginState.postValue(ValueResult(NetState.ERROR, NET_ERROR))
        }
    }

    //注册
    fun toRegister(number: String, code: String, pwd: String) {
        doRequest({
            registerState.postValue(ValueResult(NetState.LOADING, ""))
            UserRepository.userRegister(
                UserInfoHelper.userEvent(
                    Constants.ADD_USER, mapOf(
                        Constants.MOBILE to number,
                        Constants.CODE to code,
                        Constants.PASSWORD to pwd,
                        Constants.PACKAGE to BaseApplication.mPackName,
                        Constants.PLATFORM to PackageUtil.getAppMetaData(
                            BaseApplication.application,
                            Constants.PLATFORM_KEY
                        )
                    )
                )
            )?.let {
                if (it.ret == NET_SUCCESS) {
                    toLocalLogin(number, pwd)
                } else {
                    registerState.postValue(ValueResult(NetState.ERROR, it.msg))
                }
            }
        }, {
            registerState.postValue(ValueResult(NetState.ERROR, NET_ERROR))
        })

    }

    //找回密码
    fun toFindPwd(number: String, code: String, pwd: String) {
        doRequest({
            findPwdState.postValue(ValueResult(NetState.LOADING, ""))
            UserRepository.userFindPwd(
                UserInfoHelper.userEvent(
                    Constants.FIND_PWD, mapOf(
                        Constants.MOBILE to number,
                        Constants.CODE to code,
                        Constants.PASSWORD to  ApiMapUtil.md5(pwd)
                    )
                )
            )?.let {
                if (it.ret == NET_SUCCESS) {
                    findPwdState.postValue(ValueResult(NetState.SUCCESS, "找回密码成功！"))
                } else {
                    findPwdState.postValue(ValueResult(NetState.ERROR, it.msg))
                }
            }
        }, {
            findPwdState.postValue(ValueResult(NetState.ERROR, it))

        })

    }


}