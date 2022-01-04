package com.feisukj.cleaning.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.text.format.Formatter
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import android.widget.ViewSwitcher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.example.module_base.cleanbase.BaseConstant
import com.example.module_base.cleanbase.SectionData

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.*
import com.feisukj.cleaning.file.*
import com.feisukj.cleaning.file.FileType
import com.feisukj.cleaning.utils.Constant
import com.feisukj.cleaning.utils.formatFileSize
import com.feisukj.cleaning.utils.getNotUsedAppSize
import com.feisukj.cleaning.utils.lastChildView
import com.feisukj.cleaning.view.SmallLoading
import com.gyf.immersionbar.ImmersionBar

import kotlinx.android.synthetic.main.activity_phone_lose.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Thread.sleep

class PhoneLoseActivity :FragmentActivity(R.layout.activity_phone_lose), NextFileCallback {
    companion object{
        var unloadingData:List<SectionData<TitleBean_Group, AllFileBean>>?=null
    }

    private var job:Job?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .init()

        val statFs= StatFs(Environment.getExternalStorageDirectory().absolutePath)
        val total=statFs.totalBytes
        val available=statFs.availableBytes
        storageSurplusValue.text=Formatter.formatFileSize(this,available)
        storageTotalValue.text=Formatter.formatFileSize(this,total)
        ((total-available).toFloat()/total).also {
            progressText.text=(it*100+0.5).toInt().toString()+"%"
            progressStorage.progressValue=it
            when{
                it in 0f .. 0.8f->{
                    storageTip.text="手机存储空间充足"
                }
                it in 0.8f .. 0.9f->{
                    storageTip.text="手机存储空间紧张"
                }
                it>0.9f->{
                    storageTip.text="手机存储空间紧缺"
                }
            }
        }
        initListener()
        initView()

        job=GlobalScope.launch {
            //相似文件
            if (!RepetitionFileManager.isComplete){
                RepetitionFileManager.findSimilarFile()
            }
            if (!isActive){
                return@launch
            }
            if (RepetitionFileManager.isComplete){
                runOnUiThread {
                    repetitionComplete()
                }
            }

            //卸载残留
            var unloadingResultSize=0L
            val fileList=ArrayList<File>()
            val data=ArrayList<SectionData<TitleBean_Group, AllFileBean>>()
            PathManager.getUnloadingResiduePath().groupBy { it.packageName }.entries.forEach { entry ->
                val item=SectionData<TitleBean_Group, AllFileBean>()
                val t=TitleBean_Group().apply {
                    this.title=entry.value.firstOrNull()?.appName?:""
                }
                item.groupData=t
                FileManager.scanDirFile3(entry.value.map { File(it.path) },onNext = {
                    unloadingResultSize+=it.length()
                    t.itemSize+=it.length()
                    item.addItem(AllFileBean(it))
                    fileList.add(it)
                    isActive
                })
                data.add(item)
            }
            unloadingData=data.filter {
                val l=it.groupData?.itemSize
                l!=null&&l!=0L
            }.sortedBy { -(it.groupData?.itemSize?:0) }
//            PathManager.getUnloadingResiduePath().forEach { applicationGarbage ->
//                FileManager.scanDirFile2(File(applicationGarbage.path),onNext = {
//                    unloadingResultSize+=it.length()
//                    fileList.add(it)
//                    isActive
//                })
//            }
            fileList.sortBy {
                -it.length()
            }
            if (!isActive){
                return@launch
            }
            runOnUiThread{
                loadingViewSwitch(false,unloadingResidueViewSwitcher?:return@runOnUiThread)
                unloadingResidueGroup?.visibility=View.VISIBLE
                unloadingResidueTotalSize?.text=formatFileSize(this@PhoneLoseActivity,unloadingResultSize).let {
                    ((it.first.toFloatOrNull()?:0f)+0.5).toInt().toString()+it.second
                }
                unloadingResidueCount?.text=fileList.size.toString()

                val tv= listOf(unloadingFile1,unloadingFile2,unloadingFile3)
                val icons= arrayOf(unloadingFileIcon1,unloadingFileIcon2,unloadingFileIcon3)
                fileList.take(3).forEachIndexed { index, file ->
                    if (index<fileList.size) {
                        tv[index]?.text = formatFileSize(this@PhoneLoseActivity,file.length()).let {
                            ((it.first.toFloatOrNull()?:0f)+0.5).toInt().toString()+it.second
                        }
                    }else{
                        tv[index]?.visibility=View.GONE
                    }

                    if (Constant.PICTURE_FORMAT.any { file.name.endsWith(it,true) }){
                        Glide.with(this@PhoneLoseActivity).load(file).into(icons[index])
                    }else if (Constant.VIDEO_FORMAT.any { file.name.endsWith(it,true) }){
                        Glide.with(this@PhoneLoseActivity).load(file).into(icons[index])
                    }else{
                        Glide.with(this@PhoneLoseActivity).load(AllFileBean(file).fileIcon).into(icons[index])
                    }
                }
            }

            //不常用软件
            val totalSize=getNotUsedAppSize(this@PhoneLoseActivity) {isActive}
            runOnUiThread {
                loadingViewSwitch(false,appViewSwitch)
                if (totalSize==null){
                    if (appTotalSizeSwitch.currentView!=goApp){
                        appTotalSizeSwitch.showNext()
                    }
                }else{
                    if (appTotalSizeSwitch.currentView!=appTotalSize){
                        appTotalSizeSwitch.showNext()
                    }
                    appTotalSize.text=Formatter.formatFileSize(this@PhoneLoseActivity,totalSize)
                }
            }
        }

        FileManager.addCallBack(this)
        aaa()

