package com.example.module_base.base

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * @name Wifi_Manager
 * @class name：com.example.module_base.base
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 10:58:50
 * @class describe
 */
abstract class BaseVmFragment<T:ViewDataBinding,Vm:ViewModel>:BaseViewFragment<T>() {

    protected lateinit var viewModel:Vm
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //创建ViewModel
        initViewModel()
        //观察ViewModel数据变化
        observerData()
        super.onViewCreated(view, savedInstanceState)
    }

   protected fun showView(vararg view: View){
        view.forEach {
            it.visibility=View.VISIBLE
        }
    }

    protected fun goneView(vararg view: View){
        view.forEach {
            it.visibility=View.GONE
        }
    }



    open fun observerData() {
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(getViewModelClass())
    }
    abstract fun getViewModelClass(): Class<Vm>



}