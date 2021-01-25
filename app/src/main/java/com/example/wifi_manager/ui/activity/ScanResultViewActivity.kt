package com.example.wifi_manager.ui.activity

import android.content.Intent
import android.net.Uri
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.utils.copyContent
import com.example.module_base.utils.setToolBar
import com.example.module_base.utils.shareContent
import com.example.module_base.utils.toolbarEvent
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityScanResultBinding
import com.example.wifi_manager.utils.*


class ScanResultViewActivity : BaseViewActivity<ActivityScanResultBinding>() {
    private var mResult="抱歉没有扫到任何内容"
    override fun getLayoutView(): Int=R.layout.activity_scan_result
    override fun initView() {
        binding.apply {
            setToolBar(this@ScanResultViewActivity,"扫描结果",mScanToolbar)
            intent.getStringExtra(ConstantsUtil.ZXING_RESULT_KEY)?.let { mResult=it }
            mResultContent.text=mResult
        }


    }



    override fun initEvent() {
        binding.apply {
            mFindResult.setOnClickListener {
                if (mResult.startsWith("https") || mResult.startsWith("http")) {
                    startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(mResult)))
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com/s?wd=${mResult}")))
                }
            }

            mCopy.setOnClickListener {
                copyContent(this@ScanResultViewActivity,mResult)
            }

            mScanToolbar.toolbarEvent(this@ScanResultViewActivity){shareContent(this@ScanResultViewActivity,mResult)}
        }

    }


}