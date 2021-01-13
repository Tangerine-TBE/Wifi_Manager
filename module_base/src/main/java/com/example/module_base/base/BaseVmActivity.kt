package com.example.module_base.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @name Wifi_Manager
 * @class name：com.example.module_base.base
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 10:58:50
 * @class describe
 */
abstract class BaseVmActivity<T:ViewDataBinding,Vm:ViewModel>:FragmentActivity() {

    protected lateinit var viewModel:Vm
    protected lateinit var binding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, getLayoutView())
        setContentView(binding.root)
        //创建ViewModel
        initViewModel()
        //观察ViewModel数据变化
        observerData()
        //initView
        initView()
        //事件监听
        initEvent()
    }

    open fun initView() {

    }

    abstract fun getLayoutView(): Int


    open fun initEvent() {
    }

    open fun observerData() {
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(getViewModelClass())
    }
    abstract fun getViewModelClass(): Class<Vm>


    open fun release() {

    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }
}