        if (BaseConstant.channel == "_oppo"){
            appItem.visibility = View.GONE
        }else{
            appItem.visibility = View.VISIBLE
        }
    }

    private fun aaa(){
        GlobalScope.launch {
            if (RepetitionFileManager.isComplete){
                runOnUiThread {
                    repetitionComplete()
                }
            }else{
                aaa()
            }
            sleep(3000)
        }
    }

    private fun initView(){
        loadingViewSwitch(true,bigFileTotalViewSwitcher)
        loadingViewSwitch(true,unloadingResidueViewSwitcher)
        loadingViewSwitch(true,repetitionFileViewSwitcher)
        loadingViewSwitch(true,musicViewSwitch)
        loadingViewSwitch(true,appViewSwitch)
    }

    private fun initListener(){
        goBack.setOnClickListener { finish() }
        bigFileItem.setOnClickListener {
            if (!FileManager.isComplete){
                Toast.makeText(this,"扫描中...",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //MobclickAgent.onEvent(this,"200010-dawenjian-click")
            startActivity(Intent(this,BigFileActivity::class.java))
        }

        unloadingResidueItem.setOnClickListener {
            if (unloadingResidueViewSwitcher.currentView is SmallLoading){
                Toast.makeText(this,"扫描中...",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(Intent(this,UnloadingResidueActivity::class.java))
        }
        repetitionFileItem.setOnClickListener {
            if (!RepetitionFileManager.isComplete){
                Toast.makeText(this,"扫描中...",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(Intent(this,RepetitionFileActivity::class.java))
        }
        musicItem.setOnClickListener {
            if(!FileManager.isComplete){
                Toast.makeText(this,"扫描中...",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        //    MobclickAgent.onEvent(this,"200002-yinyue-click")
            val intent=Intent(this,MusicActivity::class.java)
            startActivity(intent)
        }
        appItem.setOnClickListener {
       //     MobclickAgent.onEvent(this,"200004-yingyong-click")
            val intent=Intent(this,AppActivity2::class.java)
            startActivity(intent)
        }
        pictureItem.setOnClickListener {
            val intent=Intent(this,CachePhotoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadingViewSwitch(isSwitch:Boolean,switchView: ViewSwitcher){
        val currentView=switchView.currentView
        if (isSwitch){
            if (currentView is SmallLoading){
                currentView
            }else{
                switchView.showNext()
                switchView.currentView as? SmallLoading
            }?.startAnim()
        }else{
            if (currentView is SmallLoading){
                switchView.showNext()
                currentView
            }else{
                switchView.nextView as? SmallLoading
            }?.stopAnim()
        }
    }

    private fun repetitionComplete(){
        loadingViewSwitch(false,repetitionFileViewSwitcher)
        repetitionFileTotalSize.text=Formatter.formatFileSize(this,RepetitionFileManager.repetitionFileSize)
        repetitionFileCount.text=RepetitionFileManager.repetitionFileCount.toString()
    }

    override fun onNextFile(type: FileType, fileBean: FileBean) {
//        if(type!=FileType.garbageImage||fileBean!is ImageBean){
//            return
//        }
    }

    override fun onComplete() {
        loadingViewSwitch(false,bigFileTotalViewSwitcher)
        loadingViewSwitch(false,musicViewSwitch)
        bigFileGroup.visibility= View.VISIBLE

        val sizeToText={ p:Pair<String,String>->
            (p.first.toFloat()+0.5f).toInt().toString()+p.second
        }

        val bigFile=FileContainer.getBigFileList()
        bigFileCount.text=bigFile.size.toString()
        bigFileTotalSize.text=Formatter.formatFileSize(this,bigFile.map { it.fileSize }.sum())

        val fileSizeView= arrayOf(bigFile1Size,bigFile2Size,bigFile3Size)
        val fileIconView= arrayOf(bigFileIcon1,bigFileIcon2,bigFileIcon3)

        val tempBigFile=ArrayList(bigFile)
        val max1=tempBigFile.maxByOrNull { it.fileSize }
        tempBigFile.remove(max1)
        val max2=tempBigFile.maxByOrNull { it.fileSize }
        tempBigFile.remove(max2)
        val max3=tempBigFile.maxByOrNull { it.fileSize }
        tempBigFile.remove(max3)
        arrayOf(max1,max2,max3).forEachIndexed { index, allFileBean ->
            if (allFileBean==null){
                return@forEachIndexed
            }
            fileSizeView[index].text=sizeToText(formatFileSize(this,allFileBean.fileSize))
            when(allFileBean){
                is AllFileBean->{
                    Glide.with(this).load(allFileBean.fileIcon).into(fileIconView[index])
                }
                is ImageBean ->{
                    Glide.with(this).load(allFileBean.absolutePath).into(fileIconView[index])
                }
                is AppBean->{
                    Glide.with(this).load(allFileBean.icon).into(fileIconView[index])
                }
            }
        }

        val musicFile=MusicActivity.musicData.toList()
        musicCount.text=musicFile.size.toString()
        musicTotalSize.text=Formatter.formatFileSize(this,musicFile.map { it.fileSize }.sum())


        if (FileContainer.getPhotoFileList().isNotEmpty()){
            pictureItem?.visibility=View.VISIBLE
            pictureCount?.text=FileContainer.getPhotoFileList().size.toString()
        }
        val lastView=previewPicture?.lastChildView()
        if (lastView?.visibility==View.GONE){
            runOnUiThread {
                val c= FileContainer.getPhotoFileList().take(4)
                for (i in c.indices){
                    val view=previewPicture.getChildAt(i)
                    view.visibility=View.VISIBLE
                    if (view is ImageView){
                        Glide
                                .with(view.context)
                                .load(c[i].absolutePath)
                                .into(view)
                    }
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        FileManager.removeCallBack(this)
    }
}