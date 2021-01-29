package com.example.module_base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.module_base.utils.SPUtil
import com.example.module_base.widget.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job


/**
 * @name Wifi_Manager
 * @class name：com.example.module_base.base
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 10:58:27
 * @class describe
 */
abstract class BaseViewFragment<T : ViewDataBinding>:Fragment() {
    private val job by lazy {
        Job()
    }
    protected val mScope by lazy {
        CoroutineScope(job)
    }

    protected val sp: SPUtil by lazy{
        SPUtil.getInstance()
    }

    protected val mLoadingDialog by lazy {
        LoadingDialog(requireContext())
    }


    protected lateinit var binding: T
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getChildLayout(), container, false)
        binding.lifecycleOwner=this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

    open fun initEvent() {

    }

    open fun initView() {

    }


    abstract fun getChildLayout(): Int





    override fun onDestroyView() {
        super.onDestroyView()
        release()
    }

    open fun release() {

    }


}