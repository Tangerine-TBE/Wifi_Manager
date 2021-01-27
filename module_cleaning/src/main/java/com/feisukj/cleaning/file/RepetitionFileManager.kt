package com.feisukj.cleaning.file

import com.feisukj.cleaning.bean.AllFileBean
import com.feisukj.cleaning.bean.AppBean
import com.feisukj.cleaning.bean.FileBean
import com.feisukj.cleaning.bean.ImageBean
import com.feisukj.cleaning.ui.fragment.LatelyFragment
import com.feisukj.cleaning.utils.Constant
import java.io.File

object RepetitionFileManager {
    data class RepetitionFileKey(val length:Long,val format:String?,val fileHashCode:Int)

    private val basePaths= arrayOf(
            *Constant.weChatUserPath.map { "${it}attachment" }.toTypedArray(),
            *Constant.weChatUserPath.map { "${it}video" }.toTypedArray(),
            *Constant.weChatUserPath.map { "${it}image2" }.toTypedArray(),
            Constant.WE_CHAT_ROOT_PATH_DATA_MircroMsg+"/download",
            Constant.QQ_PATH+"/sv_config_resource",
            Constant.TENCENT_PATH+"/qidian/emoji/emojis",
            if (Constant.TENCENT_PATH!=Constant.QQ_TENCENT)
                Constant.QQ_TENCENT+"/qidian/emoji/emojis"
            else
                "",
            Constant.DCIM,
            Constant.WE_SAVE_PIC,
            Constant.WE_FILE,
            Constant.WE_FILE_DATA,
            "/storage/emulated/0/accdata_vod/pcdn",
            "/storage/emulated/0/BaiduNetdisk",
            "/storage/emulated/0/tencent/tbs/backup",
            "/storage/emulated/0/browser/MediaCache/exoplayer-cache",
            "/storage/emulated/0/AudioRecorder",
            "/storage/emulated/0/qqmusic/gift_anim_zip/v_anim",
            "/storage/emulated/0/backup",
            "/storage/emulated/0/VpnCapture/ParseData",//2020_04_11_15_52_22_22
            "/storage/emulated/0/sina/weibo/storage/video_play_cache",
            "/storage/emulated/0/baiduwenku",
            "/storage/emulated/0/Books",
            "/storage/emulated/0/MIUI/sound_recorder/call_rec",
            Constant.DOWNLOAD)
    lateinit var similarFile:Map<RepetitionFileKey,List<FileBean>>
        private set

    var isComplete=false
        private set
    private var isStart=false
    var repetitionFileSize=0L
        private set
    var repetitionFileCount=0
        private set

    var callback:((Map<RepetitionFileKey,List<FileBean>>)->Unit)?=null

    fun findSimilarFile(callback:((Map<RepetitionFileKey,List<FileBean>>)->Unit)?=null){
        if (isStart){
            if (isComplete){
                callback?.invoke(similarFile)
            }else{
                this.callback=callback
            }
            return
        }else{
            isStart=true
        }

        val dirFiles= basePaths.map { File(it) }
        val repetitionFile=HashMap<RepetitionFileKey,HashSet<FileBean>>()
        val samplingCount=100
        FileManager.scanDirFile3(dirFiles,onNext = { file ->
            val fLength=file.length()
            val fileHashCode=file.inputStream().use {
                val byteArray=ByteArray(samplingCount){
                    Byte.MIN_VALUE
                }
                if (fLength<=samplingCount){
                    it.read(byteArray)
                }else{
                    val singleSkip=fLength/samplingCount
                    for (i in 0 until samplingCount){
                        byteArray[i]=it.read().toByte()
                        it.skip(singleSkip)
                    }
                }
                byteArray
            }.contentHashCode()

//            Log.d("重复文件","路径：${file.absolutePath},文件哈希值：${fileHashCode}")

            val f=getFileBean(file)
            val k=if (f.fileSize==0L){
                RepetitionFileKey(0L,null,fileHashCode)
            }else{
                if (f.fileName.contains(".")){
                    RepetitionFileKey(f.fileSize,f.fileName.split(".").lastOrNull(),fileHashCode)
                }else{
                    RepetitionFileKey(f.fileSize,null,fileHashCode)
                }
            }
            repetitionFile[k].let {
                if (it==null){
                    repetitionFile[k]= hashSetOf(f)
                }else{
                    it.add(f)
                }
            }
            true
        })

        val a=repetitionFile.filter { entry ->
            entry.value.size>=2
        }

        val t=HashMap<RepetitionFileKey,List<FileBean>>()
        a.entries.forEach {
            t[it.key]=it.value.toList()
        }
        similarFile=t
        val flatten=t.values.flatten()
        repetitionFileCount=flatten.size
        repetitionFileSize=flatten.map { it.fileSize }.sum()
        isComplete=true

        this.callback?.invoke(similarFile)
        this.callback=null
    }

    private fun getFileBean(file: File):FileBean{
        val type= LatelyFragment.LatelyFileType.getFileType(file.name)
        val f=when(type){
            LatelyFragment.LatelyFileType.Pictures -> {
                ImageBean(file)
            }
            LatelyFragment.LatelyFileType.Media -> {
                if (Constant.VIDEO_FORMAT.any { file.name.endsWith(it,true) }){
                    ImageBean(file)
                }else{
                    AllFileBean(file)
                }
            }
            LatelyFragment.LatelyFileType.Apk -> {
                FileContainer.getApkFileList()
                        .find { it.absolutePath==file.absolutePath }
                        ?: AppBean.getAppBean(file)
            }
            else->{
                AllFileBean(file)
            }
        }
        return f
    }
}