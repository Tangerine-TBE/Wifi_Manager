package com.feisukj.cleaning.utils

import android.annotation.SuppressLint
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import android.text.format.Formatter
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.module_base.cleanbase.BaseConstant
import com.example.module_base.cleanbase.ForegroundObserver

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.AppBean.Companion.getAppBean
import io.zhuliang.appchooser.AppChooser
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


fun getNotUsedAppSize(context: Context,isActive:()->Boolean={true}):Long?{
    var totalSize=0L
    if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
        val ua= when {
            Build.VERSION.SDK_INT== Build.VERSION_CODES.LOLLIPOP -> {
                context.getSystemService("usagestats") as? UsageStatsManager
            }
            Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP_MR1 -> {
                context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            }
            else -> {
                null
            }
        }
        if (ua==null){
            return null
        }
        val usageStats=ua.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY,0,System.currentTimeMillis())
        if (usageStats.isNullOrEmpty()){
            return null
        }
        val useAppTime=HashMap<String,Long>()
        usageStats.forEach {
            if (!isActive()){
                return null
            }
            if (it.totalTimeInForeground>0L){
                useAppTime[it.packageName] = it.lastTimeUsed
            }
        }
        val p=System.currentTimeMillis()-2L*24*60*60*1000
        useAppTime.forEach { entry ->
            if (!isActive()){
                return null
            }
            if (isActive()&&entry.value<p){
                getAppBean(context, entry.key)?.let {
                    totalSize+=it.appBytes
                    totalSize+=it.cacheBytes
                    totalSize+=it.dataBytes
                    totalSize+=it.fileSize
                }
            }
        }
        return totalSize
    }else{
        return null
    }
}

fun toUninstall( context: Context,appID:String){
    if (isInstallApp(appID,context)){
        val uri= Uri.parse("package:$appID")
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = uri
        context.startActivity(intent)
    }
}
fun isInstallApp(packageName:String, context: Context):Boolean{
    return try {
        context.packageManager.getApplicationInfo(packageName,0)
        true
    }catch (e:Exception){
        false
    }
}

fun delFile(path: String?): Boolean {
    if (path != null) {
        val file = File(path)
        if (file.exists()) {
            return file.delete()
        }
    }
    return true
}

fun deleteDirectory(_dir: String): Boolean {
    var dir = _dir
    // 如果dir不以文件分隔符结尾，自动添加文件分隔符
    if (!dir.endsWith(File.separator))
        dir += File.separator
    val dirFile = File(dir)
    if (dirFile.exists()&&dirFile.isFile){
        return delFile(dirFile.absolutePath)
    }
    // 如果dir对应的文件不存在，或者不是一个目录，则退出
    if (!dirFile.exists() || !dirFile.isDirectory) {
        println("删除目录失败：" + dir + "不存在！")
        return false
    }
    // 用于标识是否删除成功
    var flag = true
    // 删除文件夹中的所有文件包括子目录
    val files = dirFile.listFiles()?:return true
    for (i in files.indices) {
        // 删除子文件
        val file=files[i]
        if (!file.exists()){
            continue
        }
        if (file.isFile) {
            flag = delFile(file.absolutePath)
            if (!flag)
                break
        } else if (file.isDirectory) {
            flag = deleteDirectory(file
                    .absolutePath)
        }// 删除子目录
    }
    if (!flag) {
        println("删除目录失败！")
        return false
    }
    // 删除当前目录
    return if (dirFile.delete()) {
        println("删除目录" + dir + "成功！")
        true
    } else {
        false
    }
}

fun copyFile(fromFile:File,toFile:File){
    if (fromFile.isFile){
        val fileInputStream=fromFile.inputStream()
        val byteArray=ByteArray(1024)
        var length:Int
        length=fileInputStream.read(byteArray)
        if (toFile.isFile){
            val fileOutputStream=toFile.outputStream()
            while (length!=-1){
                fileOutputStream.write(byteArray,0,length)
                length=fileInputStream.read(byteArray)
            }
        }else{
            val fileName=fromFile.name
            val newFileOutputStream=File(toFile.absolutePath+"/$fileName").outputStream()
            while (length!=-1){
                newFileOutputStream.write(byteArray,0,length)
                length=fileInputStream.read(byteArray)
            }
        }
    }else{
        val dirName=fromFile.name
        val newDirFile=File(toFile.absolutePath+"/$dirName")
        if (!newDirFile.exists()){
            newDirFile.mkdir()
        }
        fromFile.listFiles()?.forEach {
            copyFile(it,newDirFile)
        }
    }
}

fun getDirFileSize(file:File,fileNameLimit:String?=null,formats:String?=null):Long{
    var fileLength=0L
    if (!file.exists())
        return fileLength
    if (file.isFile){
        fileLength+=file.length()
    }else if (file.isDirectory){
        file.listFiles()?.forEach {
            if (it.isFile){
                if (fileNameLimit==null||it.name.endsWith(fileNameLimit)) {
                    val format=it.name.substringAfterLast(".")
                    if (formats==null||format==formats)
                        fileLength+=it.length()
                }
            }else{
                fileLength+=getDirFileSize(it,fileNameLimit,formats)
            }
        }
    }
    return fileLength
}

