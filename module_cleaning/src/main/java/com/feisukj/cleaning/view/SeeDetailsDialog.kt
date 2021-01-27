package com.feisukj.cleaning.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.feisukj.cleaning.R
import kotlinx.android.synthetic.main.dalog_see_details_clean.*

class SeeDetailsDialog (context: Context): Dialog(context) {
    init {
        setContentView(R.layout.dalog_see_details_clean)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setTime(text:String):SeeDetailsDialog{
        time.text=text
        return this
    }

    fun setPath(text:String):SeeDetailsDialog{
        path.text=text
        return this
    }

    fun setOnPositiveClick(unit:(Dialog)->Unit,text: String?=null):SeeDetailsDialog{
        text?.let {
            tv_sure.text=text
        }
        tv_sure.setOnClickListener {
            unit.invoke(this)
        }
        return this
    }

    fun setOnNegativeClick(unit:(Dialog)->Unit,text: String?=null):SeeDetailsDialog{
        text?.let {
            tv_cancel.text=text
        }
        tv_cancel.setOnClickListener {
            unit.invoke(this)
        }
        return this
    }
}