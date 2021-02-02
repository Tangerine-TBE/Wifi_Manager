package com.example.wifi_manager.ui.activity


import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BasePopup
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.*
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityCheckDeviceBinding
import com.example.wifi_manager.ui.adapter.recycleview.DevicesAdapter
import com.example.wifi_manager.ui.popup.RenamePopup
import com.example.wifi_manager.utils.DataProvider
import com.example.wifi_manager.utils.ProgressState
import com.example.wifi_manager.utils.WifiContentState

import com.example.wifi_manager.viewmodel.CheckDeviceViewModel
import com.tamsiree.rxkit.view.RxToast

class CheckDeviceViewActivity : BaseVmViewActivity<ActivityCheckDeviceBinding,CheckDeviceViewModel>() {
    private var mSweeping=true
    override fun getLayoutView(): Int=R.layout.activity_check_device
    override fun getViewModelClass(): Class<CheckDeviceViewModel> {
        return CheckDeviceViewModel::class.java
    }
    private lateinit var mDevicesAdapter: DevicesAdapter
    private val mRenamePopup by lazy {
        RenamePopup(this)
    }


    override fun initView() {
        binding.data=viewModel
        setStatusBar(this,   binding.checkDeviceToolbar, LayoutType.CONSTRAINTLAYOUT)
        mDevicesAdapter= DevicesAdapter()
        binding.devicesContainer.apply {
            layoutManager=LinearLayoutManager(this@CheckDeviceViewActivity)
            adapter=mDevicesAdapter

        }
        mDevicesAdapter.addChildClickViewIds(R.id.deviceTab)
        viewModel.scanDevice()

    }

    override fun observerData() {
        viewModel.apply {
            scanDeviceState.observe(this@CheckDeviceViewActivity, Observer { result ->
                val deviceContent = result.deviceContent
                mSweeping = when (result.scanState) {
                    ProgressState.BEGIN -> {
                        mDevicesAdapter.setList(deviceContent)
                        true
                    }
                    ProgressState.END -> {
                        showToast("扫描完成,共发现${deviceContent.size}台设备")
                        false
                    }
                    else->{
                        mDevicesAdapter.setList(deviceContent)
                        false
                    }
                }
            })
        }
    }

    private var mSelectPosition=0
    override fun initEvent() {
        binding.apply {
            checkDeviceToolbar.toolbarEvent(this@CheckDeviceViewActivity) {}
            deviceRefresh.setOnClickListener {
                if (mSweeping) RxToast.normal("正在扫描...") else viewModel.scanDevice()
            }

            mDevicesAdapter.setOnItemChildClickListener { adapter, view, position ->
                mSelectPosition=position
                when(view.id){
                    R.id.deviceTab->{
                        if (mSweeping) RxToast.normal("正在扫描...")
                        else
                        {
                            checkAppPermission(DataProvider.askStoragePermissionLis,{
                                mRenamePopup?.apply {
                                    setOldName(viewModel.getSignName(position))
                                    showPopupView(devicesContainer)
                                }
                            },{
                                showToast("缺少权限，无法标记")
                            },this@CheckDeviceViewActivity)

                        }

                    }
                }

            }

            mRenamePopup?.apply {
                setOnActionClickListener(object : BasePopup.OnActionClickListener {
                    override fun sure() {
                        val renameText = getRenameText()
                        viewModel.saveSign(renameText,mSelectPosition)
                        dismiss()
                    }
                    override fun cancel() {

                    }
                })
            }

            protectNet.setOnClickListener {
                toOtherActivity<ProtectNetActivity>(this@CheckDeviceViewActivity){}
            }

        }
    }

    override fun release() {

    }

}