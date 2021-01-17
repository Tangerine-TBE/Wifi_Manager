package com.example.wifi_manager.ui.activity

import android.text.TextUtils
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseVmViewActivity
import com.example.wifi_manager.R
import com.example.wifi_manager.ui.adapter.recycleview.CancelShareApplyAdapter
import com.example.wifi_manager.databinding.ActivityCancelShareApplyBinding
import com.example.wifi_manager.ui.popup.CheckMacHelpPopup
import com.example.wifi_manager.utils.DataProvider
import com.example.wifi_manager.utils.setToolBar
import com.example.wifi_manager.utils.toolbarEvent
import com.example.wifi_manager.viewmodel.CancelShareApplyViewModel
import com.tamsiree.rxkit.RxKeyboardTool
import com.tamsiree.rxkit.view.RxToast
import kotlinx.android.synthetic.main.activity_cancel_share_apply.*

class CancelShareApplyViewActivity : BaseVmViewActivity<ActivityCancelShareApplyBinding,CancelShareApplyViewModel>() {
    private val mCancelApplyAdapter by lazy {
        CancelShareApplyAdapter()
    }
    private val mCheckHelpPopupWindow by lazy {
        CheckMacHelpPopup(this)
    }

    override fun getLayoutView(): Int=R.layout.activity_cancel_share_apply
    override fun getViewModelClass(): Class<CancelShareApplyViewModel> {
        return CancelShareApplyViewModel::class.java
    }

    override fun initView() {
        setToolBar(this,"取消分享申请",mCancelShareApplyToolbar)
        mInputContainer.layoutManager=LinearLayoutManager(this)
        mCancelApplyAdapter.setList(DataProvider.shareApplyList)
        mInputContainer.adapter=mCancelApplyAdapter
        mCancelApplyAdapter.addChildClickViewIds(R.id.mShareHelp)
    }

    override fun observerData() {

    }

    override fun initEvent() {

        mCancelShareApplyToolbar.toolbarEvent(this){}

        mCommitApply.setOnClickListener {
            mCancelApplyAdapter.getInputContent()?.let {
                val name = it[0]
                val mac = it[1]
                val pwd = it[2]
                if (!TextUtils.isEmpty(name) and !TextUtils.isEmpty(mac) and !TextUtils.isEmpty(pwd)) {

                } else {
                    RxToast.normal("请输入完整的wifi信息")
                }
            }
        }

        mCancelApplyAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.mShareHelp -> {
                        RxKeyboardTool.hideSoftInput(this)
                        mCheckHelpPopupWindow.showPopupView(mInputContainer, Gravity.CENTER)
                }
            }


        }
    }

}