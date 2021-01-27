@file:Suppress("DEPRECATION")

package com.example.wifi_manager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.*
import android.os.Build
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.module_base.base.BaseApplication.Companion.mContext
import com.example.module_base.utils.LogUtils
import com.example.wifi_manager.viewmodel.CheckDeviceViewModel
import java.io.BufferedReader
import java.io.FileReader
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.*


/**
 * @author wujinming QQ:1245074510
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.utils
 * @class describe
 * @time 2021/1/7 15:09:19
 * @class describe
 */
object WifiUtils {
     val wifiManager: WifiManager = mContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val connectivityManager= mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    /**
     * wifi是否打开
     * @return
     */
    val isWifiEnable: Boolean
        get() {
            var isEnable = false
            if (wifiManager != null) {
                if (wifiManager.isWifiEnabled) {
                    isEnable = true
                }
            }
            return isEnable
        }

    /**
     * 打开WiFi
     */
    fun openWifi() {
        if (wifiManager != null && !isWifiEnable) {
            wifiManager.isWifiEnabled = true
        }
    }

    /**
     * 关闭WiFi
     */
    fun closeWifi() {
        if (wifiManager != null && isWifiEnable) {
            wifiManager.isWifiEnabled = false
        }
    }

    /**
     * 获取WiFi列表
     * @return
     */
    val wifiList: MutableList<ScanResult>
        get() {

            val resultList: MutableList<ScanResult> = ArrayList()
            if (wifiManager != null && isWifiEnable) {
                wifiManager.startScan()
                resultList.addAll(wifiManager.scanResults)
            }
            return resultList
        }

    /**
     * 有密码连接
     * @param ssid
     * @param pws
     */
    fun connectWifiPws(ssid: String, pws: String):Boolean   {
        val disableNetwork = wifiManager.disableNetwork(wifiManager.connectionInfo.networkId)
        val netId = wifiManager.addNetwork(getWifiConfig(ssid, pws, true))
        val enableNetwork = wifiManager.enableNetwork(netId, true)
        LogUtils.i("-----connectWifiPws-${wifiManager.connectionInfo.networkId}----$disableNetwork---------$enableNetwork------------")
        return enableNetwork
    }

    /**
     * 无密码连接
     * @param ssid
     */
    fun connectWifiNoPws(ssid: String):Boolean {
        wifiManager.disableNetwork(wifiManager.connectionInfo.networkId)
        val netId = wifiManager.addNetwork(getWifiConfig(ssid, "", false))
        val enableNetwork = wifiManager.enableNetwork(netId, true)
        LogUtils.i("-----connectWifiNoPws-------$enableNetwork----------------")
        return enableNetwork

    }

