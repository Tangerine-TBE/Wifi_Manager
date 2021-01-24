package com.example.wifi_manager.ui.activity

import com.example.module_base.base.BaseViewActivity
import com.example.module_base.utils.setToolBar
import com.example.module_base.utils.toAppShop
import com.example.module_base.utils.toOtherActivity
import com.example.module_base.utils.toolbarEvent
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityCancelShareBinding
import com.example.wifi_manager.utils.ConstantsUtil



class CancelShareViewActivity : BaseViewActivity<ActivityCancelShareBinding>() {
    override fun getLayoutView(): Int=R.layout.activity_cancel_share

    override fun initView() {
        setToolBar(this,"取消分享",binding.mCancelShareWifiToolbar)
    }

    override fun initEvent() {
        binding.apply {
            toAppShop.setOnClickListener {
                toAppShop(this@CancelShareViewActivity)
            }

            mApplyShare.setOnClickListener {
                toOtherActivity<CancelShareApplyViewActivity>(this@CancelShareViewActivity){
                    putExtra(ConstantsUtil.WIFI_SHARE_ACTION,0)
                }
            }
            mCancelShareWifiToolbar.toolbarEvent(this@CancelShareViewActivity){
                toOtherActivity<CancelShareApplyViewActivity>(this@CancelShareViewActivity){
                    putExtra(ConstantsUtil.WIFI_SHARE_ACTION,1)
                }
            }
        }

    }

}