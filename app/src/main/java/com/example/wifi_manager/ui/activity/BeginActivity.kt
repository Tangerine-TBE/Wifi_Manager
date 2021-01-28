package com.example.wifi_manager.ui.activity

import android.view.KeyEvent
import com.example.module_ad.advertisement.SplashHelper
import com.example.module_ad.utils.AdMsgUtil
import com.example.module_base.base.BasePopup
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.*
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityBeginBinding
import com.example.wifi_manager.ui.popup.AgreementPopup
import com.example.wifi_manager.utils.DataProvider.askLocationPermissionLis
import com.example.wifi_manager.viewmodel.BeginViewModel

class BeginActivity : BaseVmViewActivity<ActivityBeginBinding, BeginViewModel>() {

    private var showConut = 0
    private val mSplashHelper by lazy {
        SplashHelper(this, binding.mAdContainer, MainViewActivity::class.java)
    }
    private val mAgreementPopup by lazy {
        AgreementPopup(this)
    }

    override fun getViewModelClass(): Class<BeginViewModel> {
        return BeginViewModel::class.java
    }

    override fun getLayoutView(): Int = R.layout.activity_begin
    override fun initView() {
        viewModel.loadAdMsg()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        MyStatusBarUtil.fullScreenWindow(hasFocus, this)
        if (showConut < 1) {
            if (hasFocus) {
                sp.apply {
                    if (getBoolean(Constants.IS_FIRST, true)) {
                        //陪伴天数计数
                        putLong(Constants.FIRST_TIME, System.currentTimeMillis())
                        mAgreementPopup?.showPopupView(binding.mAdContainer)
                        showConut++
                    } else {
                       mSplashHelper.showAd()
                    }
                }
            }
        }
    }


    override fun initEvent() {
        mAgreementPopup.setOnActionClickListener(object : BasePopup.OnActionClickListener {
            override fun sure() {
                checkAppPermission(
                    askLocationPermissionLis, {
                        if (AdMsgUtil.getADKey() != null) {
                            mSplashHelper.showAd()
                        } else {
                            goHome()
                        }
                    },
                    {
                        goHome()
                    }, this@BeginActivity
                )
            }

            override fun cancel() {
                finish()
            }
        })

    }

    fun goHome() {
        toOtherActivity<MainViewActivity>(this@BeginActivity, true) {}
    }

    //禁用返回键
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean =
        if (keyCode == KeyEvent.KEYCODE_BACK) true
        else super.onKeyDown(keyCode, event)


}