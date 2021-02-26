package com.example.wifi_manager.ui.activity


import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BasePopup
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.utils.*
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityWifiProtectBinding
import com.example.wifi_manager.ui.adapter.recycleview.WifiProtectAdapter
import com.example.wifi_manager.ui.popup.RemindPopup
import com.example.wifi_manager.utils.ConstantsUtil
import com.example.wifi_manager.utils.DataProvider
import com.example.wifi_manager.utils.WifiUtils
import java.util.*

class WifiProtectViewActivity : BaseViewActivity<ActivityWifiProtectBinding>() {

    override fun getLayoutView(): Int = R.layout.activity_wifi_protect

    private val mProtectInfoPopup by lazy {
        RemindPopup(this).apply {
            setRemindContent("是否对“${WifiUtils.getConnectWifiName()}”开启保镖？每个人只能保护一个哦，请确保你是WiFi主人才会生效")
        }
    }

    override fun initView() {
        binding.apply {
            setToolBar(this@WifiProtectViewActivity,"wifi保镖",mWifiProtectToolbar)

            mWifiProtectContainer.layoutManager=LinearLayoutManager(this@WifiProtectViewActivity)
            val wifiProtectAdapter1 = WifiProtectAdapter()
            wifiProtectAdapter1.setList(DataProvider.wifiProtectList)
            mWifiProtectContainer.adapter=wifiProtectAdapter1
        }

    }


    override fun initEvent() {
        binding.apply {
            mWifiProtectToolbar.toolbarEvent(this@WifiProtectViewActivity){}

            mProtectInfoPopup.setOnActionClickListener(object : BasePopup.OnActionClickListener {
                override fun sure() {
                        toOtherActivity<WifiProtectInfoViewActivity>(
                            this@WifiProtectViewActivity,
                            true
                        ) {}
                        sp.apply {
                            putBoolean(ConstantsUtil.SP_WIFI_PROTECT_OPEN, true)
                            putString(
                                ConstantsUtil.SP_WIFI_PROTECT_NAME,
                                WifiUtils.getConnectWifiName()
                            )
                            //1611986400000
                            putLong(ConstantsUtil.SP_WIFI_PROTECT_TIME, System.currentTimeMillis())
                            putLong(ConstantsUtil.SP_WIFI_PROTECT_DAY, System.currentTimeMillis())
                        }

                }

                override fun cancel() {

                }
            })

            mOpenProtect.setOnClickListener {
                if (WifiUtils.getCipherType()) {
                    mProtectInfoPopup.showPopupView(mWifiProtectContainer)
                } else {
                    showToast("开放WiFi无法开启保护哦")
                }
            }


        }
    }

    override fun release() {
        mProtectInfoPopup.dismiss()
    }


}