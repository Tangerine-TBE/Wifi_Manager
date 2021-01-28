package com.example.wifi_manager.viewmodel

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseViewModel
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.getCurrentThreadName
import com.example.wifi_manager.domain.ValueNetWorkHint
import com.example.wifi_manager.domain.ValueRefreshWifi
import com.example.wifi_manager.domain.WifiMessageBean
import com.example.module_base.extensions.exAwait
import com.example.module_base.utils.calLastedTime
import com.example.wifi_manager.repository.WifiInfoRepository
import com.example.wifi_manager.ui.fragment.HomeFragment
import com.example.wifi_manager.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.viewmodel
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 13:42:18
 * @class describe
 */
class HomeViewModel : BaseViewModel() {

    companion object {
        const val WPA2 = "WPA2"
        const val WPA_AND_WPA2 = "WPA/WPA2"
        const val OPEN = "开放"

        const val LEVEL_HIGH = "强"
        const val LEVEL_MIDDLE = "一般"
        const val LEVEL_LOW = "弱"

        const val PROTECT_WAY_ONE = "[WPA2"
        const val PROTECT_WAY_TWO = "[WPA"

    }


    val wifiState by lazy {
        MutableLiveData<WifiState>()
    }
    val wifiContentEvent by lazy {
        MutableLiveData<ValueRefreshWifi>()
    }

    val errorConnectCount by lazy {
        MutableLiveData<Int>()
    }

    val connectingCount by lazy {
        MutableLiveData<Int>()
    }

    val connectError by lazy {
        MutableLiveData<Boolean>()
    }

    val currentNetWorkName by lazy {
        MutableLiveData(ValueNetWorkHint(HomeFragment.NET_NOT_CONNECT_HINT, HomeFragment.NET_NOT_CONNECT))
    }

    val protectTimeOut by lazy {
        MutableLiveData<Boolean>()
    }


