package com.example.wifi_manager.ui.activity

import android.view.View
import androidx.lifecycle.Observer
import com.example.module_base.base.BaseVmActivity
import com.example.module_base.utils.LogUtils
import com.example.module_base.widget.MyToolbar
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivitySpeedTestBinding
import com.example.wifi_manager.utils.setToolBar
import com.example.wifi_manager.viewmodel.SpeedTestViewModel
import com.tamsiree.rxkit.RxNetTool
import com.tamsiree.rxui.view.dialog.RxDialogSureCancel
import kotlinx.android.synthetic.main.activity_speed_test.*
import kotlinx.android.synthetic.main.activity_wifi_info.*

class SpeedTestActivity : BaseVmActivity<ActivitySpeedTestBinding, SpeedTestViewModel>() {

    private val mTestRemindDialog by lazy {
        RxDialogSureCancel(this).apply {
            setContent("当前未连接WiFI，继续测速将消耗数据流量，是否继续进行网络测速")
            setCancelable(true)
        }
    }

    override fun getLayoutView(): Int = R.layout.activity_speed_test
    override fun getViewModelClass(): Class<SpeedTestViewModel> {
        return SpeedTestViewModel::class.java
    }

    override fun initView() {
        setToolBar(this, "网络测速", mSpeedToolbar)
        if (!RxNetTool.isWifiConnected(this)) {
            if (!isFinishing) {
                mTestRemindDialog.show()
            }
        } else {
            viewModel.startSpeedTest()
        }
    }


    //1610532064261  04   1610532084378    24
    override fun observerData() {
        viewModel.totalRxBytes.observe(this, Observer {
              LogUtils.i("--------totalRxBytes----------- >${it.dataSize}--------------${it.continueTime}")
        })
    }

    override fun initEvent() {
        mSpeedToolbar.setOnBackClickListener(object : MyToolbar.OnBackClickListener {
            override fun onBack() {
                finish()
            }

            override fun onRightTo() {
            }
        })


        mTestRemindDialog.setSureListener(View.OnClickListener {
            viewModel.startSpeedTest()
        })

        mTestRemindDialog.setCancelListener(View.OnClickListener {
            mTestRemindDialog.dismiss()
            finish()
        })

        mSpeedTest.setOnClickListener {
            viewModel.stopSaveFile()
        }
    }


}