package com.example.wifi_manager.ui.activity

import com.example.module_ad.advertisement.AdType
import com.example.module_ad.advertisement.FeedHelper
import com.example.module_ad.advertisement.InsertHelper
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.utils.setToolBar
import com.example.module_base.utils.toolbarEvent
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivitySpeedTestResultBinding
import com.example.wifi_manager.utils.*

class SpeedTestResultViewActivity : BaseViewActivity<ActivitySpeedTestResultBinding>() {
    private val feedHelper by lazy {
        FeedHelper(this,binding.bottomAd)
    }
    private val insertHelper by lazy {
        InsertHelper(this)
    }

    override fun getLayoutView(): Int=R.layout.activity_speed_test_result
    override fun initView() {
        binding.apply {
            setToolBar(this@SpeedTestResultViewActivity, "网络测速", mSpeedResultToolbar)
            intent.apply {
                val delay = getStringExtra(ConstantsUtil.WIFI_DELAY_KEY)
                val downSpeed = getStringExtra(ConstantsUtil.WIFI_DOWN_LOAD_KEY)
                mWifiDelay.text="${delay?:"0"}ms"
                mSpeedWifiName.text=getConnectWifiName()
                downSpeed?.let {
                    mWifiDownSpeed.text="${it}MB/s"
                    mWifiSpeedRange.text="用户宽带在${WifiSpeedTestUtil.netWorkLevel(it.toFloat())}之间"
                }
        }

            insertHelper.showAd(AdType.CLEAN_FINISHED)
            feedHelper.showAd(AdType.CLEAN_FINISHED)

    }

    }

    override fun initEvent() {
        binding.mSpeedResultToolbar.toolbarEvent(this){}
    }

    override fun release() {
        insertHelper.releaseAd()
        feedHelper.releaseAd()
    }
}