fun formatFileSize(context: Context?,sizeBytes:Long):Pair<String,String>{
    context?:return Pair("0","B")
    val text=Formatter.formatFileSize(context,sizeBytes)
    val t1= StringBuilder()
    val t2= StringBuilder()
    for (c in text){
        if (c in '0' .. '9'||c=='.'){
            t1.append(c)
        }else if (c in 'A' .. 'z'){
            t2.append(c)
        }
    }
    return Pair(t1.toString(),t2.toString())
}

fun getSizeString(size: Long, decimal:Int=2,separation:String=" "):String {
    val kb = 1000
    val mb = 1000_000
    val gb = 1000_000_000
    val varSize:Float
    val sizeString:String
    val formatString=StringBuilder("#")
    for (i in 0 until decimal){
        if (i==0){
            formatString.append(".0")
        }else{
            formatString.append("0")
        }
    }
    val format= DecimalFormat(formatString.toString())
    if (size>gb) {
        varSize=(size/gb.toFloat())
        sizeString=format.format(varSize)+"${separation}GB"
    } else if (size>mb) {
        varSize=(size/mb.toFloat())
        format.format(varSize)
        sizeString=format.format(varSize)+"${separation}MB"
    } else if (size>kb) {
        varSize=(size/kb.toFloat())
        format.format(varSize)
        sizeString=format.format(varSize)+"${separation}KB"
    } else {
        sizeString=size.toString()+"${separation}B"
    }
    return sizeString
}

fun ViewGroup.lastChildView(): View?{
    return if (this.childCount>0) {
        this.getChildAt(this.childCount - 1)
    }else{
        null
    }
}

@SuppressLint("SimpleDateFormat")
fun toTimeFormat(time:Long):String{
    val cal=Calendar.getInstance()
//    val formatter=SimpleDateFormat.getDateInstance()//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    val  formatter =SimpleDateFormat("HH:mm:ss")
    cal.timeInMillis=time
    return formatter.format(cal.time)
}

/**
 * 使用其他应用打开文件
 */
fun toAppOpenFile(activity: FragmentActivity, file: File){
    if (!file.exists()||!file.isFile){
        Toast.makeText(activity,R.string.fileUnExis,Toast.LENGTH_SHORT).show()
        return
    }
  //  ForegroundObserver.isShowAd=false
    AppChooser.from(activity)
            .file(file)
            .authority(activity.packageName+".myProvider")
            .load()
}
fun toAppOpenFile(fragment: Fragment, file: File){
    if (!file.exists()||!file.isFile){
        Toast.makeText(fragment.context,R.string.fileUnExis,Toast.LENGTH_SHORT).show()
        return
    }
//    ForegroundObserver.isShowAd=false
    AppChooser.from(fragment)
            .file(file)
            .authority((fragment.context?.packageName?:"com.feisukj.shoujiqlds")+".myProvider")
            .load()
}

/**
 * 分享文本
 */
fun onShareClick(fragment: Fragment, content: String) {
    AppChooser
            .from(fragment)
            .text(content)
            .load()
}
fun onShareClick(activity: FragmentActivity, content: String) {
    AppChooser
            .from(activity)
            .text(content)
            .load()
}
/**
 * 清除默认打开文件的方式
 */
fun cleanOpenFile(activity: FragmentActivity){
    AppChooser.from(activity)
            .cleanDefaults()
}
fun cleanOpenFile(fragment: Fragment){
    AppChooser.from(fragment)
            .cleanDefaults()
}
fun getIcon(packageName:String?): Drawable?{
    packageName?:return null
    return try {
        BaseConstant.application.packageManager?.getPackageInfo(packageName, 0)?.applicationInfo?.loadIcon(BaseConstant.application.packageManager)
    }catch (e:Exception){
        null
    }
}
fun getLabel(packageName: String?):String?{
    packageName?:return null
    return try {
        BaseConstant.application.packageManager?.getPackageInfo(packageName, 0)?.applicationInfo?.loadLabel(BaseConstant.application.packageManager)?.toString()
    }catch (e:Exception){
        null
    }
}

const val isContainsLikeLogAndCache=false

fun getInstallAppPackageName():List<String>{
    return try {
        BaseConstant.application.packageManager?.getInstalledPackages(0)?.map { it.packageName }?: emptyList()
    }catch (e:Exception){
        emptyList()
    }
}

//fun getAppPath():List<Pair<String,String>>{
//    val listAppPath=ArrayList<Pair<String,String>>()
//    try {
//        BaseConstant.application.packageManager?.getInstalledPackages(0)?.forEach {
//            if (it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
//                val package_ = it.packageName
//                val path = "${Environment.getExternalStorageDirectory().absolutePath}/android/data/${it.packageName}"
//                listAppPath.add(Pair(package_, path))
//            }
//        }
//    }catch (e:Exception){
//        e.printStackTrace()
//    }
//    return listAppPath
//}

//fun getCacheAndLogDir(file:File):List<File>{
//    val listFile=ArrayList<File>()
//    if (!file.exists()){
//        return listFile
//    }
//    if (file.isDirectory){
//        val isCache=file.name.let {
//            it.contains("cache",true)||it.contains("log",true)
//        }
//        if (isCache){
//            listFile.add(file)
//        }else{
//            file.listFiles()?.forEach {
//                listFile.addAll(getCacheAndLogDir(it))
//            }
//        }
//    }
//    return listFile
//}

fun isImageFile(filePath: String): Boolean {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(filePath, options)
    return options.outWidth != -1
}
