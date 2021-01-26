package com.example.wifi_manager.ui.activity

import android.content.SharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.*
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivitySafetyBinding
import com.example.wifi_manager.domain.SpSignalBean
import com.example.wifi_manager.livedata.ScoreLiveData
import com.example.wifi_manager.livedata.WifiStateLiveData
import com.example.wifi_manager.ui.adapter.recycleview.SafetyItemAdapter
import com.example.wifi_manager.ui.popup.SafetyCheckPopup
import com.example.wifi_manager.utils.*
import com.example.wifi_manager.utils.ConstantsUtil.DIS_WIFI
import com.example.wifi_manager.viewmodel.SafetyCheckViewModel
import java.text.DecimalFormat
import java.util.*

class SafetyCheckActivity : BaseVmViewActivity<ActivitySafetyBinding, SafetyCheckViewModel>(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var speed = ""
    private var currentTotalRxData = 0L
    private var currentTime = 0f
    private var isOpenProtect = false
    private var scoreHint = ""
    private var isPreferenceChanged = false
    private var checkFinish = false

    private val mCheckItemPopup by lazy {
        SafetyCheckPopup(this)
    }

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
            currentText.text = "正在检测中..."
        }
        loadLiveData()
    }


    private fun loadLiveData() {
        mSafetyItemAdapter.cleanList()
        viewModel.scanDevice()
        viewModel.getWifiSignalLevel()
    }

    override fun observerData() {
        binding.apply {
            viewModel.apply {
                ScoreLiveData.observe(this@SafetyCheckActivity, {
                    score.text = "${it.score}分"
                    scoreHint = it.optimize
                    if (isPreferenceChanged) {
                        binding.currentText.text = "经过6项检测无异常, 网速为${speed}$scoreHint"
                    }
                })


                WifiStateLiveData.observe(this@SafetyCheckActivity, {
                    when (it) {
                        WifiState.DISABLED -> {
                            finish()
                            showToast(DIS_WIFI)
                        }
                    }
                })

                currentWifiLevel.observe(this@SafetyCheckActivity, {
                    sp.apply {
                        val upState = getBoolean(ConstantsUtil.SP_SIGNAL_SATE)
                        if (upState) {
                            gsonHelper<SpSignalBean>(getString(ConstantsUtil.SP_SIGNAL_INFO))?.let {
                                DataProvider.safetyList[0].title = "${it.newLevel}%"
                                DataProvider.safetyList[0].actionHint = "增强${it.upLevel}%"
                                DataProvider.safetyList[0].state = true
                            }

                        } else {
                            DataProvider.safetyList[0].title = "未增强，当前$it%"
                            DataProvider.safetyList[0].actionHint = "增强信号"
                            DataProvider.safetyList[0].state = false
                        }

                        ScoreLiveData.getScore()
                    }



                    mSafetyItemAdapter.setList(DataProvider.safetyList)
                    mSafetyItemAdapter.setStepState(StepState.ONE)
                    protectCheckView.setProgressState(StepState.ONE)

                })
                scanDeviceState.observe(this@SafetyCheckActivity, {
                    when (it.scanState) {
                        ProgressState.END -> {
                            DataProvider.safetyList[1].title = "${it.deviceContent.size}台设备连接"
                            mSafetyItemAdapter.setList(DataProvider.safetyList)
                            mSafetyItemAdapter.setStepState(StepState.TWO)
                            binding.protectCheckView.setProgressState(StepState.TWO)
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
                    protectCheckView.setProgressState(StepState.THREE)


                    ScoreLiveData.getScore()

                }

                totalRxBytes.observe(this@SafetyCheckActivity, {
                    currentTime = it.continueTime.toFloat()
                    currentTotalRxData = it.dataSize
                })

                downState.observe(this@SafetyCheckActivity, {
                    if (it) {
                        if (currentTotalRxData != 0L) {
                            speed = DecimalFormat("0.00").format(currentTotalRxData / currentTime) + "MB/s"
                            DataProvider.safetyList[3].title = speed


                            val open = sp.getBoolean(ConstantsUtil.SP_WIFI_PROTECT_STATE)
                            currentText.text = if (open) "开放网络注意安全, 网速为${speed}$scoreHint" else "经过6项检测无异常, 网速为${speed}$scoreHint"
                            DataProvider.safetyList[4].title = if (open) "存在风险项" else "经过6项检测无异常"
                            DataProvider.safetyList[4].state = open

                            DataProvider.checkList[3].hint = if (open) "异常" else "正常"
                            DataProvider.checkList[3].state = open



                            mSafetyItemAdapter.setList(DataProvider.safetyList)
                            mSafetyItemAdapter.setStepState(StepState.FIVE)
                            protectCheckView.setProgressState(StepState.FIVE)

                            checkFinish = true

                        }
                    }
                })


            }
        }
    }


    override fun initEvent() {
        sp.prefs.registerOnSharedPreferenceChangeListener(this)

        binding.apply {
            safetyCheckToolbar.toolbarEvent(this@SafetyCheckActivity) {}

            mSafetyItemAdapter.setOnItemClickListener { adapter, view, position ->
                if (checkFinish) {
                    when (position) {
                        0 -> toOtherActivity<SignalUpActivity>(this@SafetyCheckActivity) {
                            putExtra(ConstantsUtil.SIGNAL_SATE, true)
                        }
                        1 -> toOtherActivity<CheckDeviceViewActivity>(this@SafetyCheckActivity) {}
                        2 -> {
                            if (isOpenProtect) {
                                toOtherActivity<WifiProtectInfoViewActivity>(this@SafetyCheckActivity) {}
                            } else {
                                toOtherActivity<WifiProtectViewActivity>(this@SafetyCheckActivity) {}
                            }
                        }
                        3 -> toOtherActivity<SpeedTestViewActivity>(this@SafetyCheckActivity) {}
                        4 -> {
                            mCheckItemPopup.setCheckData(DataProvider.checkList)
                            mCheckItemPopup.showPopupView(safetyContainer,)
                        }

                    }
                } else {
                    showToast("正在检测中...")
                }
            }

        }
    }


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        showLog(key)
        sp.apply {
            isOpenProtect = getBoolean(ConstantsUtil.SP_WIFI_PROTECT_OPEN)
            val endTime = getLong(ConstantsUtil.SP_WIFI_PROTECT_TIME, 0L)
            DataProvider.safetyList[2].title = if (isOpenProtect) {
                "${WifiUtils.getConnectWifiName()}已保护${
                    calLastedTime(
                            Date(endTime),
                            Date(System.currentTimeMillis())
                    )
                }天"
            } else {

                "未开启"
            }

            val upState = getBoolean(ConstantsUtil.SP_SIGNAL_SATE)
            if (upState) {
                gsonHelper<SpSignalBean>(getString(ConstantsUtil.SP_SIGNAL_INFO))?.let {
                    DataProvider.safetyList[0].title = "${it.newLevel}%"
                    DataProvider.safetyList[0].actionHint = "增强${it.upLevel}%"
                    DataProvider.safetyList[0].state = true
                }

            }
            isPreferenceChanged = true
            ScoreLiveData.getScore()


            mSafetyItemAdapter.setList(DataProvider.safetyList)

        }

    }

}