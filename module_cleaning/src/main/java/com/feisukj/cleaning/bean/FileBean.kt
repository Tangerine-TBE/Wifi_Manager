package com.feisukj.cleaning.bean

import android.graphics.BitmapFactory
import android.os.Build
import com.example.module_base.cleanbase.BaseConstant
import com.feisukj.cleaning.file.FileManager
import com.feisukj.cleaning.filevisit.DocumentFileUtil
import com.feisukj.cleaning.filevisit.FileR
import com.feisukj.cleaning.utils.getSizeString
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

open class FileBean constructor(val file:File) {
    constructor(path:String):this(File(path)){

    }
    constructor(fileR: FileR):this(File(fileR.absolutePath)){
        this.fileR=fileR
    }
    var fileR:FileR?=null
    init {
        if (fileR==null){
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.R
                    && DocumentFileUtil.isDataDirPermission(BaseConstant.application)){
                fileR=FileR(file)
            }
        }
    }

    var group:Int=0
    val fileLastModified:Long by lazy { file.lastModified() }
    val fileSize:Long by lazy { file.length() }
    val fileSizeString:String by lazy { getSizeString(file.length()) }
    val absolutePath:String=file.absolutePath
    val fileName:String=file.name
    val isFile:Boolean=file.isFile
    val format:String=absolutePath.substringAfterLast(".")

    val year:Int
    val month:Int
    val day:Int
    val hour:Int
    val minute:Int
    val second:Int
    val fileDate:String

    var isCheck:Boolean=false
    var uri =fileR?.uri
        get() {
            field=fileR?.uri
            return field
        }
    val isPicture by lazy {
        val options= BitmapFactory.Options()
        options.inJustDecodeBounds=true
        if (java.io.File(absolutePath).canRead()){
            BitmapFactory.decodeFile(absolutePath,options)
        }else{
            BitmapFactory.decodeStream(fileR?.openInputStream(),null,options)
        }
        options.outHeight!=-1&&options.outWidth!=-1
    }

    val isVideo by lazy {
        FileManager.videoFormat.any {
            fileName.endsWith(it,true)
        }
    }

    val isMusic by lazy {
        listOf(".mp3").any {
            fileName.endsWith(it,true)
        }
    }

    init {
        val cal = Calendar.getInstance()
        cal.timeInMillis = file.lastModified()
        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)
        day = cal.get(Calendar.DAY_OF_MONTH)
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
        second = cal.get(Calendar.SECOND)
        val formatter=SimpleDateFormat.getDateInstance()
        fileDate=formatter.format(cal.time)
    }

    override fun hashCode(): Int {
        return absolutePath.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is FileBean) return false
        return other.absolutePath==absolutePath
    }
}