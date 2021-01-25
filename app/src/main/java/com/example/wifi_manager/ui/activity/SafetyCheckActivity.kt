package com.example.wifi_manager.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.setToolBar
import com.example.module_base.utils.toOtherActivity
import com.example.module_base.utils.toolbarEvent
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivitySafetyBinding
import com.example.wifi_manager.ui.adapter.recycleview.SafetyItemAdapter
import com.example.wifi_manager.utils.DataProvider
import com.example.wifi_manager.utils.ProgressState
import com.example.wifi_manager.utils.StepState
import com.example.wifi_manager.viewmodel.SafetyCheckViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class SafetyCheckActivity : BaseVmViewActivity<ActivitySafetyBinding, SafetyCheckViewModel>() {

    private val mSafetyItemAdapter by lazy {
        SafetyItemAdapter()
    }

    override fun getViewModelClass(): Class<SafetyCheckViewModel> {
        return SafetyCheckViewModel::class.java
    }

    override fun getLayoutView(): Int = R.layout.activity_safety

    override fun initView() {
        binding.apply {
            setToolBar(this@SafetyCheckActivity, "安全检测", safetyCheckToolbar)

            safetyContainer.layoutManager = LinearLayoutManager(this@SafetyCheckActivity)
            safetyContainer.adapter = mSafetyItemAdapter
            mSafetyItemAdapter.setList(DataProvider.safetyList)

            score.text="70分"
            currentText.text="正在检测中..."

        }
        loadLiveData()
    }

    override fun onResume() {
        super.onResume()
      //  viewModel.getProtectInfo()
    }

    private fun loadLiveData() {
        mSafetyItemAdapter.cleanList()
        viewModel.scanDevice()
        viewModel.getWifiSignalLevel()


    }

    private var currentTotalRxData = 0L
    private var currentTime = 0f
    private var isOpenProtect = false
    override fun observerData() {
        viewModel.apply {

            currentWifiLevel.observe(this@SafetyCheckActivity, {
                DataProvider.safetyList[0].title = "未增强，当前$it%"
                mSafetyItemAdapter.setList(DataProvider.safetyList)
                mSafetyItemAdapter.setStepState(StepState.ONE)
               binding. protectCheckView.setProgressState(StepState.ONE)
            })
            scanDeviceState.observe(this@SafetyCheckActivity, {
                when (it.scanState) {
                    ProgressState.END -> {
                        DataProvider.safetyList[1].title = "${it.deviceContent.size}台设备连接"
                        mSafetyItemAdapter.setList(DataProvider.safetyList)
                        mSafetyItemAdapter.setStepState(StepState.TWO)
                        binding. protectCheckView.setProgressState(StepState.TWO)
                        viewModel.startSpeedTest()
                        viewModel.getProtectInfo()


                    }
                }
            })

            protectState.observe(this@SafetyCheckActivity) {
                DataProvider.safetyList[2].title = it.info
                isOpenProtect = it.open

                mSafetyItemAdapter.setList(DataProvider.safetyList)
                mSafetyItemAdapter.setStepState(StepState.THREE)
                binding.protectCheckView.setProgressState(StepState.THREE)
            }

            totalRxBytes.observe(this@SafetyCheckActivity, {
                currentTime = it.continueTime.toFloat()
                currentTotalRxData = it.dataSize
            })

            downState.observe(this@SafetyCheckActivity, {
                if (it) {
                    if (currentTotalRxData != 0L) {
                        DataProvider.safetyList[3].title = DecimalFormat("0.00").format(currentTotalRxData / currentTime) + "MB/s"
                        DataProvider.safetyList[4].title = "经过6项检测无异常"
                        mSafetyItemAdapter.setList(DataProvider.safetyList)
                        mSafetyItemAdapter.setStepState(StepState.FIVE)
                        binding. protectCheckView.setProgressState(StepState.FIVE)

                    }
                }
            })
        }
    }


    override fun initEvent() {
        binding.apply {
            safetyCheckToolbar.toolbarEvent(this@SafetyCheckActivity) {}

            mSafetyItemAdapter.setOnItemClickListener { adapter, view, position ->
                when (position) {
                    0 -> toOtherActivity<SignalUpActivity>(this@SafetyCheckActivity) {}
                    1 -> toOtherActivity<CheckDeviceViewActivity>(this@SafetyCheckActivity) {}
                    2 -> {
                        if (isOpenProtect) {
                            toOtherActivity<WifiProtectInfoViewActivity>(this@SafetyCheckActivity) {}
                        } else {
                            toOtherActivity<WifiProtectViewActivity>(this@SafetyCheckActivity) {}
                        }
                    }
                    3 -> toOtherActivity<SpeedTestViewActivity>(this@SafetyCheckActivity) {}
                    4 -> toOtherActivity<SignalUpActivity>(this@SafetyCheckActivity) {}

                }

            }

        }
    }


}