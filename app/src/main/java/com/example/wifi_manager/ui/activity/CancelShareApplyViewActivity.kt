package com.example.wifi_manager.ui.activity

import android.text.TextUtils
import android.view.Gravity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.*
import com.example.module_base.utils.Rx.RxKeyboardTool
import com.example.module_tool.utils.toast
import com.example.wifi_manager.R
import com.example.wifi_manager.ui.adapter.recycleview.CancelShareApplyAdapter
import com.example.wifi_manager.databinding.ActivityCancelShareApplyBinding
import com.example.wifi_manager.ui.popup.CheckMacHelpPopup
import com.example.wifi_manager.utils.*
import com.example.wifi_manager.viewmodel.CancelShareApplyViewModel

class CancelShareApplyViewActivity : BaseVmViewActivity<ActivityCancelShareApplyBinding,CancelShareApplyViewModel>() {

    companion object{
        const val TOOLBAR_NAME_CANCEL="取消分享申请"
        const val TOOLBAR_NAME_CHECK="查询分享WIFI"

        const val TOAST_CANCEL_SUCCESS = "取消分享申请成功"
        const val TOAST_CANCEL_ERROR = "取消分享申请失败"
        const val TOAST_CHECK_SUCCESS = "该WiFi已经被分享"
        const val TOAST_CHECK_ERROR = "查询失败"




        const val TOAST_EMPTY = "请输入完整的wifi信息"
        const val HINT_TOP_CANCEL = "请确保以下信息都填写正确，否则会因为无法验证您的主人有效身份证而无法审核通过哦！"
        const val HINT_TOP_CHECK = "请确保以下信息都填写正确，否则会无法准确查询到热点是否被分享"
        const val HINT_BOTTOM_CANCEL= "个人WiFi变成共享WiFi，是由于"+"\n"+"1) 曾经连过WiFi的朋友把密码主动分享了"+"\n"+"2) wifi被误判为公共场所WiFi"
        const val HINT_BOTTOM_CHECK= "因为全国同名WIFI非常多，所以需要你正确填写以上信息，以便查询是否被分享。以上查询针对WIFI钥匙共享平台，其他平台数据无法查询。"
        const val BT_TEXT_CANCEL="立即提交"
        const val BT_TEXT_CHECK="立即查询"
    }


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

    private var mState=0
    override fun initView() {
        binding.apply {
            mState=intent.getIntExtra(ConstantsUtil.WIFI_SHARE_ACTION,0)
            setToolBar(this@CancelShareApplyViewActivity, if (mState==0) TOOLBAR_NAME_CANCEL else TOOLBAR_NAME_CHECK ,mCancelShareApplyToolbar)

            topHint.text= if (mState==0) HINT_TOP_CANCEL else HINT_TOP_CHECK
            bottomHint.text=if (mState==0) HINT_BOTTOM_CANCEL else HINT_BOTTOM_CHECK
            mCommitApply.text=if (mState==0) BT_TEXT_CANCEL else BT_TEXT_CHECK

            mInputContainer.layoutManager=LinearLayoutManager(this@CancelShareApplyViewActivity)
            mCancelApplyAdapter.setList(DataProvider.shareApplyList)
            mInputContainer.adapter=mCancelApplyAdapter
            mCancelApplyAdapter.addChildClickViewIds(R.id.mShareHelp)
        }

    }

    override fun observerData() {
        viewModel.apply {
            cancelState.observe(this@CancelShareApplyViewActivity, Observer { state ->
                mLoadingDialog.apply {

                    when(state){
                        RequestNetState.LOADING->showDialog(this@CancelShareApplyViewActivity)
                        RequestNetState.ERROR->{
                            dismiss()
                            showToast( if(mState==0) TOAST_CANCEL_ERROR else TOAST_CHECK_ERROR)
                        }
                        RequestNetState.SUCCESS->{
                            dismiss()
                            showToast(if(mState==0) TOAST_CANCEL_SUCCESS else TOAST_CHECK_SUCCESS)
                        }
                    }
                }

            })
        }
    }

    override fun initEvent() {
    binding.apply {
        mCancelShareApplyToolbar.toolbarEvent(this@CancelShareApplyViewActivity){
            toAppShop(this@CancelShareApplyViewActivity)
        }

        mCommitApply.setOnClickListener {
            mCancelApplyAdapter.getInputContent()?.let {
                val name = it[0]
                val mac = it[1]
                val pwd = it[2]
                if (!TextUtils.isEmpty(name) and !TextUtils.isEmpty(mac) and !TextUtils.isEmpty(pwd)) {
                    if (mState == 0) {
                        viewModel.cancelShareWifi(name, mac, pwd)
                    } else {
                        viewModel.queryShareWifi(name, mac, pwd)
                    }
                } else {
                    toast(TOAST_EMPTY)
                }
            }
        }

        mCancelApplyAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.mShareHelp -> {
                    RxKeyboardTool.hideSoftInput(this@CancelShareApplyViewActivity)
                    mCheckHelpPopupWindow.showPopupView(mInputContainer, Gravity.CENTER)
                }
            }


        }
    }

    }

    override fun release() {
        mCheckHelpPopupWindow.dismiss()
    }

}