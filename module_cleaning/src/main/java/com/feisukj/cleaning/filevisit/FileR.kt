package com.feisukj.cleaning.filevisit

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import com.example.module_base.cleanbase.BaseConstant
import com.example.module_base.cleanbase.eLog
import com.example.module_base.cleanbase.iLog
import kotlinx.coroutines.*
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class FileR constructor(private val documentFile: DocumentFile?) {
    companion object{
        private val handler=CoroutineExceptionHandler(handler = {coroutineContext, throwable ->
            eLog(throwable.stackTraceToString(),"异常-协程异常")
        })
        private val coroutineScope= CoroutineScope(Dispatchers.IO+ handler)
        fun coroutineScanningFile(fileR: FileR, fileCall:(FileR)->Unit): Job {
            coroutineScanningFile_(fileR){file, coroutineScope ->
                fileCall.invoke(file)
            }
            val list= LinkedList(fileR.listFiles()?.toList()?: emptyList())
            return coroutineScope.launch {
                while (synchronized(list){list.isNotEmpty()}){
                    launch {
                        while (synchronized(list){ list.isNotEmpty() }&&isActive){
                            val item= synchronized(list){
                                list.pop()
                            }
                            launch {
                                if (item.isDirectory){
                                    async {
                                        val array=item.listFiles()?: emptyArray()
                                        synchronized(list){
                                            list.addAll(array)
                                        }
                                        array
                                    }
                                }else if (item.isFile){
                                    item.length()
                                    fileCall.invoke(item)
                                }
                            }
                        }
                    }.join()
                }
            }
        }

        fun coroutineScanningFile_(fileR: FileR, fileCall:(FileR,CoroutineScope)->Unit): Job {
            val list= LinkedList(fileR.listFiles()?.toList()?: emptyList())
            return coroutineScope.launch {
                val coroutineScope=this
                while (synchronized(list){list.isNotEmpty()}){
                    launch {
                        while (synchronized(list){ list.isNotEmpty() }&&isActive){
                            val item= synchronized(list){
                                list.pop()
                            }
                            launch {
                                if (item.isDirectory){
                                    async {
                                        val array=item.listFiles()?: emptyArray()
                                        synchronized(list){
                                            list.addAll(array)
                                        }
                                        array
                                    }
                                }else if (item.isFile){
                                    item.length()
                                    fileCall.invoke(item,coroutineScope)
                                }
                            }
                        }
                    }.join()
                }
            }
        }
    }

    val context= BaseConstant.application
    constructor(file: File):this(
            {
                var documentFile:DocumentFile?=null
                val isData= DocumentFileUtil.isContainDataDir(file.path)
                if (isData){
                    if (file.exists()||file.absolutePath.contains("com.tencent.mm")||file.absolutePath.contains("com.tencent.mobileqq")){
                        if ((ContextCompat.checkSelfPermission(BaseConstant.application,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                                        ||Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                                && DocumentFileUtil.isDataDirPermission(BaseConstant.application)){
                            documentFile= DocumentFileUtil.pathToDocumentFile(BaseConstant.application, file.path)
                            documentFile
                        }else{
                            null
                        }
                    }else{
                        null
                    }
                }else{
                    null
                }
            }.invoke()
    ){
        this.file=file
    }
    constructor(path:String):this(File(path)){
        this.path=path
    }
    constructor(dir:String,name:String):this("$dir/$name")
    constructor(dir: File, name:String):this("${dir.absolutePath}/$name")
    constructor(dir: FileR, name:String):this("${dir.absolutePath}/$name")
    private var isTryPath=false
    var path:String?=null
        get() {
            if (field==null&&!isTryPath){
                isTryPath=true
                val uri=documentFile?.uri?.toString()
                if (uri!=null) {
                    val p= DocumentFileUtil.uriStringToPath(uri)
                    field=p
                    file=File(p)
                }
            }
            return field
        }

    var file: File?=null
        get() {
            if (field==null){
                val p=path
                if (p!=null) {
                    field = File(path)
                }
            }
            return field
        }
        private set

    val isDirectory by lazy {
        if (!exists()){
            eLog("文件不存在,${path}")
            false
        }else if (file?.canRead()==true){
            file?.isDirectory?:false
        }else if (documentFile!=null){

            val a=documentFile?.isDirectory ?:false

            a
        }else{
            eLog("没有访问权限,path:${path}")
            false
        }
    }
    val isFile by lazy {
        if (!exists()){
//            eLog("文件不存在,${path}")
            false
        }else if (file?.canRead()==true){
            file?.isFile?:false
        }else if (documentFile!=null){
            documentFile?.isFile ?:false
        }else{
            eLog("没有访问权限,${documentFile?.exists()},path:${path}")
            false
        }
    }
    val absolutePath by lazy {
        file?.absolutePath?:""
    }
    val name by lazy {
        file?.name?:""
    }
    val uri=documentFile?.uri

    fun exists():Boolean{
        if (documentFile!=null){
            return true
        }

        return file?.exists()?:false
    }

    fun canRead():Boolean{
        return file?.canRead()?.or(documentFile!=null)?:false
    }

    fun canWrite():Boolean{
        return file?.canWrite()?.or(documentFile!=null)?:false
    }

    private var listFile:Array<FileR>?=null
    fun listFiles():Array<FileR>?{

        if (listFile!=null){
            return listFile
        }
        return if (canRead()){
            if (file?.canRead()==true) {
                file?.listFiles()?.map { FileR(it) }
            }else{
                val d=documentFile?.listFiles()?.map {
//                    var start=System.currentTimeMillis()
                    val r= FileR(it)
//                    iLog((System.currentTimeMillis()-start).toString(),"哈哈哈反反复复v")
                    r
                }
                d
            }?.toTypedArray()
        }else{
            iLog("canRead()=false,path:${path}")
            null
        }.also {
            listFile=it
        }
    }

    private var length=-1L
    fun length():Long{
        if (!exists()){
            return 0
        }
        if (length!=-1L){
            return length
        }
        return if (canRead()){
            if (file?.canRead()==true) {
                file?.length()?:0L
            }else{
                documentFile?.length()?:0L
            }
        }else{
            iLog("canRead()=false,path:${path}")
            0L
        }.also {
            length=it
        }
    }

    private var lastModified=-1L
    fun lastModified():Long{
        if (lastModified!=-1L){
            return lastModified
        }
        if (!exists()){
            lastModified=0L
            return 0
        }
        return if (canRead()){
            if (file?.canRead()==true) {
                file?.lastModified()?:0L
            }else{
                documentFile?.lastModified()?:0L
            }
        }else{
            iLog("canRead()=false,path:${path}")
            0L
        }.also {
            lastModified=it
        }
    }

    fun renameTo(context: Context,dest: FileR):Boolean{
        return if (file?.canWrite()==true){
            file?.renameTo(dest.file?:return false)?:false
        }else if (documentFile!=null){
            val uri1=documentFile?.uri
            val uri2=dest.uri
            var result=false
            if (uri1!=null&&uri2!=null) {
                val inputStream=context.contentResolver.openInputStream(uri1)
                val outputStream=context.contentResolver.openOutputStream(uri2)
                inputStream?.use {red->
                    outputStream?.use { write->
                        val byteArray=ByteArray(1024*8)
                        var len=-1
                        while (red.read(byteArray).also { len=it }!=-1){
                            write.write(byteArray,0,len)
                        }
                        documentFile?.delete()
                        result=true
                    }
                }
            }
            result
        }else{
//            eLog("没有写入权限")
            false
        }
    }

    fun mkdir(){
        if (file?.absolutePath?.let { DocumentFileUtil.isContainDataDir(it) }!=true){
            file?.mkdir()
        }else{
            documentFile?.parentFile?.createFile("*/*",documentFile.name?:return)
        }
    }

    fun delete():Boolean{
        if (!exists()){
            return false
        }
        return if (file?.canWrite()==true){
            file?.delete()?:false
        }else if (documentFile!=null){
            documentFile?.delete()?:false
        }else{
//            eLog("没有写入权限")
            false
        }
    }

    fun openInputStream():InputStream?{
        if (!exists()){
            return null
        }
        if (file?.canRead()==true){
            return file?.inputStream()
        }else {
            return context.contentResolver.openInputStream(documentFile?.uri ?: return null)
        }
    }

    fun openOutputStream():OutputStream?{
        if (!exists()){
            return null
        }
        if (file?.canWrite()==true){
            return file?.outputStream()
        }else {
            return context.contentResolver.openOutputStream(documentFile?.uri ?: return null)
        }
    }
}