    private val netWorkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)


            }

            override fun onLost(network: Network) {
                super.onLost(network)
            }

            override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
            ) {

            }
        }
    }


    private val mOldWifiMessageBeans: MutableList<WifiMessageBean> = ArrayList()
    private val mCurrentWifiMessageBeans: MutableList<WifiMessageBean> = ArrayList()

    fun setWifiState(state: WifiState) {
        wifiState.value = state
    }

    @SuppressLint("MissingPermission")
    fun getWifiList(state: WifiContentState) {
        mCurrentWifiMessageBeans?.let { list ->
            val wifiList = WifiUtils.wifiList.filter { it.SSID != "" }
            if (wifiList.isNotEmpty()) {
                list.clear()
                wifiList.forEach {
                    list.add(WifiMessageBean(it.SSID, it.BSSID, it.capabilities, it.level, wifiSignalState(it.level), wifiProtectState(it.capabilities),saveWifiPwdState = WifiUtils.isSaveWifiPwd(it.SSID)))
                }
                list.sortWith(compareBy({ it.wifiProtectState.length }, { it.wifiName }))
                mOldWifiMessageBeans?.clear()
                mOldWifiMessageBeans?.addAll(list)
                setWifiContent(state, list)
                getUserShareList(list)
            } else {
                if (mOldWifiMessageBeans?.size > 0) {
                    setWifiContent(state, mOldWifiMessageBeans)
                    getUserShareList(mOldWifiMessageBeans)
                }
            }
        }
    }

    private var realList: MutableList<WifiMessageBean> = ArrayList()
    private fun getUserShareList(list: MutableList<WifiMessageBean>) {
        viewModelScope.launch(Dispatchers.IO) {
    //    LogUtils.i("-----newData-------begin---------------------")
        val filterList: MutableList<WifiMessageBean> = list.filter { it.wifiProtectState != OPEN }.toMutableList()
        realList = if (filterList.size > 20) filterList.subList(0, 20) else filterList
        val addressList = StringBuffer()
        realList.forEach {
            addressList.append("${it.wifiMacAddress},")
            //LogUtils.i("-----newData---realList--${it.wifiName}--------------$addressList---------")
        }
        WifiInfoRepository.getShareWifiList(addressList.substring(0, addressList.length - 1)).exAwait({
          //  LogUtils.i("-----newData----center--${it.message}-----------------------")
        }, { it ->
            if (it.code() == NET_SUCCESS) {
                it.body()?.apply {
                    if (code == NET_SUCCESS) {
                        data.list?.let {
                            it.forEach { newData ->
                                list.forEach { oldData ->
                                    if (oldData.wifiMacAddress == newData.address) {
                                        oldData.wifiPwd = newData.password
                                        oldData.shareState = true
                                    }
                                }
                           //     LogUtils.i("-----newData----end--${newData.name}-----------------------")
                            }
                            setWifiContent(WifiContentState.NONE, list)
                        }

                    } else {
                   //     LogUtils.i("-----getUserShareList-----${msg}-----------------------")
                    }
                }
            }

        })
        }
    }


    private fun setWifiContent(state: WifiContentState, list: MutableList<WifiMessageBean>) {
        wifiContentEvent.postValue(ValueRefreshWifi(state, list))
    }

    fun shareWifiInfo(wifiMessages: WifiMessageBean) {
        if (!wifiMessages.shareState) {
            viewModelScope.launch(Dispatchers.IO){
                WifiInfoRepository.shareWifi(wifiMessages.wifiName, wifiMessages.wifiMacAddress, wifiMessages.wifiPwd, wifiMessages.encryptionWay).exAwait(
                        {
                     //       LogUtils.i("--------shareWifiInfo------${it.message}--------")
                        },
                        { result ->
                            val body = result.body()
                     //       LogUtils.i("--------shareWifiInfo---------------${body?.string()}---------------------------")
                        })
            }
        }

    }

    fun setCurrentNetState(info: ValueNetWorkHint) {
        currentNetWorkName.postValue(info)
    }


    private fun wifiSignalState(level: Int) =
            when (level) {
                in 0 downTo -20 -> LEVEL_HIGH
                in -20 downTo -50 -> LEVEL_HIGH
                in -50 downTo -60 -> LEVEL_MIDDLE
                in -60 downTo -70 -> LEVEL_MIDDLE
                in -70 downTo -1000 -> LEVEL_LOW
                else -> LEVEL_HIGH
            }

    private fun wifiProtectState(capabilities: String) = when {
        capabilities.startsWith(PROTECT_WAY_ONE) -> {
            WPA2
        }
        capabilities.startsWith(PROTECT_WAY_TWO) -> {
            WPA_AND_WPA2
        }
        else -> {
            OPEN
        }
    }

    fun connectWifi(wifiMessage: WifiMessageBean, open: Boolean, wifiPwd: String = "") {
        viewModelScope.launch(Dispatchers.Default) {
            if (open) {
             //   connectError.postValue(WifiUtils.connectNoPwdWifi(wifiMessage.wifiName, netWorkCallback))
                connectError.postValue(WifiUtils.connectWifiNoPws(wifiMessage.wifiName))
            } else {
              //  connectError.postValue(WifiUtils.connectPwdWifi(wifiMessage.wifiName, wifiPwd, netWorkCallback))
                connectError.postValue(WifiUtils.connectWifiPws(wifiMessage.wifiName, wifiPwd))
            }
    //        LogUtils.i("--------connectWifi-${getCurrentThreadName()}-----${wifiMessage.wifiName}--------")
        }
    }

    fun savePwdConnectWifi(SSID:String){
        connectError.postValue(WifiUtils.savePwdConnect(SSID))
    }



    fun connectAction(wifiMessage: WifiMessageBean, showPopupAction: () -> Unit) {
        if (wifiMessage.wifiProtectState == OPEN) {
            connectWifi(wifiMessage, true)
        }
        showPopupAction()
    }


    private var mConnectErrorCount = 0
    private var mConnectingCount = 0
    fun setConnectErrorCount(count: Int) {
        mConnectErrorCount += count
        errorConnectCount.value = mConnectErrorCount
     //   LogUtils.i("---没连接上---------${mConnectErrorCount}-----------")
    }

    fun setConnectingCount(count: Int) {
        mConnectingCount += count
        connectingCount.value = mConnectingCount
  //      LogUtils.i("---正在连接---------${mConnectingCount}-----------")
    }


    fun clearConnectCount() {
        mConnectErrorCount = 0
        mConnectingCount = 0
    }


    fun checkProtectTimeOut() {
        //wifi保镖计数
        sp.apply {
            val isOpen = getBoolean(ConstantsUtil.SP_WIFI_PROTECT_OPEN, false)
            if (isOpen) {
                val time = getLong(ConstantsUtil.SP_WIFI_PROTECT_TIME, 0L)
                    val calLastedTime = calLastedTime(Date(), Date(time))
            //        LogUtils.i("----isOpen-------$calLastedTime----------------")
                    if (calLastedTime > 7) {
                            putBoolean(ConstantsUtil.SP_WIFI_PROTECT_OPEN, false)
                            putString(ConstantsUtil.SP_WIFI_PROTECT_NAME, "")
                            putLong(ConstantsUtil.SP_WIFI_PROTECT_DAY, 0L)
                            putLong(ConstantsUtil.SP_WIFI_PROTECT_TIME, 0L)
                            protectTimeOut.value = true
                    } else {
                        putLong(ConstantsUtil.SP_WIFI_PROTECT_DAY, System.currentTimeMillis())
                    }
            }

        }
    }

}