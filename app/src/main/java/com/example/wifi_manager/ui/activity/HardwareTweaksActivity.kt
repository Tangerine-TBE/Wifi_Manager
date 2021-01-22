package com.example.wifi_manager.ui.activity

import android.animation.Animator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.setStatusBar
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityHardwareTweaksBinding
import com.example.wifi_manager.ui.adapter.recycleview.HardwareAdapter
import com.example.wifi_manager.ui.widget.HardwareProgressView
import com.example.wifi_manager.utils.*
import java.util.*

class HardwareTweaksActivity : BaseViewActivity<ActivityHardwareTweaksBinding>() {
    companion object{
        val state= arrayListOf(
                StepState.NONE,
                StepState.ONE,
                StepState.TWO,
                StepState.THREE,
                StepState.FOUR,
                StepState.FIVE
        )
    }

    private val time= arrayListOf(5000L,10000L,15000L)

    private val mHardwareOptimizeAdapter by lazy {
        HardwareAdapter()
    }

    override fun getLayoutView(): Int = R.layout.activity_hardware_tweaks
    override fun initView() {
        setStatusBar(this, binding.hardwareToolbar, LayoutType.CONSTRAINTLAYOUT)
        binding.apply {
            optimizeContainer.layoutManager = LinearLayoutManager(this@HardwareTweaksActivity)
            mHardwareOptimizeAdapter.setList(DataProvider.hardwareList)
            optimizeContainer.adapter=mHardwareOptimizeAdapter
        }

        animation()

    }

    override fun initEvent() {
        binding.apply {
            hardwareToolbar.toolbarEvent(this@HardwareTweaksActivity) {}
            hardwareProgressView.setOnFinishStepClickListener(object :HardwareProgressView.onFinishStepClickListener{
                override fun finish(step: StepState) {
                    mHardwareOptimizeAdapter.setStepState(step)
                }

            })

        }


    }

    override fun release() {

    }

    private fun animation(){
      ValueAnimator.ofInt(0,5).apply {
          duration=time[Random().nextInt(time.size)]
          LogUtils.i("-----animation--${time[Random().nextInt(time.size)]}-------------------")
          addUpdateListener {
            val animatedValue = it.animatedValue as Int

              binding.hardwareProgressView.setProgressState(state[animatedValue])
          }
      }.start()
    }


}