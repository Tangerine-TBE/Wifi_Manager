package com.example.wifi_manager.ui.activity

import android.animation.ValueAnimator
import androidx.core.animation.doOnEnd
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_base.base.BaseApplication
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.setStatusBar
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivitySignalUpBinding
import com.example.wifi_manager.livedata.WifiStateLiveData

import com.example.wifi_manager.ui.adapter.recycleview.SignalAppInfoAdapter
import com.example.wifi_manager.ui.adapter.recycleview.SignalWifiCheckAdapter
import com.example.wifi_manager.utils.*
import com.example.wifi_manager.viewmodel.SignalUpViewModel
import kotlinx.android.synthetic.main.activity_signal_up.*
import kotlinx.coroutines.*


class SignalUpActivity : BaseVmViewActivity<ActivitySignalUpBinding, SignalUpViewModel>() {

    private val mSignalAppInfoAdapter by lazy {
        SignalAppInfoAdapter()
    }

    private val mSignalWifiCheckAdapter by lazy {
        SignalWifiCheckAdapter()
    }

    private val mSignalNetWorkAdapter by lazy {
        SignalWifiCheckAdapter()
    }


    override fun getLayoutView(): Int = R.layout.activity_signal_up
    override fun getViewModelClass(): Class<SignalUpViewModel> {
        return SignalUpViewModel::class.java
    }



    override fun initView() {
        LogUtils.i("---SignalUpActivity------OOO/${   WifiUtils.getConnectWifiSignalLevel()}-----------------")
        viewModel.getWifiSignalLevel()
        binding.data=viewModel
        binding.apply {
            lifecycle.addObserver(signalUpView)
            setStatusBar(this@SignalUpActivity, binding.mSignalUpToolbar, LayoutType.LINEARLAYOUT)
            signalSelectLayout.apply {
                signalOne.layoutManager = LinearLayoutManager(this@SignalUpActivity, RecyclerView.HORIZONTAL, false)
                signalOne.adapter = mSignalAppInfoAdapter

                signalTwo.layoutManager = LinearLayoutManager(this@SignalUpActivity)
                signalTwo.adapter = mSignalWifiCheckAdapter

                signalThree.layoutManager = LinearLayoutManager(this@SignalUpActivity)
                signalThree.adapter = mSignalNetWorkAdapter

                mSignalNetWorkAdapter.setList(DataProvider.signalNetList)
                mSignalWifiCheckAdapter.setList(DataProvider.signalWifiList)

            }

        }
    }


    private var currentLevel=0
    private var currentLevelUp=0

    override fun observerData() {
        binding.apply {
            viewModel.apply {
                appDataList.observe(this@SignalUpActivity, Observer {
                    mSignalAppInfoAdapter.setList(it)
                })

                animationState.observe(this@SignalUpActivity, Observer {state->
                    when(state){
                        ProgressState.BEGIN->
                        {
                            visibleView(nSignalUpTip,signalSelectLayout.root)
                            goneView(signalNormalLayout.root,nSignalUp)
                        }

                        ProgressState.END->{
                            visibleView(nSignalUp, signalNormalLayout.root)
                            signalSelectLayout.apply {
                                goneView(nSignalUpTip,root, signalTwo, signalOne)
                            }

                        }
                    }
                })

                optimizeCount.observe(this@SignalUpActivity, Observer {
                    val atLastLevel = currentLevel + currentLevelUp
                    signalUpView.startCurrentHint(   (if (atLastLevel < 99) atLastLevel else 99) *it/8)

                })

                wifiSignalLevel.observe(this@SignalUpActivity, Observer { level->
                    currentLevel=level
                    signalUpView.setCurrentHint(level)
                })

                wifiSignalUp.observe(this@SignalUpActivity, Observer {
                    currentLevelUp=it
                    nSignalUp.text="立即增强+${it}"
                })

            }
        }

        WifiStateLiveData.observe(this, Observer{
            when(it){
                WifiState.DISABLED->{
                    finish()
                    showToast("WiFi已关闭")
                }
            }
        })

    }


    override fun initEvent() {
        binding.apply {
            mSignalUpToolbar.toolbarEvent(this@SignalUpActivity) {}

            signalNormalLayout.mOptimizeHardwareBt.setOnClickListener {
                toOtherActivity<HardwareTweaksActivity>(this@SignalUpActivity) {}
            }

            nSignalUp.setOnClickListener {


                viewModel.setAnimationState(ProgressState.BEGIN)
                mSignalNetWorkAdapter.mList.clear()
                mSignalWifiCheckAdapter.mList.clear()


                visibleView(signalSelectLayout.root)

                    animation(3, {
                        mSignalNetWorkAdapter.setStepState(HardwareTweaksActivity.state[it])
                        viewModel.setOptimizeCount(it)
                    }) {
                        BaseApplication.mHandler.postDelayed({

                            visibleView( signalSelectLayout.signalTwo)

                            animation(4, {
                                mSignalWifiCheckAdapter.setStepState(HardwareTweaksActivity.state[it])
                                viewModel.setOptimizeCount(3+it)
                            }, {
                                BaseApplication.mHandler.postDelayed({
                                    viewModel.getAppInfo()

                                    visibleView( signalSelectLayout.signalOne)


                                },300)
                            })
                        },300)

                    }
                }
            }
        }

     private fun animation(step:Int,doing:(Int)->Unit,finish:()->Unit) {
        ValueAnimator.ofInt(0, step).apply {
            duration = 5000
            addUpdateListener {
                val animatedValue = it.animatedValue as Int
                doing(animatedValue)
            }
            doOnEnd {
                finish()
            }
        }.start()
    }


}