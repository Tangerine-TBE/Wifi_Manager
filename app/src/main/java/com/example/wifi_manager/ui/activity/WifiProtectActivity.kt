package com.example.wifi_manager.ui.activity


import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseActivity
import com.example.wifi_manager.R
import com.example.wifi_manager.ui.adapter.recycleview.WifiProtectAdapter
import com.example.wifi_manager.utils.DataProvider
import com.example.wifi_manager.utils.setToolBar
import com.example.wifi_manager.utils.toolbarEvent
import kotlinx.android.synthetic.main.activity_wifi_protect.*
class WifiProtectActivity : BaseActivity() {

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

        }

    }



}