package com.example.module_base.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
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
abstract class BaseVmViewActivity<T:ViewDataBinding,Vm:ViewModel>:BaseActivity() {

    protected lateinit var viewModel:Vm
    protected lateinit var binding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, getLayoutView())
        setContentView(binding.root)
        binding.lifecycleOwner=this
        //创建ViewModel
        initViewModel()
        //initView
        initView()
        //观察ViewModel数据变化
        observerData()
        //事件监听
        initEvent()
    }

    open fun observerData() {
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(getViewModelClass())
    }
    abstract fun getViewModelClass(): Class<Vm>

    open fun initEvent() {
    }

    open fun initView() {
    }

    abstract fun getLayoutView(): Int


}