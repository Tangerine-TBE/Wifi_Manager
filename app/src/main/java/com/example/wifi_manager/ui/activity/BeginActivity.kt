package com.example.wifi_manager.ui.activity

import android.view.KeyEvent
import com.example.module_ad.advertisement.SplashHelper
import com.example.module_ad.advertisement.TTAdManagerHolder
import com.example.module_ad.utils.AdMsgUtil
import com.example.module_ad.utils.Contents
import com.example.module_base.base.BaseApplication
import com.example.module_base.base.BasePopup
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.*
import com.example.wifi_manager.R
import com.example.wifi_manager.base.MainApplication
import com.example.wifi_manager.databinding.ActivityBeginBinding
import com.example.wifi_manager.ui.popup.AgreementPopup
import com.example.wifi_manager.utils.DataProvider.askAllPermissionLis
import com.example.wifi_manager.viewmodel.BeginViewModel
import com.umeng.commonsdk.UMConfigure

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
        sp.putBoolean(Contents.NO_BACK, true)
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
                        TTAdManagerHolder.init(BaseApplication.application)
                        //友盟
                        UMConfigure.init(applicationContext, UMConfigure.DEVICE_TYPE_PHONE, "601a1119aa055917f8816f3a")
                        UMConfigure.setLogEnabled(true)
                       mSplashHelper.showAd()
                    }
                }
            }
        }
    }


    override fun initEvent() {
        mAgreementPopup.setOnActionClickListener(object : BasePopup.OnActionClickListener {
            override fun sure() {
                TTAdManagerHolder.init(BaseApplication.application)
                //友盟
                UMConfigure.init(applicationContext, UMConfigure.DEVICE_TYPE_PHONE, "601a1119aa055917f8816f3a")
                UMConfigure.setLogEnabled(true)
                checkAppPermission(
                    askAllPermissionLis, {
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


    override fun release() {
        sp.putBoolean(Contents.NO_BACK, false)
        mAgreementPopup.dismiss()
    }

}