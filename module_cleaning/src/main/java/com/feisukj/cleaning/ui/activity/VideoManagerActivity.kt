package com.feisukj.cleaning.ui.activity

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentPagerAdapter
import com.example.module_base.cleanbase.BaseActivity2

import com.feisukj.cleaning.R
import com.feisukj.cleaning.ui.fragment.FileFragment
import com.feisukj.cleaning.utils.Constant
import kotlinx.android.synthetic.main.act_photo_video_manage_clean.*

class VideoManagerActivity: BaseActivity2(){

    override fun getLayoutId()= R.layout.act_photo_video_manage_clean
    override fun isActionBar(): Boolean = false
    override fun initView() {
        barTitle.text = resources.getText(R.string.VideoManagement)
        immersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()
        val titleRes= listOf(R.string.cameraVideo,R.string.DownloadedVideo,R.string.qqVideo,R.string.weChatVideo)
        val iconRes= listOf(R.mipmap.ic_camera_video,R.mipmap.ic_down_video,
                R.drawable.ic_video_qq,R.drawable.ic_video_we)
        val fragments=listOf(
                FileFragment.getInstance(listOf(Constant.CAMERA),R.string.videoManagerDes,true,Constant.VIDEO_FORMAT.toList()),
                FileFragment.getInstance(Constant.otherPicture.map{it.first},R.string.DownloadedVideo,true,Constant.VIDEO_FORMAT.toList()),
                FileFragment.getInstance(listOf(Constant.QQ_VIDEO),R.string.QQSavedVideo,true,Constant.VIDEO_FORMAT.toList()),
                FileFragment.getInstance(listOf(Constant.WE_SAVE_PIC,*Constant.WE_VIDEO.toTypedArray()),R.string.weChatSavedAndChatVideo,true,Constant.VIDEO_FORMAT.toList())
        )
        viewPage.adapter=object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int)=fragments[position]

            override fun getCount()= fragments.size
        }
        tableLayout.setupWithViewPager(viewPage)
        viewPage.offscreenPageLimit=fragments.size

        for (i in 0 until tableLayout.tabCount){
            val tab=tableLayout.getTabAt(i)?:break
            val view= LayoutInflater.from(this).inflate(R.layout.item_file_tab,tableLayout,false)
            view.findViewById<ImageView>(R.id.tabItemIcon).setImageResource(iconRes[i])
            view.findViewById<TextView>(R.id.tabItemTitle).setText(titleRes[i])
            tab.customView=view
        }

//        AdController.Builder(this, ADConstants.COMMONLY_USED_PAGE_SECOND_LEVEL)
//                .setBannerContainer(bannerAd)
//                .setContainer(frameLayoutAd)
//                .create()
//                .show()
    }

    override fun initListener() {
        goBack.setOnClickListener {
            finish()
        }
    }
}