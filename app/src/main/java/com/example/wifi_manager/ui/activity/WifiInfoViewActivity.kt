package com.example.wifi_manager.ui.activity

import com.example.module_base.base.BaseViewActivity
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityWifiInfoBinding
import com.example.wifi_manager.utils.*
import com.example.wifi_manager.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_wifi_info.*


class WifiInfoViewActivity : BaseViewActivity<ActivityWifiInfoBinding>() {
    private var protect=""
    override fun getLayoutView(): Int = R.layout.activity_wifi_info
    override fun initView() {
        val name = intent.getStringExtra(ConstantsUtil.WIFI_NAME_KEY)
        val level = intent.getStringExtra(ConstantsUtil.WIFI_LEVEL_KEY)
          protect = intent.getStringExtra(ConstantsUtil.WIFI_PROTECT_KEY)?:""
        setToolBar(this, name ?: "Wifi信息", mWifiInfoToolbar)
        mWifiInfoName.text = level ?: ""
        mWifiInfoProtect.text = protect
    }

    override fun initEvent() {
        mWifiInfoToolbar.toolbarEvent(this) {}

        mCancelShareWifi.setOnClickListener {
            if (protect != HomeViewModel.OPEN) {
                toOtherActivity<CancelShareViewActivity>(this) {
                    putExtra(ConstantsUtil.WIFI_SHARE_ACTION,0)
                }
            } else {
                showToast("该WiFi是开放的WiFi！")
            }
        }

    }

}