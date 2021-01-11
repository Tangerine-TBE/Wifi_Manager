package com.feisukj.main.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color.blue
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.example.module_tool.R
import com.example.module_tool.utils.DeviceUtils
import kotlinx.android.synthetic.main.layout_refund_years.*

/**
 * Created by Gpp on 2018/6/12.
 */
class RefundYearsDialog(context: Context,
                        list: ArrayList<Any>,
                        currentPosition: String,
                        title: String,
                        textSize: Float,
                        listener: DialogItemSelectedListener) : Dialog(context, R.style.BottomDialog) {

    var selected: Int = list.indexOf(currentPosition)

    init {
        //-----初始化dialog--start--
        val contentView = LayoutInflater.from(context).inflate(R.layout.layout_refund_years, null)
        setContentView(contentView)
        val params = contentView.layoutParams as ViewGroup.MarginLayoutParams
//        params.width = context.resources.displayMetrics.widthPixels - DensityUtil.dp2px(16f)
//        params.bottomMargin = DensityUtil.dp2px(20f)
        params.width = context.resources.displayMetrics.widthPixels
        params.bottomMargin = DeviceUtils.dp2px(context,0f)
        contentView.layoutParams = params
        val window = window
        window?.setGravity(Gravity.BOTTOM)
        setCanceledOnTouchOutside(true)
        getWindow()!!.setWindowAnimations(R.style.BottomDialog_Animation)
        //-----初始化dialog--end--
        dialogTitle.text = title
        yearWheelView.setCyclic(false)
        yearWheelView.setTextSize(textSize)

        yearWheelView.currentItem = list.indexOf(currentPosition)
        Log.e("ss","currentPosition==$currentPosition currentItem=${list.indexOf(currentPosition)}")
        yearWheelView.setGravity(Gravity.CENTER)
        yearWheelView.adapter = ArrayWheelAdapter(list)
        yearWheelView.setTextColorCenter(ContextCompat.getColor(context,R.color.them_cjy))

        yearWheelView.setOnItemSelectedListener {
            Log.e("ss","yearWheelView position==$it,OnItemSelected==${list[it]}")
            selected = it
        }
        //-----初始化view--start--
        yearsCancel.setOnClickListener { dismiss() }
        yearsConfirm.setOnClickListener {
            listener.onItemSelected(selected)
            dismiss()
        }
        //-----初始化view--end--

    }

}
