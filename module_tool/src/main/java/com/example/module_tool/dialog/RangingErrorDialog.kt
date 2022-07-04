package com.example.module_tool.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.example.module_tool.R
import kotlinx.android.synthetic.main.dialog_ranging_error.*

class RangingErrorDialog(context: Context,val helpUnit: ()->Unit,val againUnit: ()->Unit): Dialog(context, R.style.MyDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_ranging_error)

        val a = window?.attributes
        a?.width = WindowManager.LayoutParams.MATCH_PARENT
        a?.width = WindowManager.LayoutParams.WRAP_CONTENT
        a?.gravity = Gravity.BOTTOM

        close.setOnClickListener {
            dismiss()
        }
        help.setOnClickListener {
            dismiss()
            helpUnit.invoke()
        }
        again.setOnClickListener {
            dismiss()
            againUnit.invoke()
        }
    }
}