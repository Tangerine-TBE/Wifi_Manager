package com.example.wifi_manager.ui.activity

import com.example.module_base.base.BaseViewActivity
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivitySpeedTestResultBinding
import com.example.wifi_manager.utils.*
import com.tamsiree.rxui.view.RxProgressBar
import com.tamsiree.rxui.view.roundprogressbar.RxRoundProgress
import kotlinx.android.synthetic.main.activity_speed_test_result.*

class SpeedTestResultViewActivity : BaseViewActivity<ActivitySpeedTestResultBinding>() {
    override fun getLayoutView(): Int=R.layout.activity_speed_test_result
    override fun initView() {
        binding.apply {
            setToolBar(this@SpeedTestResultViewActivity, "网络测速", mSpeedResultToolbar)
            intent.apply {
                val delay = getStringExtra(ConstantsUtil.WIFI_DELAY_KEY)
                val downSpeed = getStringExtra(ConstantsUtil.WIFI_DOWN_LOAD_KEY)
                mWifiDelay.text="${delay?:"0"}ms"
                mWifiDownSpeed.text="${downSpeed?:"0"}MB/s"
                mWifiSpeedRange.text="用户宽带在${WifiSpeedTestUtil.netWorkLevel(downSpeed?.toFloat())}之间"
                mSpeedWifiName.text=getConnectWifiName()
        }
    }

    }

    override fun initEvent() {
        binding.mSpeedResultToolbar.toolbarEvent(this){}
    }
}