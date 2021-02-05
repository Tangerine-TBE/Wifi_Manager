package com.example.module_base.base

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.module_base.utils.MyActivityManager
import com.example.module_base.utils.MyStatusBarUtil
import com.example.module_base.utils.SPUtil
import com.example.module_base.widget.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * @author: 铭少
 * @date: 2021/1/16 0016
 * @description：
 */
open class BaseActivity:FragmentActivity() {

    protected val mJob=Job()
    protected val mScope by lazy {
        CoroutineScope(mJob)
    }
    protected val sp: SPUtil by lazy{
        SPUtil.getInstance()
    }

    protected val mLoadingDialog by lazy{
        LoadingDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreenWindow()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        MyActivityManager.addActivity(this)

    }

    open fun setFullScreenWindow(){
        MyStatusBarUtil.setColor(this, Color.TRANSPARENT)
        MyStatusBarUtil.setFullWindow(this)
    }


    fun visibleView(vararg view:View){
        view.forEach {
            it.visibility=View.VISIBLE
        }
    }

    fun goneView(vararg view:View){
        view.forEach {
            it.visibility=View.GONE
        }
    }


    open fun release() {

    }

    override fun onDestroy() {
        super.onDestroy()
        MyActivityManager.removeActivity(this)
        release()
    }
}