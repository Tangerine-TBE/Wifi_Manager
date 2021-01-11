package com.example.wifi_manager.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI
import com.example.module_base.activity.DealActivity
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.Constants
import com.example.module_base.utils.LogUtils
import com.example.module_tool.activity.DistanceActivity
import com.example.wifi_manager.R
import com.example.wifi_manager.activity.ScanActivity
import com.example.wifi_manager.activity.ScanResultActivity
import com.example.wifi_manager.adapter.MyBottomAdapter
import com.example.wifi_manager.adapter.MyTopAdapter
import com.example.wifi_manager.databinding.FragmentMyBinding
import com.example.wifi_manager.utils.ConstantsUtil
import com.example.wifi_manager.utils.DataProvider
import com.example.wifi_manager.utils.toOtherActivity
import com.example.wifi_manager.utils.toOtherResultActivity
import com.example.wifi_manager.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.fragment_my.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.fragment
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
        mMyTopContainer.layoutManager=GridLayoutManager(activity,3)
        mMyTopAdapter.setList(DataProvider.myTopList)
        mMyTopContainer.adapter=mMyTopAdapter


        mMyBottomContainer.layoutManager=LinearLayoutManager(activity)
        mMyBottomAdapter.setList(DataProvider.myBottomList)
        mMyBottomContainer.adapter=mMyBottomAdapter


    }

    override fun initEvent() {
        mMyTopAdapter.setOnItemClickListener { adapter, view, position ->
            when (position) {
                0 -> startActivityForResult(Intent(activity,ScanActivity::class.java),REQUEST_CODE)
                3-> toOtherActivity<DistanceActivity>(activity){}
            }
        }



        mMyBottomAdapter.setOnItemClickListener { adapter, view, position ->
            when (position) {
                2 -> FeedbackAPI.openFeedbackActivity()
                3 -> toOtherActivity<DealActivity>(context) { putExtra(Constants.SET_DEAL1, 1) }
                4 -> toOtherActivity<DealActivity>(context) { putExtra(Constants.SET_DEAL1, 2) }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_CODE&&resultCode==1) {
            toOtherActivity<ScanResultActivity>(activity){putExtra(ConstantsUtil.ZXING_RESULT,data?.getStringExtra(ConstantsUtil.ZXING_RESULT))}
        }
    }

}