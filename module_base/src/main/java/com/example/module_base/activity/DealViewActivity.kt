package com.example.module_base.activity

import android.graphics.Color
import com.example.module_base.R
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.databinding.ActivityDealBinding
import com.example.module_base.utils.Constants
import com.example.module_base.utils.MyStatusBarUtil
import com.example.module_base.utils.PackageUtil
import com.example.module_base.utils.toolbarEvent
import com.example.module_base.widget.MyToolbar
import com.tamsiree.rxkit.RxKeyboardTool
import kotlinx.android.synthetic.main.activity_deal.*


class DealViewActivity : BaseViewActivity<ActivityDealBinding>()  {


    var mTitleMsg="用户协议"
    var mContent=R.string.user


    override fun setFullScreenWindow() {
        MyStatusBarUtil.setColor(this, Color.TRANSPARENT)
    }
    override fun getLayoutView(): Int = R.layout.activity_deal
    override fun initView() {
        when (intent.getIntExtra(Constants.SET_DEAL1, 0)) {
            1 -> {
                mTitleMsg="用户协议"
                mContent=R.string.user
            }
            2-> {
                mTitleMsg="隐私协议"
                mContent=R.string.privacy
            }
            3->{
                mTitleMsg="功能说明"
                mContent=R.string.shareText
            }

        }
        privacy_toolbar.setTitle(mTitleMsg)
        text.text = PackageUtil.difPlatformName(this,mContent)
    }

    override fun initEvent() {

        privacy_toolbar.toolbarEvent(this) {}




    }

}