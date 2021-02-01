package com.example.wifi_manager.ui.fragment

import android.content.Intent
import android.text.TextUtils
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI
import com.example.module_base.activity.AboutActivity
import com.example.module_base.activity.DealViewActivity
import com.example.module_base.base.BasePopup
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.*
import com.example.module_tool.activity.DistanceActivity
import com.example.module_user.domain.ValueUserInfo
import com.example.module_user.livedata.UserInfoLiveData
import com.example.module_user.ui.activity.LoginActivity
import com.example.module_user.utils.NetState
import com.example.wifi_manager.R
import com.example.wifi_manager.ui.adapter.recycleview.MyBottomAdapter
import com.example.wifi_manager.ui.adapter.recycleview.MyTopAdapter
import com.example.wifi_manager.databinding.FragmentMyBinding
import com.example.wifi_manager.ui.activity.*
import com.example.wifi_manager.ui.popup.RemindPopup
import com.example.wifi_manager.utils.*
import com.example.wifi_manager.viewmodel.MyViewModel
import com.tamsiree.rxkit.RxNetTool

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.fragment
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 13:41:47
 * @class describe
 */
class MyFragment : BaseVmFragment<FragmentMyBinding, MyViewModel>() {
    companion object {
        const val REQUEST_CODE = 1

    }

    private val mRemindPopup by lazy {
        RemindPopup(activity).apply {
            setRemindContent("您确定要退出登录吗？")
        }
    }
    private val mLogoutPopup by lazy {
        RemindPopup(activity).apply {
            setRemindContent("您确定要注销此账号吗？")
        }
    }

    private val mMyTopAdapter by lazy {
        MyTopAdapter()
    }
    private val mMyBottomAdapter by lazy {
        MyBottomAdapter()
    }
    private var currentLoginState = false
    override fun getViewModelClass(): Class<MyViewModel> {
        return MyViewModel::class.java
    }

    override fun getChildLayout(): Int = R.layout.fragment_my
    override fun initView() {
        binding.apply {
            mMyTopContainer.layoutManager = GridLayoutManager(activity, 3)
            mMyTopAdapter.setList(DataProvider.myTopList)
            mMyTopContainer.adapter = mMyTopAdapter

            mMyBottomContainer.layoutManager = LinearLayoutManager(activity)
            mMyBottomAdapter.setList(DataProvider.myBottomList)
            mMyBottomContainer.adapter = mMyBottomAdapter

        }
    }


    override fun observerData() {
        viewModel.apply {
            logOutState.observe(this@MyFragment, {
                mLoadingDialog?.apply {
                    when (it.state) {
                        NetState.LOADING -> showDialog(activity)
                        NetState.SUCCESS, NetState.ERROR -> {
                            dismiss()
                            showToast(it.msg)
                        }
                    }

                }
            })

            UserInfoLiveData.observe(this@MyFragment, {
                userId = it.userInfo?.data?.id.toString()
                currentLoginState = it.loginState
                binding.userInfoText.text = "${if (it.loginState) userId else "登录/注册"}"
            })
        }
    }

    private var userId=""
    override fun initEvent() {
        binding.apply {
            mMyTopAdapter.setOnItemClickListener { adapter, view, position ->
                when (position) {
                    0 ->{
                        checkAppPermission(DataProvider.askCameraPermissionLis,{
                            startActivityForResult(Intent(activity, ScanActivity::class.java), REQUEST_CODE)
                        },{
                            showToast("无法获得权限，请重试！！！")
                        },fragment = this@MyFragment)

                    }
                    1 -> {
                        if (sp.getBoolean(ConstantsUtil.SP_WIFI_PROTECT_OPEN)) {
                            toOtherActivity<WifiProtectInfoViewActivity>(activity) {}
                        } else {
                            toOtherActivity<WifiProtectViewActivity>(activity) {}
                        }

                    }
                    2 -> if (RxNetTool.isWifiConnected(requireContext())) toOtherActivity<SpeedTestViewActivity>(
                            activity
                    ) {} else showToast(ConstantsUtil.NO_CONNECT_WIFI)
                    3 -> toOtherActivity<DistanceActivity>(activity) {}
                    4 -> toOtherActivity<HardwareTweaksActivity>(activity) {}
                }
            }

            userInfoText.setOnClickListener {
                if (currentLoginState) {
                    mRemindPopup?.apply {
                        showPopupView(mMyTopContainer)
                    }
                } else {
                    toOtherActivity<LoginActivity>(activity) {}
                }

            }


            mRemindPopup.setOnActionClickListener(object : BasePopup.OnActionClickListener {
                override fun sure() {
                    UserInfoLiveData.setUserInfo(ValueUserInfo(false, null))
                }
                override fun cancel() {
                }
            })

            mLogoutPopup.setOnActionClickListener(object : BasePopup.OnActionClickListener {
                override fun sure() {
                    if (!TextUtils.isEmpty(userId)) {
                        viewModel.toLogOut(userId)
                    }
                }
                override fun cancel() {
                }
            })


        }

        mMyBottomAdapter.setOnItemClickListener { adapter, view, position ->
            when (position) {
                0 -> toOtherActivity<CancelShareViewActivity>(activity) {}
                1 -> FeedbackAPI.openFeedbackActivity()
                2 -> toOtherActivity<DealViewActivity>(activity) { putExtra(Constants.SET_DEAL1, 1) }
                3 -> toOtherActivity<DealViewActivity>(activity) { putExtra(Constants.SET_DEAL1, 2) }
                4 -> toOtherActivity<AboutActivity>(activity) { }
                5 -> toAppShop(activity)
                6 -> {
                    if (currentLoginState) {
                        mLogoutPopup?.apply {
                            showPopupView(binding.mMyTopContainer)
                        }
                    } else {
                        showToast("您还没有登录呢！")
                    }
                }
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == 1) {
            toOtherActivity<ScanResultViewActivity>(activity) { putExtra(ConstantsUtil.ZXING_RESULT_KEY, data?.getStringExtra(ConstantsUtil.ZXING_RESULT_KEY)) }
        }
    }

}