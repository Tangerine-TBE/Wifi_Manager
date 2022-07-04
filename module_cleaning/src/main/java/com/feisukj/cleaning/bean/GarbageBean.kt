package com.feisukj.cleaning.bean

import android.graphics.drawable.Drawable
import com.example.module_base.cleanbase.BaseConstant

import com.feisukj.cleaning.R
import com.feisukj.cleaning.filevisit.FileR
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
        val b=items.add(item)
        fileSize+=item.fileLength
        return b
    }

    fun getItems():List<GarbageItemBean>{
        return items.toList()
    }
}

data class GarbageItemBean(val path:String,val des:String){
    var fileLength=0L
        private set
    private val files by lazy { ArrayList<FileR>() }
    fun addFile(file:FileR):Boolean{
        return synchronized(this){
            fileLength+=file.length()
            files.add(file)
        }
    }

    fun getFiles():List<FileR>{
        return files.toList()
    }
}