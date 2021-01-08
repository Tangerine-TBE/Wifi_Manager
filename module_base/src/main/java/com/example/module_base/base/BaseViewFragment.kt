package com.example.module_base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * @name Wifi_Manager
 * @class name：com.example.module_base.base
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 10:58:27
 * @class describe
 */
abstract class BaseViewFragment<T:ViewDataBinding>:Fragment() {
    protected lateinit var mBinding: T
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getChildLayout(), container, false)
        return mBinding.root
    }
    abstract fun getChildLayout(): Int


    override fun onDestroyView() {
        super.onDestroyView()
        release()
    }

    open fun release() {

    }

}