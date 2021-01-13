package com.example.module_base.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.module_base.R
import com.example.module_base.utils.LogUtils
import kotlinx.android.synthetic.main.layout_toolbar_new.view.*

/**
 * @name WeatherOne
 * @class name：com.nanjing.tqlhl.ui.custom.mj15day
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2020/10/20 11:43
 * @class describe
 */
class MyToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_toolbar_new, this, true)
        context.obtainStyledAttributes(attrs, R.styleable.MyToolbar).apply {
            mTitle = getString(R.styleable.MyToolbar_toolbarTitle) ?: "顶部栏"
            mRightTitle = getString(R.styleable.MyToolbar_rightTitle)
            mTitleColor = getColor(R.styleable.MyToolbar_titleColor, Color.WHITE)
            mBarBgColor = getColor(R.styleable.MyToolbar_barBgColor, Color.WHITE)
            mRightTitleColor = getColor(R.styleable.MyToolbar_rightTitleColor, Color.WHITE)
            mLeftIcon = getResourceId(R.styleable.MyToolbar_backIconStyle, R.drawable.icon_base_back)
            mRightIcon = getResourceId(R.styleable.MyToolbar_rightIconStyle, -1)
            isHaveAdd = getBoolean(R.styleable.MyToolbar_has_right_icon, false)
            isHaveRight = getBoolean(R.styleable.MyToolbar_hasRightTitle, false)
            isHaveBack = getBoolean(R.styleable.MyToolbar_has_right_icon, true)
            recycle()
        }
        initView()
        initEvent()
    }






    private var mTitle: String = ""
    private var mRightTitle: String? = null
    private var mTitleColor: Int = Color.BLACK
    private var mBarBgColor: Int = Color.WHITE
    private var mLeftIcon: Int?=null
    private var mRightIcon: Int? = null
    private var isHaveAdd: Boolean? = null
    private var isHaveBack: Boolean? = null
    private var isHaveRight: Boolean? = null
    private var mRightTitleColor: Int ?=null


    private fun initView() {
        mTitle?.let {
            tv_toolbarTitle?.text = it
        }

        tv_toolbarTitle?.setTextColor(mTitleColor)

        rl_bar?.setBackgroundColor(mBarBgColor)

        mLeftIcon?.let {
            if (it != -1) {
                iv_bar_back.setImageResource(it)
            }
        }

        mRightIcon?.let {
            if (it != -1) {
                iv_bar_add.setImageResource(it)
            }
        }



        if (isHaveAdd!!) {
            iv_bar_add.visibility = View.VISIBLE
        } else {
            iv_bar_add.visibility = View.GONE
        }

        if (isHaveBack!!) {
            iv_bar_back.visibility = View.VISIBLE
        } else {
            iv_bar_back.visibility = View.GONE
        }

        mRightTitle?.let {
            tv_bar_right?.text = it
        }
        mRightTitleColor?.let {
            tv_bar_right.setTextColor(it)
        }

        if (isHaveRight!!) {
            tv_bar_right.visibility = View.VISIBLE
        } else {
            tv_bar_right.visibility = View.GONE
        }


    }

    private fun initEvent() {
        iv_bar_back.setOnClickListener {
            mOnBackClickListener?.onBack()
        }

        iv_bar_add.setOnClickListener {
            mOnBackClickListener?.onRightTo()
        }

        tv_bar_right.setOnClickListener {
            mOnBackClickListener?.onRightTo()
        }

    }

    private var mOnBackClickListener: OnBackClickListener? = null
    fun setOnBackClickListener(listener: OnBackClickListener?) {
        this.mOnBackClickListener = listener
    }

    fun setTitle(title: String) {
        mTitle = title
        initView()
    }


    interface OnBackClickListener {
        fun onBack()

        fun onRightTo()
    }
}


