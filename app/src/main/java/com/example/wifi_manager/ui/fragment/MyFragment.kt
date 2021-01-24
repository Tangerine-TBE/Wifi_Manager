package com.example.wifi_manager.ui.fragment

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI
import com.example.module_base.activity.DealViewActivity
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.Constants
import com.example.module_base.utils.showToast
import com.example.module_base.utils.toOtherActivity
import com.example.module_tool.activity.DistanceActivity
import com.example.module_user.ui.activity.LoginActivity
import com.example.wifi_manager.R
import com.example.wifi_manager.ui.adapter.recycleview.MyBottomAdapter
import com.example.wifi_manager.ui.adapter.recycleview.MyTopAdapter
import com.example.wifi_manager.databinding.FragmentMyBinding
import com.example.wifi_manager.ui.activity.*
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
class MyFragment:BaseVmFragment<FragmentMyBinding,MyViewModel>() {
    companion object{
        const val REQUEST_CODE=1

    }

    override fun getViewModelClass(): Class<MyViewModel> {
        return MyViewModel::class.java
    }
    override fun getChildLayout(): Int= R.layout.fragment_my


    private val mMyTopAdapter  by lazy {
        MyTopAdapter()
    }
    private val mMyBottomAdapter  by lazy {
        MyBottomAdapter()
    }

    override fun initView() {
        binding.apply {
            mMyTopContainer.layoutManager=GridLayoutManager(activity,3)
            mMyTopAdapter.setList(DataProvider.myTopList)
            mMyTopContainer.adapter=mMyTopAdapter

            mMyBottomContainer.layoutManager=LinearLayoutManager(activity)
            mMyBottomAdapter.setList(DataProvider.myBottomList)
            mMyBottomContainer.adapter=mMyBottomAdapter

        }
    }

    override fun initEvent() {
        binding.apply {
            mMyTopAdapter.setOnItemClickListener { adapter, view, position ->
                when (position) {
                    0 -> startActivityForResult(Intent(activity,ScanActivity::class.java),REQUEST_CODE)
                    1-> toOtherActivity<WifiProtectViewActivity>(activity){}
                    2 -> if (RxNetTool.isWifiConnected(requireContext())) toOtherActivity<SpeedTestViewActivity>(activity) {} else showToast(ConstantsUtil.NO_CONNECT_WIFI)
                    3-> toOtherActivity<DistanceActivity>(activity){}
                    4-> toOtherActivity<HardwareTweaksActivity>(activity){}
                }
            }

            toLogin.setOnClickListener {
                toOtherActivity<LoginActivity>(activity){}
            }
        }



        mMyBottomAdapter.setOnItemClickListener { adapter, view, position ->
            when (position) {
                1-> toOtherActivity<CancelShareViewActivity>(activity){

                }
                2 -> FeedbackAPI.openFeedbackActivity()
                3 -> toOtherActivity<DealViewActivity>(activity) { putExtra(Constants.SET_DEAL1, 1) }
                4 -> toOtherActivity<DealViewActivity>(activity) { putExtra(Constants.SET_DEAL1, 2) }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_CODE&&resultCode==1) {
            toOtherActivity<ScanResultViewActivity>(activity){putExtra(ConstantsUtil.ZXING_RESULT_KEY,data?.getStringExtra(ConstantsUtil.ZXING_RESULT_KEY))}
        }
    }

}