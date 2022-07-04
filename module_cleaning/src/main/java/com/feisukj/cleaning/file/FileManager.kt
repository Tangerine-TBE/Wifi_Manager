package com.feisukj.cleaning.file

import android.os.Environment
import android.util.Log
import com.example.module_base.cleanbase.BaseConstant

import com.feisukj.cleaning.bean.AllFileBean
import com.feisukj.cleaning.bean.AppBean
import com.feisukj.cleaning.bean.ImageBean
import com.feisukj.cleaning.db.SQLiteDbManager
import com.feisukj.cleaning.filevisit.FileR
import com.feisukj.cleaning.ui.UIConst
import com.feisukj.cleaning.ui.activity.BigFileActivity
import com.feisukj.cleaning.ui.activity.MusicActivity
import com.feisukj.cleaning.ui.activity.PhotoCleanActivity
import com.feisukj.cleaning.ui.fragment.DocFragment
import com.feisukj.cleaning.utils.Constant
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking

import java.io.File
import java.util.*
import kotlin.collections.ArrayList

object FileManager {
    /*****************************/
    var musicSize=0L
    var docSize=0L
    var packageSize=0L
    /*****************************/
    val pictureFormat=arrayOf(".jpg",".jpeg",".png",".raw",".svg")//".gif",

    val videoFormat=arrayOf(".3gp", ".amv", ".flv", ".mp4",".mpeg",".mpg")

    private fun scanDBFile(){
        try {
            val picPath= SQLiteDbManager.getGarbagePicPathInfo().groupBy {
                it.garbagetype
            }
            picPath["垃圾图片"]?.map { FileR(it.filePath) }?.forEach { file ->
                val fs=scanDirFile2(file,{ file1 ->
//                    ImageBean(file1).also { it.group=it.year*12+it.month }
                    ImageBean(file1).also {
                        val currentDate= Date(it.fileLastModified)
                        it.group = when{
                            UIConst.monthWithinTime.before(currentDate)->{
                                UIConst.MONTH_TIME_ID
                            }
                            UIConst.threeMonthWithinTime.before(currentDate)-> {
                                UIConst.THREE_MONTH_TIME_ID
                            }
                            UIConst.sixMonthWithinTime.before(currentDate)-> {
                                UIConst.SIX_MONTH_TIME_ID
                            }
                            else->{
                                UIConst.SIX_MONTH_AGO
                            }
                        }
                    }
                },isWith = {
                    it.isFile&&it.length()!=0L
                })
                typeCallback.forEach {
                    it.onNextFiles(FileType.garbageImage,fs)
                }
                FileContainer.addAllPhotoFile(fs)
            }
        }catch (e:Exception){
         //   MobclickAgent.reportError(BaseConstant.application,e)
        }
    }

    var isComplete=false
        private set
    private var isStart=false
    private val typeCallback=ArrayList<NextFileCallback>()

    fun start(){
        if (isStart)
            return
        isStart=true
        Thread{
            scanFile(FileR(Environment.getExternalStorageDirectory()))
            scanDBFile()
            Constant.garbagePicturePaths.forEach { str ->
                val fs=scanDirFile(FileR(str),{
//                    ImageBean(this).also { it.group=it.year*12+it.month }
                    ImageBean(this).also {
                        val currentDate= Date(it.fileLastModified)
                        it.group = when{
                            UIConst.monthWithinTime.before(currentDate)->{
                                UIConst.MONTH_TIME_ID
                            }
                            UIConst.threeMonthWithinTime.before(currentDate)-> {
                                UIConst.THREE_MONTH_TIME_ID
                            }
                            UIConst.sixMonthWithinTime.before(currentDate)-> {
                                UIConst.SIX_MONTH_TIME_ID
                            }
                            else->{
                                UIConst.SIX_MONTH_AGO
                            }
                        }
                    }
                },isWith = {
                    this.isFile&&this.length()!=0L
                })
                typeCallback.forEach {
                    it.onNextFiles(FileType.garbageImage,fs)
                }
                FileContainer.addAllPhotoFile(fs)
                Thread.sleep(10)
            }
            applicationGarbagePath.asSequence().filter { it.des.contains("安装包") }.map { it.path.toLowerCase(Locale.getDefault()) }.toSet().forEach {
                val fs=scanDirFile(FileR(it),{
                    AppBean.getAppBean(this)
                },isWith = {
                    this.isFile&&this.length()!=0L&&this.name.endsWith("apk",true)
                })
                typeCallback.forEach {
                    it.onNextFiles(FileType.apk,fs)
                }
                FileContainer.addAllApk(fs)
            }

            isComplete=true
            if (typeCallback.isNotEmpty()) {
                typeCallback.toTypedArray().forEach {
                    Thread.sleep(10)

                    BaseConstant.mainHandler.post {
                        it.onComplete()
                    }
                }
                typeCallback.clear()
            }
        }.start()
    }

