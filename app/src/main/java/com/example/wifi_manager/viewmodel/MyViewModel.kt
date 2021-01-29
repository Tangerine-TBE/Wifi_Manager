package com.example.wifi_manager.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.module_base.base.BaseViewModel
import com.example.module_user.domain.ValueResult
import com.example.module_user.domain.ValueUserInfo
import com.example.module_user.livedata.UserInfoLiveData
import com.example.module_user.repository.UserRepository
import com.example.module_user.utils.Constants
import com.example.module_user.utils.NetState
import com.example.module_user.utils.UserInfoHelper
import com.example.module_user.utils.UserInfoUtil

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.viewmodel
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 13:42:18
 * @class describe
 */
class MyViewModel: BaseViewModel() {

    val logOutState by lazy {
        MutableLiveData<ValueResult>()
    }

    fun toLogOut(id:String){
        doRequest({
            logOutState.postValue(ValueResult(NetState.LOADING, ""))
            UserRepository.userLogOut(UserInfoHelper.userLogOut(Constants.DELETE_USER,id))?.let {
                if (it.ret == NET_SUCCESS) {
                    logOutState.postValue(ValueResult(NetState.SUCCESS, "账号注销成功！"))
                    UserInfoLiveData.setUserInfo(ValueUserInfo(false,null))
                } else {
                    logOutState.postValue(ValueResult(NetState.ERROR, it.msg))
                }
            }
        }, {
            logOutState.postValue(ValueResult(NetState.ERROR, "网络错误！"))
        })
    }


}