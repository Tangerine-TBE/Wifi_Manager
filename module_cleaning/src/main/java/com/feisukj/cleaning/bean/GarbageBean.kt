package com.feisukj.cleaning.bean

import android.graphics.drawable.Drawable
import com.example.module_base.cleanbase.BaseConstant

import com.feisukj.cleaning.R
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

data class GarbageBean(val packageName:String?, val title:String?=null,val pathHashCode:Int=0){
    var isCheck=false
    var fileSize:Long=0L
        private set

    var icon: Drawable? =packageName?.let {
        val context= BaseConstant.application
        try {
            context.packageManager?.getPackageInfo(it, 0)?.applicationInfo?.loadIcon(context.packageManager)
        }catch (e:Exception){
            null
        }
    }

    val label:String =packageName?.let {
        val context=BaseConstant.application
        try {
            context.packageManager?.getPackageInfo(it, 0)?.applicationInfo?.loadLabel(context.packageManager)?.toString()
        }catch (e:Exception){
            null
        }
    }?:BaseConstant.application.getString(R.string.unknow)

    private val items by lazy { LinkedList<GarbageItemBean>() }

    fun addItem(item:GarbageItemBean):Boolean{
        items.add(item)
//        if (b){
            fileSize+=item.fileLength
//        }
        return true
    }

    fun getItems():List<GarbageItemBean>{
        return items.toList()
    }
}

data class GarbageItemBean(val path:String,val des:String){
    var fileLength=0L
        private set
    private val files by lazy { ArrayList<File>() }
    fun addFile(file:File):Boolean{
        val b=files.add(file)
//        if(b){
            fileLength+=file.length()
//        }
        return true
    }

    fun getFiles():List<File>{
        return files.toList()
    }
}