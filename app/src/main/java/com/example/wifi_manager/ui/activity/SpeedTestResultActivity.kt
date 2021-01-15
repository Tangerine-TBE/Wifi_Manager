package com.example.wifi_manager.ui.activity

import com.example.module_base.base.BaseActivity
import com.example.wifi_manager.R
import com.example.wifi_manager.utils.ConstantsUtil
import com.example.wifi_manager.utils.setToolBar
import com.example.wifi_manager.utils.toolbarEvent
import kotlinx.android.synthetic.main.activity_speed_test_result.*

class SpeedTestResultActivity : BaseActivity() {
    override fun getLayoutView(): Int=R.layout.activity_speed_test_result
    override fun initView() {
        setToolBar(this, "网络测速", mSpeedResultToolbar)
        intent.apply {
            val delay = getStringExtra(ConstantsUtil.WIFI_DELAY_KEY)
            val downSpeed = getStringExtra(ConstantsUtil.WIFI_DOWN_LOAD_KEY)
            val upSpeed = getStringExtra(ConstantsUtil.WIFI_UP_LOAD_KEY)

            mWifiDelay.text="${delay?:"0"}ms"
            mWifiDownSpeed.text="${downSpeed?:"0"}MB/s"
            mWifiUpSpeed.text="${upSpeed?:"0"}KB/s"
        }

    }

    override fun initEvent() {
        mSpeedResultToolbar.toolbarEvent(this){}
    }
}