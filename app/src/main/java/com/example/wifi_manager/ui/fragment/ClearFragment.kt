package com.example.wifi_manager.ui.fragment

import android.os.CountDownTimer
import com.example.module_base.base.BaseViewFragment
import com.example.module_base.utils.LogUtils
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.FragmentClearBinding
import com.example.wifi_manager.ui.widget.ConnectWifiView

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.fragment
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 13:41:32
 * @class describe
 */
class ClearFragment:BaseViewFragment<FragmentClearBinding>() {
    override fun getChildLayout(): Int= R.layout.fragment_clear

    override fun initView() {
     //   lifecycle.addObserver(binding.connectWifiView)

    }

}