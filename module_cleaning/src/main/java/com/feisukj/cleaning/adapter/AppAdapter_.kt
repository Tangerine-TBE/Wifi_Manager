package com.feisukj.cleaning.adapter

import com.feisukj.cleaning.bean.AppBean

open class AppAdapter_(layoutInt: Int, listData:List<AppBean>, compare1:Comparator<AppBean>):SortListAdapter<AppBean>(layoutInt,listData,compare1) {
    open fun removeItem(packageName: String) {
        listData.find { it.packageName==packageName }?.let {
            removeItem(it)
        }
    }
}