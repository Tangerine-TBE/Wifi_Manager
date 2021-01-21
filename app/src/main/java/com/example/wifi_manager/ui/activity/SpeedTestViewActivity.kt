package com.example.wifi_manager.ui.activity

import android.view.View
import androidx.lifecycle.Observer
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.LogUtils
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivitySpeedTestBinding
import com.example.wifi_manager.extensions.noFinishShow
import com.example.wifi_manager.utils.*
import com.example.wifi_manager.viewmodel.SpeedTestViewModel
import com.tamsiree.rxkit.RxNetTool
import com.tamsiree.rxui.view.dialog.RxDialogSureCancel
import java.text.DecimalFormat

class SpeedTestViewActivity : BaseVmViewActivity<ActivitySpeedTestBinding,SpeedTestViewModel>() {
    private val mTestRemindDialog by lazy {
        RxDialogSureCancel(this).apply {
            setContent("当前未连接WiFI，继续测速将消耗数据流量，是否继续进行网络测速")
            setCancelable(true)
        }
    }
    override fun getLayoutView(): Int=R.layout.activity_speed_test
    override fun getViewModelClass(): Class<SpeedTestViewModel> { return SpeedTestViewModel::class.java }
    override fun initView() {
        binding.apply {
            setToolBar(this@SpeedTestViewActivity, "网络测速", mSpeedToolbar)
            if (!RxNetTool.isWifiConnected(this@SpeedTestViewActivity)) mTestRemindDialog.noFinishShow(this@SpeedTestViewActivity) else viewModel.startPing()
            testWifiName.text= getConnectWifiName()+"-正在测速"
        }

    }


    //1610532064261  04   1610532084378    24
    private var currentTotalRxData=0L
    private var currentPing=0
    private var currentTime=0f
    override fun observerData() {
        viewModel.apply {
            binding.apply {
            totalRxBytes.observe(this@SpeedTestViewActivity, Observer {
                wifiSpeedTestView.startRotate(it.dataSize / it.continueTime.toFloat())
                currentTime=it.continueTime.toFloat()
                currentTotalRxData=it.dataSize
                testSpeedState.text="下载速度检查中"
                LogUtils.i("--------totalRxBytes----------- >${it.dataSize / it.continueTime.toFloat()}---------${it.continueTime}-----")
            })

            downState.observe(this@SpeedTestViewActivity, Observer {
                if (it) {
                    if (currentTotalRxData!=0L) {
                        val downLoadSpeed =  DecimalFormat("0.00").format(currentTotalRxData / currentTime)
                        LogUtils.i("--------downLoadSpeed---$downLoadSpeed-----")
                        toOtherActivity<SpeedTestResultViewActivity>(this@SpeedTestViewActivity,true){
                            putExtra(ConstantsUtil.WIFI_DELAY_KEY,currentPing.toString())
                            putExtra(ConstantsUtil.WIFI_DOWN_LOAD_KEY,downLoadSpeed)
                        }
                    }
                }
            })
            pingValue.observe(this@SpeedTestViewActivity, Observer {
                currentPing=it
                testSpeedState.text="网络延时检测中"
            })

            }
        }

    }

    override fun initEvent() {
        binding.apply {
            mSpeedToolbar.toolbarEvent(this@SpeedTestViewActivity){}
            mTestRemindDialog.setSureListener {
                mTestRemindDialog.dismiss()
                viewModel.startPing()
            }

            mTestRemindDialog.setCancelListener {
                finish()
            }

            mSpeedTest.setOnClickListener {
                viewModel.stopSaveFile()
                finish()
            }

        }

    }


}