package com.example.module_base.cleanbase

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.module_base.R

import kotlinx.android.synthetic.main.dialog_sure_false.*

class SureCancelDialog(context: Context):Dialog(context) {
    init {
        setContentView(R.layout.dialog_sure_false)
    }

    fun setContent(text:CharSequence):SureCancelDialog{
        tv_content.text=text
        return this
    }

    fun setOnPositiveClick(unit:(Dialog)->Unit):SureCancelDialog{
        tv_sure.setOnClickListener {
            unit.invoke(this)
        }
        return this
    }

    fun setOnPositiveClick(unit:(Dialog)->Unit,text: String):SureCancelDialog{
        tv_sure.text=text
        tv_sure.setOnClickListener {
            unit.invoke(this)
        }
        return this
    }

    fun setOnNegativeClick(unit:(Dialog)->Unit):SureCancelDialog{
        tv_cancel.setOnClickListener {
            unit.invoke(this)
        }
        return this
    }

    fun setOnNegativeClick(unit:(Dialog)->Unit,text: String):SureCancelDialog{
        tv_cancel.text=text
        tv_cancel.setOnClickListener {
            unit.invoke(this)
        }
        return this
    }
}