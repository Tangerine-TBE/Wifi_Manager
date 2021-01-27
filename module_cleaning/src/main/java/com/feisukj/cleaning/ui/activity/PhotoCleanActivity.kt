package com.feisukj.cleaning.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.module_base.cleanbase.BaseActivity
import com.example.module_base.cleanbase.toast

import com.feisukj.cleaning.BuildConfig
import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.FileBean
import com.feisukj.cleaning.bean.ImageBean
import com.feisukj.cleaning.file.FileContainer
import com.feisukj.cleaning.file.FileManager
import com.feisukj.cleaning.file.FileType
import com.feisukj.cleaning.file.NextFileCallback
import com.feisukj.cleaning.view.PhotoCleanItemView_
import kotlinx.android.synthetic.main.act_photoclean_clean.*
import java.io.File
import kotlin.properties.Delegates

class PhotoCleanActivity: BaseActivity(),NextFileCallback {
    private var garbageView: PhotoCleanItemView_?=null

    private var anim = ScaleAnimation(0f,1f,1f,1f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0.5f)

    override fun getLayoutId()= R.layout.act_photoclean_clean

    override fun isActionBar(): Boolean = false

    override fun initView() {
        addGarbagePic()
        initClick()
        FileManager.addCallBack(this)

        FileContainer.cachePhotoCount.observe(this, Observer {
            textCount.text = it?.toString()
        })

        anim.apply {
            this.duration = 1500
            this.interpolator = LinearInterpolator()
            this.repeatMode = Animation.RESTART
            this.repeatCount = -1
        }
        img_anim.animation = anim

    }

    override fun onResume() {
        super.onResume()
        if (FileManager.isComplete){
            garbageView?.onCompleteUpdate(FileContainer.getPhotoFileList())
        }
    }

    //垃圾图片
    private fun addGarbagePic(){
        garbageView=PhotoCleanItemView_(linear,getString(R.string.garbagePic))
        garbageView?.setOnClickListener {
            if (!FileManager.isComplete){
                toast("正在扫描中...")
                return@setOnClickListener
            }
            val intent=Intent(this,CachePhotoActivity::class.java)
            startActivity(intent)
        }
        if (FileManager.isComplete){
            garbageView?.onCompleteUpdate(FileContainer.getPhotoFileList())
        }else{
            garbageView?.initPhoto(FileContainer.getPhotoFileList())
        }
    }

    private fun initClick(){
        back.setOnClickListener { finish() }
        clean.setOnClickListener {
            if (FileContainer.getPhotoFileList().isEmpty()){
                Toast.makeText(this,R.string.noPicClean,Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val thread=Thread{
                kotlin.run {
                    FileContainer.getPhotoFileList().forEach {
                        val file=File(it.absolutePath)
                        if (file.exists()&&!BuildConfig.DEBUG){
                            file.delete()
                        }
                    }
                    runOnUiThread {
                        FileContainer.clearPhotoFile()
                        garbageView?.onCompleteUpdate(emptyList())
                    }
                }
            }
            AlertDialog.Builder(this)
                    .setTitle(R.string.tips)
                    .setMessage(String.format(resources.getString(R.string.askDelete),FileContainer.getPhotoFileList().size.toString()))
                    .setNegativeButton(R.string.no) { _, _ ->  }
                    .setPositiveButton(R.string.yes) { _, _ ->
                        try {
                            thread.start()
                            val intent=Intent(this, CompleteActivity::class.java)
                            intent.putExtra(CompleteActivity.IMAGE_COUNT_KEY, FileContainer.getPhotoFileList().size)
                            startActivity(intent)
                            finish()
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    .show()
        }
        garbageView?.setAllChooseClick {
            it.isSelected=!it.isSelected
            if (it.isSelected) {
                bottomButton.visibility=View.VISIBLE
                clean.text = String.format(resources.getString(R.string.deletePicC),FileContainer.cachePhotoCount.value.toString())
            }else{
                bottomButton.visibility=View.GONE
            }
        }
    }

    override fun onNextFile(type: FileType,fileBean: FileBean) {
        if(type!=FileType.garbageImage){
            return
        }
        runOnUiThread {
            if (fileBean is ImageBean) {
                garbageView?.onNextPhoto(fileBean)
            }
        }
    }

    override fun onComplete() {
        if (garbageView?.getAllChooseState()==true) {
            bottomButton.visibility=View.VISIBLE
            clean.text = String.format(resources.getString(R.string.deletePicC),FileContainer.cachePhotoCount.value.toString())
        }else{
            bottomButton.visibility=View.GONE
        }
        garbageView?.onCompleteUpdate(FileContainer.getPhotoFileList())
    }

    override fun onDestroy() {
        super.onDestroy()
        FileManager.removeCallBack(this)
    }
}