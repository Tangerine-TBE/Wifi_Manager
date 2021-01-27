package com.feisukj.cleaning.manager

import android.content.Intent
import com.example.module_base.cleanbase.ActivityList
import com.example.module_base.cleanbase.BaseConstant
import com.example.module_base.utils.SPUtil

import com.feisukj.cleaning.file.adGarbagePaths
import com.feisukj.cleaning.file.applicationGarbagePath
import com.feisukj.cleaning.file.shortVideoPath
import com.feisukj.cleaning.file.weAndQQPaths
import com.feisukj.cleaning.ui.activity.ShortVideoDesActivity2
import com.feisukj.cleaning.ui.activity.WeChatAndQQCleanActivity
import com.feisukj.cleaning.utils.*
import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class FloatBallManager private constructor(){
    companion object{
        val instance by lazy { FloatBallManager() }
        private const val FLOAT_SWITCH_KEY="float_switch"
        private const val USER_ACCESS_KEY="user_access"
        var fromFloatBall=false//标识是否是从悬浮球进入activity
    }
    private val executors by lazy {  ThreadPoolExecutor(1,1,30, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>()) }
    private val floatBallControl=FloatBallControl.instance

    /**
     * 悬浮球开关
     */
    var floatBallSwitch=false
        set(value) {
            if (field!=value){
                field=value
                SPUtil.getInstance().putBoolean(FLOAT_SWITCH_KEY,value)
            }
        }
    /**
     * 查看使用情况开关
     */
    var userAccessSwitch=false
        set(value) {
            if (field!=value){
                field=value
                SPUtil.getInstance().putBoolean(USER_ACCESS_KEY,value)
            }
        }
    init {
        floatBallSwitch=SPUtil.getInstance().getBoolean(FLOAT_SWITCH_KEY,false)
        userAccessSwitch=SPUtil.getInstance().getBoolean(USER_ACCESS_KEY,false)
    }

    /**
     * 设置悬浮球显示的垃圾大小
     */
    fun setFloatViewShowSize(sizeString:List<String>){
        if (sizeString.size<2)
            return
        floatBallControl.setGSizeString(sizeString[0])
        floatBallControl.setGSizeUnit(sizeString[1])
    }

    private val updateFloatBallViewRunnable by lazy {
        Runnable {
            Thread.sleep(500)
            updateFloatBallView()
            Thread.sleep(1000*60*5)
        }
    }
    private var intent=Intent()
    private enum class ToAct(val clazz:Class<*>?){
        qq(WeChatAndQQCleanActivity::class.java),we(WeChatAndQQCleanActivity::class.java),
        short(ShortVideoDesActivity2::class.java),home(ActivityList.HomeActivity.cls);
    }
    private var toAct=ToAct.home

    init {
        executors.execute(updateFloatBallViewRunnable)

    }

    private fun getFileSize(listFile:List<String>,fileNameLimit:String?=null,formats:String?=null):Long{
        var length=0L
        var i=0
        while (i<listFile.size){
            length+=getDirFileSize(File(listFile[i]),fileNameLimit=fileNameLimit,formats = formats)
            i++
        }
        return length
    }
    fun updateFloatBallView(){
        if (!floatBallSwitch){
            return
        }

        //微信垃圾
        var weChatGarbageLength=0L
        weChatGarbageLength+=getFileSize(Constant.weChatGarbage)
        weChatGarbageLength+=getFileSize(Constant.weChatCache,fileNameLimit="_panel_enable")
        weChatGarbageLength+=getFileSize(Constant.weChatFriend)
        weChatGarbageLength+=getFileSize(Constant.weChatOther,formats="jpg")
        //qq垃圾
        val qqGarbage=ArrayList<String>().apply {
            weAndQQPaths.toTypedArray().forEach { weAndQQClean ->
                weAndQQClean.paths?.let {
                    addAll(it)
                }
            }
        }
        val qqGarbageLength=getFileSize(qqGarbage)
        qqGarbage.clear()
        //短视频垃圾
        val shortVideoGarbage= shortVideoPath.map { it.path }
        val shortVideoGarbageLength=getFileSize(shortVideoGarbage)
        //首页垃圾
        val homeGarbage=ArrayList<String>().apply {
            adGarbagePaths.toTypedArray().forEach {
                add(it.path)
            }
            applicationGarbagePath.toTypedArray().forEach { applicationGarbage ->
                this.add(applicationGarbage.path)
            }
//            getAppPath().map { it.second }.forEach { appPath ->
//                val file=File(appPath)
//                addAll(getCacheAndLogDir(file).map { it.absolutePath })
//            }
        }
        val homeGarbageLength=getFileSize(homeGarbage)
        homeGarbage.clear()


        val max= listOf(weChatGarbageLength,qqGarbageLength,shortVideoGarbageLength,homeGarbageLength).maxBy { it }?:return
        val maxList=getSizeString(max,separation = ",").split(",")
        if(maxList.size<2)
            return
        when(max){
            weChatGarbageLength->{
                BaseConstant.mainHandler.post {
                    floatBallControl.setGSizeString(maxList[0])
                    floatBallControl.setGSizeUnit(maxList[1])
                    floatBallControl.setGDes("微信缓存需清理")
                    intent.putExtra(WeChatAndQQCleanActivity.KEY,WeChatAndQQCleanActivity.WE_CHAT)
                    toAct=ToAct.we
                }
            }
            qqGarbageLength->{
                BaseConstant.mainHandler.post {
                    floatBallControl.setGSizeString(maxList[0])
                    floatBallControl.setGSizeUnit(maxList[1])
                    floatBallControl.setGDes("QQ缓存需清理")
                    intent.putExtra(WeChatAndQQCleanActivity.KEY,WeChatAndQQCleanActivity.QQ)
                    toAct=ToAct.qq
                }
            }
            shortVideoGarbageLength->{
                BaseConstant.mainHandler.post {
                    floatBallControl.setGSizeString(maxList[0])
                    floatBallControl.setGSizeUnit(maxList[1])
                    floatBallControl.setGDes("短视频缓存需清理")
                    toAct=ToAct.short
                }
            }
            homeGarbageLength->{
                BaseConstant.mainHandler.post {
                    floatBallControl.setGSizeString(maxList[0])
                    floatBallControl.setGSizeUnit(maxList[1])
                    floatBallControl.setGDes("待清理垃圾")
                    toAct=ToAct.home
                }
            }
        }
    }
}