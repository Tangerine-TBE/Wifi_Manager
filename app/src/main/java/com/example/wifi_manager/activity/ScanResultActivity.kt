package com.example.wifi_manager.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.module_base.base.BaseActivity
import com.example.module_base.utils.MarginStatusBarUtil
import com.example.module_base.utils.MyStatusBarUtil
import com.example.module_base.widget.MyToolbar
import com.example.wifi_manager.R
import com.example.wifi_manager.utils.ConstantsUtil
import com.example.wifi_manager.utils.copyContent
import com.example.wifi_manager.utils.shareContent
import kotlinx.android.synthetic.main.activity_scan_result.*

class ScanResultActivity : BaseActivity() {
    private var mResult="抱歉没有扫到任何内容"
    override fun getLayoutView(): Int=R.layout.activity_scan_result
    override fun initView() {
        MyStatusBarUtil.setColor(this,Color.WHITE)
        mScanToolbar.setTitle("扫描结果")
        intent.getStringExtra(ConstantsUtil.ZXING_RESULT)?.let { mResult=it }
        mResultContent.text=mResult

    }

    override fun initEvent() {
        mFindResult.setOnClickListener {
            if (mResult.startsWith("https") || mResult.startsWith("http")) {
                startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(mResult)))
            } else {
                copyContent(this,mResult)
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com")))
            }
        }

        mCopy.setOnClickListener {
            copyContent(this,mResult)
        }


        mScanToolbar.setOnBackClickListener(object :MyToolbar.OnBackClickListener{
            override fun onBack() {
                finish()
            }
            override fun onRightTo() {
                shareContent(this@ScanResultActivity,mResult)
            }
        })
    }



}