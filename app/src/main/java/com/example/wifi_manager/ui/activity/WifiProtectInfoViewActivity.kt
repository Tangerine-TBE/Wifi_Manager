package com.example.wifi_manager.ui.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.utils.LogUtils
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityWifiProtectInfoBinding
import com.example.wifi_manager.utils.setToolBar
import com.example.wifi_manager.utils.toolbarEvent
import com.tamsiree.rxkit.RxServiceTool
import kotlinx.android.synthetic.main.activity_wifi_protect_info.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.math.BigInteger
import java.net.InetAddress


class WifiProtectInfoViewActivity : BaseViewActivity<ActivityWifiProtectInfoBinding>() {
    override fun getLayoutView(): Int = R.layout.activity_wifi_protect_info

    override fun initView() {
        setToolBar(this,"开启保护",mProtectInfoToolbar)
    }

    override fun initEvent() {
        mProtectInfoToolbar.toolbarEvent(this){}
    }



}