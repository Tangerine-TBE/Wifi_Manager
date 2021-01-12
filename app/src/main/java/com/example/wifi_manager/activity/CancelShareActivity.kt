package com.example.wifi_manager.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.module_base.base.BaseActivity
import com.example.module_base.utils.MyStatusBarUtil
import com.example.module_base.widget.MyToolbar
import com.example.wifi_manager.R
import com.example.wifi_manager.utils.setToolBar
import com.example.wifi_manager.utils.toOtherActivity
import kotlinx.android.synthetic.main.activity_cancel_share.*
import kotlinx.android.synthetic.main.activity_scan_result.*

class CancelShareActivity : BaseActivity() {
    override fun getLayoutView(): Int=R.layout.activity_cancel_share

    override fun initView() {
        setToolBar(this,"取消分享",mCancelShareWifiToolbar)
    }

    override fun initEvent() {

        mApplyShare.setOnClickListener {
            toOtherActivity<CancelShareApplyActivity>(this){}
        }

        mCancelShareWifiToolbar.setOnBackClickListener(object :MyToolbar.OnBackClickListener{
            override fun onBack() {
                finish()
            }

            override fun onRightTo() {

            }
        })
    }

}