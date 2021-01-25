package com.example.wifi_manager.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.setToolBar
import com.example.module_base.utils.toolbarEvent
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivitySafetyBinding
import com.example.wifi_manager.ui.adapter.recycleview.SafetyItemAdapter
import com.example.wifi_manager.utils.DataProvider
import com.example.wifi_manager.utils.ProgressState
import com.example.wifi_manager.viewmodel.SafetyCheckViewModel

class SafetyCheckActivity : BaseVmViewActivity<ActivitySafetyBinding,SafetyCheckViewModel>() {

    private val mSafetyItemAdapter by lazy {
        SafetyItemAdapter()
    }

    override fun getViewModelClass(): Class<SafetyCheckViewModel> {
        return SafetyCheckViewModel::class.java
    }
    override fun getLayoutView(): Int=R.layout.activity_safety

    override fun initView() {
        binding.apply {
            setToolBar(this@SafetyCheckActivity,"安全检测",safetyCheckToolbar)
            protectCheckView.startProtectCheck()
            safetyContainer.layoutManager = LinearLayoutManager(this@SafetyCheckActivity)
            safetyContainer.adapter=mSafetyItemAdapter
            mSafetyItemAdapter.setList(DataProvider.safetyList)
        }

        loadLiveData()
    }


    private fun loadLiveData(){
        viewModel.getWifiSignalLevel()
        viewModel.scanDevice()
    }

    override fun observerData() {
        viewModel.apply {

            currentWifiLevel.observe(this@SafetyCheckActivity,{
                DataProvider.safetyList[0].title="未增强，当前$it%"
                mSafetyItemAdapter.setList(DataProvider.safetyList)
            })

            scanDeviceState.observe(this@SafetyCheckActivity, {
                when (it.scanState) {
                    ProgressState.END -> {
                        DataProvider.safetyList[1].title = "${it.deviceContent.size}台设备连接"
                        mSafetyItemAdapter.setList(DataProvider.safetyList)
                    }
                }
            })

        }
    }


    override fun initEvent() {
        binding.apply {
            safetyCheckToolbar.toolbarEvent(this@SafetyCheckActivity){}

        }
    }


}