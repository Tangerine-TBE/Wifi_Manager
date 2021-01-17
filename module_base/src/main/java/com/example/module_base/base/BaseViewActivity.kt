package com.example.module_base.base

import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.example.module_base.utils.MyStatusBarUtil

/**
 * @name Wifi_Manager
 * @class name：com.example.module_base.base
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 10:58:27
 * @class describe
 */
abstract class BaseViewActivity<T:ViewDataBinding>:BaseActivity() {

    protected lateinit var binding:T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView<T>(this,getLayoutView())
        setContentView(binding.root)
        initView()
        initEvent()
    }

    open fun initEvent() {
    }

    open fun initView() {
    }


    abstract fun getLayoutView(): Int


}