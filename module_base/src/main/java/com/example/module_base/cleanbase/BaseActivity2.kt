package com.example.module_base.cleanbase

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.example.module_base.R
import com.example.module_base.utils.LogUtils
import com.example.module_base.widget.LoadingDialog

import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.base_activity.*

abstract class BaseActivity2 :FragmentActivity(){
    protected lateinit var immersionBar: ImmersionBar
    protected val loadingDialog by lazy { LoadingDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isActionBar()) {
            setContentView(R.layout.base_activity)
            contentView.also {
                it.removeAllViews()
                LayoutInflater.from(this).inflate(getLayoutId(),it,true)
            }

        }else{
            setContentView(getLayoutId())
        }
        getStatusBarColor().also {
            baseBar?.setBackgroundColor(ContextCompat.getColor(this,it))
            initImmersionBar(it)
        }
        leftIcon?.setOnClickListener { finish() }
        initView()
        initListener()
        LogUtils.i("--------BaseActivity2-----------------${javaClass.simpleName}---")
    }

    protected open fun getStatusBarColor():Int{
        return R.color.themeColor
    }

    abstract fun getLayoutId():Int

    abstract fun initView()

    abstract fun initListener()

    protected open fun isActionBar():Boolean{
        return true
    }





    protected fun setContentText(resId:Int){
        contentText?.setText(resId)
    }

    protected fun setContentText(text:String){
        contentText?.text=(text)
    }



    /**
     * 沉浸栏颜色
     */
    private fun initImmersionBar(color: Int) {
        immersionBar = ImmersionBar.with(this)
                .statusBarDarkFont(isStatusBarDarkFont())
        immersionBar.statusBarColor(color)
        immersionBar.init()
    }

    protected open fun isStatusBarDarkFont():Boolean{
        return false
    }

    protected fun dismissLoadingDialog(){
        if (loadingDialog.isShowing&&!isFinishing)
            loadingDialog.dismiss()
    }
}