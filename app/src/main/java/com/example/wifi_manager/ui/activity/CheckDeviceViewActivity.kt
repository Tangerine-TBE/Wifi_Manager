package com.example.wifi_manager.ui.activity


import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.setStatusBar
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityCheckDeviceBinding
import com.example.wifi_manager.ui.adapter.recycleview.DevicesAdapter
import com.example.wifi_manager.utils.ScanDeviceState
import com.example.wifi_manager.utils.toolbarEvent
import com.example.wifi_manager.viewmodel.CheckDeviceViewModel
import com.tamsiree.rxkit.RxTool
import com.tamsiree.rxkit.view.RxToast

class CheckDeviceViewActivity : BaseVmViewActivity<ActivityCheckDeviceBinding,CheckDeviceViewModel>() {
    private var mSweeping=true
    override fun getLayoutView(): Int=R.layout.activity_check_device
    override fun getViewModelClass(): Class<CheckDeviceViewModel> {
        return CheckDeviceViewModel::class.java
    }
    private lateinit var mDevicesAdapter: DevicesAdapter

    override fun initView() {
        binding.data=viewModel
        setStatusBar(this,   binding.checkDeviceToolbar, LayoutType.CONSTRAINTLAYOUT)
        mDevicesAdapter= DevicesAdapter()
        binding.devicesContainer.apply {
            layoutManager=LinearLayoutManager(this@CheckDeviceViewActivity)
            adapter=mDevicesAdapter

        }
        viewModel.scanDevice()

    }

    override fun observerData() {
        viewModel.apply {
            scanDeviceState.observe(this@CheckDeviceViewActivity, Observer { result ->
                val deviceContent = result.deviceContent
                mSweeping = when (result.scanState) {
                    ScanDeviceState.BEGIN -> {
                        mDevicesAdapter.setList(deviceContent)
                        true
                    }
                    ScanDeviceState.END -> {
                        RxToast.success("扫描完成,共发现${deviceContent.size}台设备")
                        false
                    }
                }
            })

        }
    }


    override fun initEvent() {
        binding.apply {
            checkDeviceToolbar.toolbarEvent(this@CheckDeviceViewActivity) {}
            deviceRefresh.setOnClickListener {
                if (mSweeping) RxToast.normal("正在扫描...") else viewModel.scanDevice()
            }

        }
    }

    override fun release() {

    }

}