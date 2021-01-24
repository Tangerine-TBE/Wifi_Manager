package com.example.wifi_manager.ui.activity


import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.utils.setToolBar
import com.example.module_base.utils.toOtherActivity
import com.example.module_base.utils.toolbarEvent
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityWifiProtectBinding
import com.example.wifi_manager.ui.adapter.recycleview.WifiProtectAdapter
import com.example.wifi_manager.utils.DataProvider

import kotlinx.android.synthetic.main.activity_wifi_protect.*
class WifiProtectViewActivity : BaseViewActivity<ActivityWifiProtectBinding>() {

    override fun getLayoutView(): Int = R.layout.activity_wifi_protect

    override fun initView() {
        setToolBar(this,"wifi保镖",mWifiProtectToolbar)

        mWifiProtectContainer.layoutManager=LinearLayoutManager(this)
        val wifiProtectAdapter1 = WifiProtectAdapter()
        wifiProtectAdapter1.setList(DataProvider.wifiProtectList)
        mWifiProtectContainer.adapter=wifiProtectAdapter1

    }


    override fun initEvent() {
        mWifiProtectToolbar.toolbarEvent(this){}

        mOpenProtect.setOnClickListener {
            toOtherActivity<WifiProtectInfoViewActivity>(this){}
        }

    }



}