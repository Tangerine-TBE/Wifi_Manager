package com.example.module_tool.widget

import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow

class PopupWindowView2(private val contentView: View, val width:Int=ViewGroup.LayoutParams.WRAP_CONTENT, val height:Int=ViewGroup.LayoutParams.WRAP_CONTENT) {
    private var popupWindow: PopupWindow = PopupWindow(contentView,width,height)

    fun isFocusable(isFocusable:Boolean){
        popupWindow.isFocusable=isFocusable
    }

    fun showAsDropDown(inViewDown:View){
        popupWindow.showAsDropDown(inViewDown)
    }

    fun dismiss(){
        popupWindow.dismiss()
    }

    fun onDismissInvoke(unit:()->Unit){
        popupWindow.setOnDismissListener {
            unit.invoke()
        }
    }

    fun <T:View> findViewById(resId:Int):T{
        return popupWindow.contentView.findViewById(resId)
    }
}