    /**
     * 扫描文件
     */
    private fun scanFile(rootFile:FileR){
        if (!rootFile.exists()){
            return
        }
        val start=System.currentTimeMillis()
//        Log.e("时间时间 ","核心线程：${poolExecutor.corePoolSize},最大线程：${poolExecutor.maximumPoolSize}")
        var listFile:LinkedList<FileR>?=null
        if (rootFile.isDirectory&&!rootFile.listFiles().isNullOrEmpty()){
            val files=rootFile.listFiles()?.toList()
            if (files!=null) {
                listFile = LinkedList(files)
            }else{
                listFile = LinkedList()
            }
        }else if(rootFile.isFile){
            sendFile(rootFile)
        }
        for (i in 0 until 8){
            val childFiles=LinkedList<FileR>()
            while (!listFile.isNullOrEmpty()){
                val file=listFile.removeLast()
                if (!file.exists()){
                    continue
                }
                if (file.isDirectory){
                    val childFile=file.listFiles()?:continue
                    childFiles.addAll(childFile)
                }else if (file.isFile&&file.length()!=0L){
                    sendFile(file)
                }
            }
            listFile?.addAll(childFiles)
        }
        Log.e("时间时间 消耗时间",(System.currentTimeMillis()-start).toString())
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    private fun sendFile(file: FileR){
        FileType.values().forEach { type ->
            if (type.isContain(file)){
                val f=when(type){
                    FileType.music -> {
                        musicSize+=file.length()
                        val allFileBean=AllFileBean(file)
//                        allFileBean.group=allFileBean.year*12+allFileBean.month
                        val currentDate=Date(allFileBean.fileLastModified)
                        allFileBean.group = when{
                            UIConst.monthWithinTime.before(currentDate)->{
                                UIConst.MONTH_TIME_ID
                            }
                            UIConst.threeMonthWithinTime.before(currentDate)-> {
                                UIConst.THREE_MONTH_TIME_ID
                            }
                            UIConst.sixMonthWithinTime.before(currentDate)-> {
                                UIConst.SIX_MONTH_TIME_ID
                            }
                            else->{
                                UIConst.SIX_MONTH_AGO
                            }
                        }
                        BaseConstant.mainHandler.post {
                            MusicActivity.addData(type,allFileBean)
                        }
//                        if (typeCallback.find { it is MusicActivity }==null){
//                            MusicActivity.addData(type,allFileBean)
//                        }else{
//                            BaseConstant.mainHandler.post {
//                                MusicActivity.addData(type,allFileBean)
//                            }
//                        }
                        allFileBean
                    }
                    FileType.doc,FileType.pdf,FileType.ppt,FileType.xls,FileType.txt -> {
                        docSize+=file.length()
                        val allFileBean=AllFileBean(file)
//                        allFileBean.group=allFileBean.year*12+allFileBean.month
                        BaseConstant.mainHandler.post {
                            DocFragment.addData(type,allFileBean)
                        }
//                        if (typeCallback.find { it is DocFragment }==null){
//                            DocFragment.addData(type,allFileBean)
//                        }else{
//                            BaseConstant.mainHandler.post {
//                                DocFragment.addData(type,allFileBean)
//                            }
//                        }
                        allFileBean
                    }
                    FileType.apk -> {
                        packageSize+=file.length()
                        val appBean=AppBean.getAppBean(file)
//                        if (typeCallback.find { it is SoftwareManagerFragment }==null){
//                            SoftwareManagerFragment.addData(type,appBean)
//                        }else{
//                            BaseConstant.mainHandler.post {
//                                SoftwareManagerFragment.addData(type,appBean)
//                            }
//                        }
                        FileContainer.addApk(appBean)
                        appBean
                    }
                    FileType.bigFile -> {
                        val allFileBean=AllFileBean(file)
                        FileContainer.addBigFile(allFileBean)
//                        if (typeCallback.find { it is BigFileActivity }==null){
//                            BigFileActivity.addData(type,allFileBean)
//                        }else{
//                            BaseConstant.mainHandler.post {
//                                BigFileActivity.addData(type,allFileBean)
//                            }
//                        }
                        allFileBean
                    }
                    FileType.garbageImage -> {
                        val image=ImageBean(file).also {
//                            it.group=it.year*12+it.month
                            val currentDate= Date(it.fileLastModified)
                            it.group = when{
                                UIConst.monthWithinTime.before(currentDate)-> {
                                    UIConst.MONTH_TIME_ID
                                }
                                UIConst.threeMonthWithinTime.before(currentDate)-> {
                                    UIConst.THREE_MONTH_TIME_ID
                                }
                                UIConst.sixMonthWithinTime.before(currentDate)-> {
                                    UIConst.SIX_MONTH_TIME_ID
                                }
                                else->{
                                    UIConst.SIX_MONTH_AGO
                                }
                            }
                        }
                        FileContainer.addPhotoFile(image)
//                        if (typeCallback.find { it is PhotoCleanActivity }==null){
//                            PhotoCleanActivity.addCachePhoto(image)
//                        }else{
//                            BaseConstant.mainHandler.post {
//                                PhotoCleanActivity.addCachePhoto(image)
//                            }
//                        }
                        image
                    }
                }
                if (typeCallback.isNotEmpty()) {
                    typeCallback.toTypedArray().forEach {
                        it.onNextFile(type,f)
                    }
                }
            }
        }
    }

    fun addCallBack(callback: NextFileCallback){
        if (!isComplete) {
            typeCallback.add(callback)
        }else{
            callback.onComplete()
        }
    }

    fun removeCallBack(callback: NextFileCallback){
        if (typeCallback.isNotEmpty())
            typeCallback.remove(callback)
    }

    fun <T> scanDirsFile(dirFiles:List<FileR>, toT:(FileR)->T, isWith:(FileR)->Boolean, dirNextFileCallback: DirNextFileCallback<T>?=null, fileCount:Int=-1,isStop:()->Boolean={false}):List<T>{
        val listResult=ArrayList<T>()
        dirFiles.forEach { fileR ->
            FileR.coroutineScanningFile_(fileR){ file, coroutine->
                if (isWith(file)){
                    toT(file).also {
                        synchronized(listResult){
                            listResult.add(it)
                        }
                        dirNextFileCallback?.onNextFile(it)
                        if ((fileCount!=-1&&listResult.size>=fileCount)||isStop()){
                            coroutine.cancel()
                        }
                    }
                }
            }.also {
                runBlocking {
                    it.join()
                }
                if (fileCount==listResult.size){
                    return@forEach
                }
            }
        }
        return listResult
    }

    fun <T> scanDirFile(dirFile:FileR, toT:FileR.()->T, isWith:FileR.()->Boolean, dirNextFileCallback: DirNextFileCallback<T>?=null, fileCount:Int=-1):List<T>{
        if (dirFile.exists()&&dirFile.isFile){
            return emptyList()
        }
        val listResult=ArrayList<T>()

        FileR.coroutineScanningFile_(dirFile){file,coroutine->
            if (isWith(file)){
                toT(file).also {
                    synchronized(listResult){
                        listResult.add(it)
                    }
                    dirNextFileCallback?.onNextFile(it)
                    if (fileCount!=-1&&listResult.size>=fileCount){
                        coroutine.cancel()
                    }
                }
            }
        }.also {
            runBlocking {
                it.join()
            }
            if (fileCount==listResult.size){
                return@also
            }
        }
        return listResult
    }

    fun <T> scanDirFile2(dirFile:FileR,toT:(FileR)->T, isWith:((FileR)->Boolean)?=null, dirNextFileCallback: DirNextFileCallback<T>?=null,fileCount:Int=-1):List<T>{
        if (dirFile.exists()&&dirFile.isFile){
            return emptyList()
        }
        val listResult=ArrayList<T>()

        FileR.coroutineScanningFile_(dirFile){file,coroutine->
            if (isWith?.invoke(file)!=false&&file.length()!=0L){
                toT(file).also {
                    synchronized(listResult){
                        listResult.add(it)
                    }
                    dirNextFileCallback?.onNextFile(it)
                    if (fileCount!=-1&&listResult.size>=fileCount){
                        coroutine.cancel()
                    }
                }
            }
        }.also {
            runBlocking {
                it.join()
            }
        }
        return listResult
    }

    fun scanDirFile2(dirFile:FileR, isWith:((FileR)->Boolean)?=null, onNext: ((FileR) -> Boolean?)?=null):List<FileR>{
        if (dirFile.exists()&&dirFile.isFile){
            return emptyList()
        }
        val listResult=ArrayList<FileR>()

        FileR.coroutineScanningFile_(dirFile){file,coroutine->
            if (isWith?.invoke(file)!=false&&file.length()!=0L){
                synchronized(listResult){
                    listResult.add(file)
                }
                if (onNext?.invoke(file)==false){
                    coroutine.cancel()
                }
            }
        }.also {
            runBlocking {
                it.join()
            }
        }
        return listResult
    }

    fun scanDirFile3(dirFiles:List<FileR>, isWith:((FileR)->Boolean)?=null, onNext:((FileR)->Boolean)?=null):List<FileR>{
        val listResult=ArrayList<FileR>()

        var isCancel=false
        dirFiles.forEach { fileR ->
            FileR.coroutineScanningFile_(fileR){file,coroutine->
                if (isWith?.invoke(file)!=false){
                    synchronized(listResult){
                        listResult.add(file)
                    }
                    if (onNext?.invoke(file)==false){
                        isCancel=true
                        coroutine.cancel()
                    }
                }
            }.also {
                runBlocking {
                    it.join()
                }
                if (isCancel){
                    return@forEach
                }
            }
        }

        return listResult
    }
}