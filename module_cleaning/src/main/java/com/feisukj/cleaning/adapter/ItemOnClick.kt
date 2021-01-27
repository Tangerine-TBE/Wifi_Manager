package com.feisukj.cleaning.adapter

import android.view.View
import com.feisukj.cleaning.bean.FileTypeKey

interface ItemOnClick<T> {
    fun onMyClick(view: View,t:T){}
    fun onCheckItem(t:T,isCheck:Boolean){}//

    fun onGroupTitleCheck(group:Int,isCheck: Boolean){}
    fun onGroupItemCheck(day:Int,fileTypeKey: FileTypeKey,position: Int,isCheck: Boolean){}
}