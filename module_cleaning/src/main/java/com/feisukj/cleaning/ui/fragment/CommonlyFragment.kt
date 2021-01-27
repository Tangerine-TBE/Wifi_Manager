package com.feisukj.cleaning.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.module_base.cleanbase.BaseConstant


import com.feisukj.cleaning.R
import com.feisukj.cleaning.ui.activity.*

import kotlinx.android.synthetic.main.fragment_commonly_clean.*

class CommonlyFragment:Fragment(R.layout.fragment_commonly_clean) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListener()
        activity?.let {

        }
    }

    private fun initListener(){
        albumManager.setOnClickListener {
           // MobclickAgent.onEvent(context,"20000-xiangce-click")
            startActivity(Intent(context, PhotoManagerActivity::class.java))
        }
        videoManager.setOnClickListener {
       //     MobclickAgent.onEvent(context,"200001-ship-click")
            val intent=Intent(context, VideoManagerActivity::class.java)
            startActivity(intent)
        }
        musicManager.setOnClickListener {
           // MobclickAgent.onEvent(context,"200002-yinyue-click")
            val intent=Intent(context, MusicActivity::class.java)
            startActivity(intent)
        }
        docManager.setOnClickListener {
          //  MobclickAgent.onEvent(context,"200003-wendang-click")
            val intent=Intent(context, DocActivity::class.java)
            startActivity(intent)
        }
        if (BaseConstant.channel=="_oppo"){
            appManager.visibility= View.GONE
        }
        appManager.setOnClickListener {
          //  MobclickAgent.onEvent(context,"200004-yingyong-click")
            val intent=Intent(context, AppActivity2::class.java)
            startActivity(intent)
        }
        apkManager.setOnClickListener {
         //   MobclickAgent.onEvent(context,"200005-anzhuangbao-click")
            val intent=Intent(context,ApkActivity::class.java)
            startActivity(intent)
        }
        wechatManager.setOnClickListener {
          //  MobclickAgent.onEvent(context,"200007-weixin-click")
            val intent=Intent(context,QQAndWeFileActivity::class.java)
            intent.putExtra(QQAndWeFileActivity.KEY,QQAndWeFileActivity.WE)
            startActivity(intent)
        }
        qqManager.setOnClickListener {
            //MobclickAgent.onEvent(context,"200008-qq-click")
            val intent=Intent(context,QQAndWeFileActivity::class.java)
            intent.putExtra(QQAndWeFileActivity.KEY,QQAndWeFileActivity.QQ)
            startActivity(intent)
        }
        latelyManager.setOnClickListener {
          //  MobclickAgent.onEvent(context,"200009-zuijin-click")
            startActivity(Intent(context,LatelyFileActivity::class.java))
        }
        bigManager.setOnClickListener {
          //  MobclickAgent.onEvent(context,"200010-dawenjian-click")
            startActivity(Intent(context,BigFileActivity::class.java))
        }
    }
}