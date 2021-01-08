package com.example.module_base.base

import android.graphics.Color
import android.os.Bundle
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
abstract class BaseActivity:FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreenWindow()
        setContentView(getLayoutView())
        initView()
        initEvent()
    }

    open fun initEvent() {
    }

    open fun initView() {
    }
   open fun setFullScreenWindow(){
        MyStatusBarUtil.setColor(this,Color.TRANSPARENT)
        MyStatusBarUtil.setFullWindow(this)
    }

    abstract fun getLayoutView(): Int

    open fun release() {

    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }


}