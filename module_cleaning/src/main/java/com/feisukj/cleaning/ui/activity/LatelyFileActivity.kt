package com.feisukj.cleaning.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.feisukj.cleaning.bean.AllFileBean
import com.feisukj.cleaning.bean.AppBean
import com.feisukj.cleaning.bean.FileBean
import com.feisukj.cleaning.bean.ImageBean
import com.feisukj.cleaning.file.FileContainer
import com.feisukj.cleaning.file.FileManager
import com.feisukj.cleaning.ui.fragment.LatelyFragment
import com.feisukj.cleaning.utils.Constant
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File

class LatelyFileActivity :AbsTabActivity(){
    private var tabFragment:List<LatelyFragment>?=null
    private var job:Job?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Constant.CAMERA
         * Constant.SCREENSHOTS_PHOTO
         * Constant.WE_VIDEO
         * Constant.WE_PATH_IMAGE
         */
        val paths= listOf(Constant.DOWNLOAD,Constant.WE_PATH_DOWN,Constant.QQ_T_FILE)

        job= GlobalScope.launch {
            FileManager.scanDirFile3(paths.map { File(it) },onNext = {
                val item=getFileBean(it)
                runOnUiThread {
                    for (f in tabFragment?:return@runOnUiThread){
                        f.addItem(item.first,item.second)
                    }
                }
                isActive
            })
            runOnUiThread {
                tabFragment?.forEach {
                    it.onComplete()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun getFileBean(file: File):Pair<FileBean,LatelyFragment.LatelyFileType>{
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
                        ?:AppBean.getAppBean(file)
            }
            else->{
                AllFileBean(file)
            }
        }
        return Pair(f,type)
    }

    override fun getTitleText(): String {
        return "最近文件"
    }

    override fun getTabFragment(): List<Pair<Fragment, String>> {
        val fs=LatelyFragment.LatelyFileType.values().map {
            Pair(LatelyFragment.getInstance(it),it)
        }
        tabFragment=fs.map { it.first }

        for (i in fs.indices){
            fs[i].first.onRemoveItem=
                    {type, collection ->
                        if (type==LatelyFragment.LatelyFileType.All){
                            fs.filter { it.second!=LatelyFragment.LatelyFileType.All }.forEach {
                                it.first.removeAllItem(collection)
                            }
                        }else{
                            fs.filter { it.second==LatelyFragment.LatelyFileType.All }.forEach {
                                it.first.removeAllItem(collection)
                            }
                        }
                    }
        }
        return fs.map { Pair(it.first,it.second.label) }
    }
}