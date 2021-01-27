package com.feisukj.cleaning.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.feisukj.cleaning.R
import kotlinx.android.synthetic.main.dialog_open_desktop_view_tips_clean.*

class OpenDesktopViewTipDialog(mContext: Context, themeResId:Int=R.style.myDialog):Dialog(mContext,themeResId) {
    var openOnClick: View.OnClickListener?=null
    var noOpenOnClick: View.OnClickListener?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_open_desktop_view_tips_clean)
        open.setOnClickListener (openOnClick)
        noOpen.setOnClickListener(noOpenOnClick)
    }
}