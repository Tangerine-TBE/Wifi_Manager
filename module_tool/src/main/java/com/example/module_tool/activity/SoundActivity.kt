package com.example.module_tool.activity

import android.annotation.SuppressLint
import com.example.module_base.utils.MyStatusBarUtil

import com.example.module_tool.R
import com.example.module_tool.base.BaseActivity
import com.example.module_tool.utils.ColorUtil
import com.example.module_tool.widget.Decibel
import com.example.module_tool.widget.MyRecorder
import kotlinx.android.synthetic.main.activity_sound_cjy.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

/**
 * Author : Gupingping
 * Date : 2019/3/12
 * QQ : 464955343
 */
class SoundActivity : BaseActivity() {

    private  var recorder: MyRecorder?=null

    private var flag = true


    override fun getLayoutId(): Int = R.layout.activity_sound_cjy

    @SuppressLint("SetTextI18n")
    override fun initView() {
        MyStatusBarUtil.setColor(this, ColorUtil.Theme)
        sound_toolbar?.setTitle("分贝仪")
        val cacheDir=externalCacheDir
        if (cacheDir==null||!cacheDir.exists()){
            return
        }

        val file=File(cacheDir,"temp.amr")
        if (file.exists()){
            file.delete()
        }
        file.createNewFile()
        recorder = MyRecorder(file)


        sound_toolbar?.finishActivity(this)
    }

    override fun isActionBar(): Boolean = false

    override fun onResume() {
        super.onResume()
        recorder?.let {
        if (it.startRecording()) {
            flag = true
            try {
                GlobalScope.launch {
                    while (flag) {
                        val volume = it.getMaxAmplitude()
//                    lg("launch volume==$volume")
                        if (volume in 1..1000000) {
                            Decibel.dbCount = Math.log10(volume.toDouble()).toFloat() * 20//转换音频为分贝
                        }
                        runOnUiThread {
                            val s=String.format(getString(R.string.decibel),Decibel.dbCount.toInt().toString())
                            dbTv?.text = s
//                        dbView.setDegree(Decibel.dbCount.toInt())
                            dbView3?.setDegree(Decibel.dbCount.toInt())
                        }
                        delay(100)
                    }
                }
            }catch (e:Exception){

            }
        }
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            flag = false
            recorder?.deleteRecordingFile()
        } catch (e: Exception) {

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            recorder?.deleteRecordingFile()
        } catch (e: Exception) {

        }
    }
}