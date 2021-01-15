package com.example.wifi_manager.ui.activity

import com.example.module_base.base.BaseActivity
import com.example.module_base.widget.MyToolbar
import com.example.wifi_manager.R
import com.example.wifi_manager.utils.setToolBar
import com.example.wifi_manager.utils.toOtherActivity
import com.example.wifi_manager.utils.toolbarEvent
import kotlinx.android.synthetic.main.activity_cancel_share.*

class CancelShareActivity : BaseActivity() {
    override fun getLayoutView(): Int=R.layout.activity_cancel_share

    override fun initView() {
        setToolBar(this,"取消分享",mCancelShareWifiToolbar)
    }

    override fun initEvent() {
        mApplyShare.setOnClickListener {
            toOtherActivity<CancelShareApplyActivity>(this){}
        }
        mCancelShareWifiToolbar.toolbarEvent(this){}
    }

}