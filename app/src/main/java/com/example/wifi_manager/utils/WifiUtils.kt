@file:Suppress("DEPRECATION")

package com.example.wifi_manager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.*
import android.os.PatternMatcher
import androidx.core.content.ContextCompat.getSystemService
import com.example.module_base.base.BaseApplication.Companion.mContext
import com.example.module_base.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
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
    private fun isExist(ssid: String): WifiConfiguration? {
        @SuppressLint("MissingPermission") val configs =
            wifiManager.configuredNetworks
        for (config in configs) {
            if (config.SSID == "\"" + ssid + "\"") {
                return config
            }
        }
        return null
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

    suspend fun getConnectedIP(): List<String> = withContext(Dispatchers.IO) {
            val connectedIP = ArrayList<String>()
        val br = BufferedReader(FileReader("/proc/net/arp"))
            try {
                LogUtils.i("--------------${br.readLine()}------------")
                val readLine = br.readLine()
                while (readLine != null) {
                    if (isActive) {
                        val splitted = readLine.split(" +".toRegex()).toTypedArray()
                        if (splitted != null && splitted.size >= 4) {
                            val ip = splitted[0]
                            connectedIP.add(ip)
                            LogUtils.i("--------------${ip}------------")
                        }
                    } else {
                        break
                    }
                }
                connectedIP
            } catch (e: Exception) {
                connectedIP
            }finally {
                br.close()
            }

        }

    //连接wifi
    fun  connectWifi(wifiName: String, wifiPwd: String, networkCallback: NetworkCallback){
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
        } else {
            connectWifiPws(wifiName, wifiPwd)
        }

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


    fun getLocalMacAddress(): String {
        val info = wifiManager.connectionInfo
        return info.macAddress
    }
}