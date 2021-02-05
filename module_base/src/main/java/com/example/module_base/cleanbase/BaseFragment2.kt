package com.example.module_base.cleanbase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.module_base.widget.LoadingDialog


abstract class BaseFragment2 : Fragment(){
    protected var loadingDialog: LoadingDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(),container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    protected fun showLoadingDialog():LoadingDialog?{
        if (!isAdded){
            return null
        }
        loadingDialog = LoadingDialog(context?:return null)
        loadingDialog?.show()
        return loadingDialog
    }

    protected fun dismissLoadingDialog(){
        if (!isAdded){
            return
        }
        if (loadingDialog?.isShowing==true)
            loadingDialog?.dismiss()
        loadingDialog=null
    }

    abstract fun getLayoutId():Int

    abstract fun initView()

    abstract fun initListener()


    open fun release(){

    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }
}