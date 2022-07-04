package com.example.module_tool.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.example.module_tool.R
import com.example.module_tool.utils.KeyBoardUtlis
import com.example.module_base.utils.ToastUtil
import kotlinx.android.synthetic.main.dialog_tall.*

class TallDialog(context: Context,val toDo: ((Int)->Unit)): Dialog(context, R.style.MyDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_tall)

        val la = window?.attributes
        la?.width = WindowManager.LayoutParams.MATCH_PARENT
        la?.height = WindowManager.LayoutParams.WRAP_CONTENT
        la?.gravity = Gravity.BOTTOM

        initView()
    }

    private fun initView() {
        close.setOnClickListener {
            dismiss()
        }
        btn.setOnClickListener {
            val s = editText.text.toString()
            if (s.isEmpty()){
                ToastUtil.showShortToast("身高不可为空")
                return@setOnClickListener
            }
            toDo.invoke(Integer.parseInt(s))
            dismiss()
//            callback.setTall(Integer.parseInt(s))
        }

        KeyBoardUtlis.autoShowKeyBoard(editText)

    }

//    interface TallDialogCallback{
//        fun setTall(tall: Int)
//    }
}