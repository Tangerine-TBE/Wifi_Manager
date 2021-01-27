package com.feisukj.cleaning.ui.fragment

import android.content.Intent
import com.bumptech.glide.Glide
import com.example.module_base.cleanbase.BaseFragment2
import com.example.module_base.cleanbase.SectionData
import com.example.module_base.cleanbase.toast


import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.ImageBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.file.DirNextFileCallback
import com.feisukj.cleaning.file.FileManager
import com.feisukj.cleaning.ui.activity.PhotoActivity
import com.feisukj.cleaning.ui.activity.SimilarPhotoActivity
import com.feisukj.cleaning.utils.*
import kotlinx.android.synthetic.main.frag_proposal_photo_manager.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class ProposalPhotoManagerFragment: BaseFragment2() {
    companion object{
        val similarPhotoData=ArrayList<SectionData<TitleBean_Group, ImageBean>>()
        private var findSimilarPhotoEnd=false
    }

    override fun getLayoutId()= R.layout.frag_proposal_photo_manager

    override fun initView() {
        GlobalScope.launch {
            initScreenshot()
            initSimilarPhoto()
        }
        activity?.let {

        }
    }

    override fun initListener() {
        similarPhotoItem.setOnClickListener {
            if (!findSimilarPhotoEnd){
                toast("正在扫描中...")
                return@setOnClickListener
            }
            startActivity(Intent(context,SimilarPhotoActivity::class.java))
        }
        screenshotPhotoItem.setOnClickListener {
            val intent=Intent(context, PhotoActivity::class.java)
            intent.putExtra(PhotoActivity.PATH_KEY, Constant.SCREENSHOTS_PHOTO)
            intent.putExtra(PhotoActivity.TITLE_RES_KEY,R.string.screenshot)
            startActivity(intent)
        }
    }

    private fun initSimilarPhoto(){
        if (!findSimilarPhotoEnd){
            val photos= PhotoRepository.getPhoto(context)
            SimilarPhoto.find(context,photos).forEach { group ->
                val similar=group.photos
                if (similar!=null) {
                    val sectionData=SectionData<TitleBean_Group,ImageBean>().apply {
                        val minDate=similar.minBy {
                            it.year*12+it.month
                        }?:return@apply
                        val maxDate=similar.maxBy {
                            it.year*12+it.month
                        }?:return@apply
                        val date=if (minDate==maxDate){
                            if (language== Language.zh_CN){
                                "${minDate.year}年${minDate.month + 1}月"
                            }else{
                                "${(MonthIndexMap.values().find { it.month==minDate.month }?:return@apply).name} ${minDate.year}"
                            }
                        }else{
                            if (language== Language.zh_CN){
                                "${minDate.year}年${minDate.month + 1}月--${maxDate.year}年${maxDate.month + 1}月"
                            }else{
                                "${(MonthIndexMap.values().find { it.month==minDate.month }?:return@apply).name} ${minDate.year}" +
                                        " to ${(MonthIndexMap.values().find { it.month==maxDate.month }?:return@apply).name} ${maxDate.year}"
                            }
                        }
                        this.groupData=TitleBean_Group().also {
                            it.title=date
                        }
                        this.addAllItem(similar)
                        this.id=minDate.year*12+minDate.month
                    }
                    activity?.runOnUiThread {
                        similarPhotoData.add(sectionData)
                    }
                }
            }
            findSimilarPhotoEnd=true
        }
        activity?.runOnUiThread {
            val similarSize=similarPhotoData.map { it.getItemData().map { it.fileSize }.sum() }.sum()
            similarPhotoItemSize.text=getSizeString(similarSize)
            val imageBeans=ArrayList<ImageBean>()
            similarPhotoData.map { it.getItemData() }.forEach {
                imageBeans.addAll(it)
            }
            for (i in 0 until if (imageBeans.size>=4) 4 else imageBeans.size){
                val image=when(i){
                    0->{
                        similarPhotoItemImage1
                    }
                    1->{
                        similarPhotoItemImage2
                    }
                    2->{
                        similarPhotoItemImage3
                    }
                    3->{
                        similarPhotoItemImage4
                    }
                    else->{
                        null
                    }
                }
                if (image!=null){
                    Glide.with(this).load(imageBeans[i].absolutePath).into(image)
                }
            }
        }
    }

    private fun initScreenshot(){
        var i=0
        val f=FileManager.scanDirFile(File(Constant.SCREENSHOTS_PHOTO),{this},{Constant.PICTURE_FORMAT.any { this.absolutePath.endsWith(it) }},object :DirNextFileCallback<File>{
            override fun onNextFile(item: File) {
                activity?.runOnUiThread {
                    when(i){
                        0->{
                            Glide.with(this@ProposalPhotoManagerFragment).load(item).into(screenshotPhotoItemImage1)
                        }
                        1->{
                            Glide.with(this@ProposalPhotoManagerFragment).load(item).into(screenshotPhotoItemImage2)
                        }
                        2->{
                            Glide.with(this@ProposalPhotoManagerFragment).load(item).into(screenshotPhotoItemImage3)
                        }
                        3->{
                            Glide.with(this@ProposalPhotoManagerFragment).load(item).into(screenshotPhotoItemImage4)
                        }
//                    else->{}
                    }
                    i++
                }
            }
        })
        activity?.runOnUiThread {
            screenshotPhotoItemSize.text= getSizeString(f.map { it.length() }.sum())
        }
    }
}