package com.feisukj.cleaning.bean

import com.feisukj.cleaning.utils.getSizeString
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

open class FileBean(file:File) {
    var group:Int=0
    val fileLastModified:Long = file.lastModified()
    val fileSize:Long=file.length()
    val fileSizeString:String= getSizeString(fileSize)
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
}