    /**
     * wifi设置
     * @param ssid
     * @param pws
     * @param isHasPws
     */
    private fun getWifiConfig(
            ssid: String,
            pws: String,
            isHasPws: Boolean
    ): WifiConfiguration {
        val config = WifiConfiguration()

        config.allowedAuthAlgorithms.clear()
        config.allowedGroupCiphers.clear()
        config.allowedKeyManagement.clear()
        config.allowedPairwiseCiphers.clear()
        config.allowedProtocols.clear()
        config.SSID = "\"" + ssid + "\""
        val tempConfig = isExist(ssid)
        if (tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId)
        }
        if (isHasPws) {
            config.preSharedKey = "\"" + pws + "\""
            config.hiddenSSID = true
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN)
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
            config.status = WifiConfiguration.Status.ENABLED
        } else {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
        }
        return config
    }

    /**
     * 得到配置好的网络连接
     * @param ssid
     * @return
     */
    @SuppressLint("MissingPermission")
     private fun isExist(ssid: String): WifiConfiguration? {
        val configuredNetworks = wifiManager.configuredNetworks
        if (configuredNetworks.size > 0) {
            for (config in configuredNetworks) {
                if (config.SSID == "\"" + ssid + "\"") {
                    return config
                }
            }
        } else {
            return null
        }
        return null
    }


    /**
     *  判断wifi是否保存过
     * @param ssid
     * @return
     */
    @SuppressLint("MissingPermission")
     fun isSaveWifiPwd(ssid: String): Boolean {
        val configuredNetworks = wifiManager.configuredNetworks
        if (configuredNetworks.size > 0) {
            configuredNetworks.forEach {
                if (it.SSID== "\"" + ssid + "\"") {
                    LogUtils.i("-isSaveWifiPwd---------$ssid-----${it.SSID}-------")
                    return true
                }
            }
        } else {
            return false
        }
        return false
    }


    //保存密码连接
    fun savePwdConnect(ssid:String):Boolean{
        val wifiConfig = isExist(ssid)
        return if (wifiConfig != null) {
            wifiManager.enableNetwork(wifiConfig.networkId, true)
        } else {
            false
        }
    }



    /**
     * 获取本机 ip地址
     *
     * @return
     */
    fun getIpAddressString(): String{
        try {
            val enNetI = NetworkInterface.getNetworkInterfaces()
            while (enNetI.hasMoreElements()) {
                val netI = enNetI.nextElement()
                val enumIpAddr = netI.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (inetAddress is Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "0.0.0.0"
    }

    //连接有密码wifi
    fun  connectPwdWifi(wifiName: String, wifiPwd: String, networkCallback: NetworkCallback)=
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val specifier = WifiNetworkSpecifier.Builder()
                    .setSsidPattern(PatternMatcher(wifiName, PatternMatcher.PATTERN_PREFIX))
                    .setWpa2Passphrase(wifiPwd)
                    .build()



            val request = NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .setNetworkSpecifier(specifier)
                    .build()
                connectivityManager.requestNetwork(request, networkCallback)
                true
        } else {
            connectWifiPws(wifiName, wifiPwd)
        }

    //连接有无密码wifi
    fun  connectNoPwdWifi(wifiName: String, networkCallback: NetworkCallback)=
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val specifier = WifiNetworkSpecifier.Builder()
                    .setSsidPattern(PatternMatcher(wifiName, PatternMatcher.PATTERN_PREFIX))
                    .build()

            val request = NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .setNetworkSpecifier(specifier)
                    .build()
            connectivityManager.requestNetwork(request, networkCallback)
            true
        } else {
            connectWifiNoPws(wifiName)

    }





    fun getConnectWifiName(): String {
        var realName=""
        val wifiInfo = wifiManager.connectionInfo
        var name = wifiInfo.ssid
        if (name!="<unknown ssid>"&&name!=""){
            realName= wifiInfo.ssid.replace("\"", "")
        }
        LogUtils.i("----getConnectWifiName------${realName}------------")
        return realName
    }

    fun getConnectWifiSignalLevel():Int{
        val info: WifiInfo = wifiManager.connectionInfo
        return  info.rssi
    }


  private  fun getLocalMacAddress(): String {
        val info = wifiManager.connectionInfo
        return info.macAddress
    }

    fun getMacFromArpCache(ip: String): String {
        var br: BufferedReader?=null
        try {
            br = BufferedReader(FileReader(CheckDeviceViewModel.FILE_NAME))
            var line: String
            while (br.readLine().also { line = it } != null) {
                val splitted = line.split(" +".toRegex()).toTypedArray()
                if (splitted != null && splitted.size >= 4 && ip == splitted[0]) {
                    return splitted[3]
                }
            }
        } catch (e: java.lang.Exception) {

        } finally {
            br?.close()
        }
        return getLocalMacAddress()
    }

    //是否加密
    fun getCipherType():Boolean{
      wifiManager?.scanResults?.apply {
          forEach {
              if (it.SSID == getConnectWifiName()) {
                  val capabilities: String = it.capabilities
                  return if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
                      true
                  } else capabilities.contains("WEP") || capabilities.contains("wep")
                  }
          }

    }
        return false
    }

}