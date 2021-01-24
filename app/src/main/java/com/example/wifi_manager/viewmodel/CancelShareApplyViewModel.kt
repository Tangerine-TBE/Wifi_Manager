package com.example.wifi_manager.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.module_base.base.BaseViewModel
import com.example.module_base.extensions.exAwait
import com.example.wifi_manager.repository.WifiInfoRepository
import com.example.wifi_manager.utils.RequestNetState

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.viewmodel
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/12 17:03:41
 * @class describe
 */
class CancelShareApplyViewModel: BaseViewModel(){

    val cancelState by lazy{
        MutableLiveData<RequestNetState>()
    }

    fun cancelShareWifi(name:String?,mac:String?,pwd:String?){
        cancelState.value = RequestNetState.LOADING
        WifiInfoRepository.cancelShareWifi(name,mac,pwd).exAwait({
            cancelState.value = RequestNetState.ERROR
        }, { it ->
            if (it.code() == NET_SUCCESS) {
                it.body()?.let {
                    cancelState.value =   if (it.code == NET_SUCCESS)  RequestNetState.SUCCESS else RequestNetState.ERROR
                }
            }

        })
    }

    fun queryShareWifi(name:String?,mac:String?,pwd:String?){
        cancelState.value = RequestNetState.LOADING
        WifiInfoRepository.queryShareWifi(name,mac,pwd).exAwait({
            cancelState.value = RequestNetState.ERROR
        }, { it ->
            if (it.code() == NET_SUCCESS) {
                it.body()?.let {
                    cancelState.value =   if (it.code == NET_SUCCESS)  RequestNetState.SUCCESS else RequestNetState.ERROR
                }
            }

        })
    }


}