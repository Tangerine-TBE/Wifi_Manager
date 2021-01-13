package com.example.wifi_manager.ui.activity

import com.example.module_base.base.BaseActivity
import com.example.module_base.widget.MyToolbar
import com.example.wifi_manager.R
import com.example.wifi_manager.utils.ConstantsUtil
import com.example.wifi_manager.utils.setToolBar
import com.example.wifi_manager.utils.toOtherActivity
import kotlinx.android.synthetic.main.activity_wifi_info.*


class WifiInfoActivity : BaseActivity() {
    override fun getLayoutView(): Int =R.layout.activity_wifi_info

    override fun initView() {
        val name = intent.getStringExtra(ConstantsUtil.WIFI_NAME_KEY)
        val level = intent.getStringExtra(ConstantsUtil.WIFI_LEVEL_KEY)
        val protectWay = intent.getStringExtra(ConstantsUtil.WIFI_PROTECT_KEY)
        setToolBar(this,name?:"Wifi信息",mWifiInfoToolbar)
        mWifiInfoName.text=level?:""
        mWifiInfoProtect.text=protectWay?:""
    }
    override fun initEvent() {

        mWifiInfoToolbar.setOnBackClickListener(object :MyToolbar.OnBackClickListener{
            override fun onBack() {
                finish()
            }

            override fun onRightTo() {

            }

        })

        mCancelShareWifi.setOnClickListener {
            toOtherActivity<CancelShareActivity>(this){}
        }

    }

}