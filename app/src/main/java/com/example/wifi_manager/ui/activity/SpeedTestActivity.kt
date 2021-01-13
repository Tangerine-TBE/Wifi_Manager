package com.example.wifi_manager.ui.activity

import androidx.lifecycle.Observer
import com.example.module_base.base.BaseVmActivity
import com.example.module_base.utils.LogUtils
import com.example.module_base.widget.MyToolbar
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivitySpeedTestBinding
import com.example.wifi_manager.utils.setToolBar
import com.example.wifi_manager.viewmodel.SpeedTestViewModel
import kotlinx.android.synthetic.main.activity_speed_test.*

class SpeedTestActivity : BaseVmActivity<ActivitySpeedTestBinding,SpeedTestViewModel>() {
    override fun getLayoutView(): Int=R.layout.activity_speed_test
    override fun getViewModelClass(): Class<SpeedTestViewModel> { return SpeedTestViewModel::class.java }
    override fun initView() {
        setToolBar(this,"网络测速",mSpeedToolbar)
        viewModel.startSpeedTest()
    }


    //1610532064261  04   1610532084378    24
    override fun observerData() {
        viewModel.totalRxBytes.observe(this, Observer {

         //   LogUtils.i("--------totalRxBytes----------- >${it.dataSize}--------------${it.continueTime}")
        })
    }

    override fun initEvent() {
        mSpeedToolbar.setOnBackClickListener(object:MyToolbar.OnBackClickListener{
            override fun onBack() {
                finish()
            }
            override fun onRightTo() {
            }
        })
    }



}