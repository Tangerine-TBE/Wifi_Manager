package com.feisukj.cleaning.ui.activity

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.module_base.cleanbase.BaseActivity2

import com.feisukj.cleaning.R
import com.feisukj.cleaning.ui.fragment.FileFragment
import com.feisukj.cleaning.utils.Constant
import kotlinx.android.synthetic.main.act_qq_and_we_file_clean.*

/**
 * 深度清理中的QQ或微信页面
 */
class QQAndWeFileActivity : BaseActivity2(){
    companion object{
        const val KEY="key"
        const val QQ="qq"
        const val WE="we"
    }

    private var flag= QQ
    override fun getLayoutId()= R.layout.act_qq_and_we_file_clean

    override fun isActionBar(): Boolean = false

    override fun initView() {
        flag=intent.getStringExtra(KEY)?:QQ
        immersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()
        val f:List<Fragment>
        val titleRes:List<Int>
        val iconRes:List<Int>
        if (flag==QQ){
            barTitle.text = resources.getText(R.string.qqFile)
            tableLayout.tabMode= TabLayout.MODE_FIXED
            titleRes= listOf(R.string.picture,R.string.video,R.string.file,R.string.voice)
            iconRes= listOf(R.drawable.selector_depth_picture,R.drawable.selector_depth_video,
                    R.drawable.selector_depth_file,R.drawable.selector_depth_voice)
            f=listOf(
                    FileFragment.getInstance(listOf(Constant.CHAT_IMG),R.string.ChatPictures,true),
                    FileFragment.getInstance(listOf(Constant.QQ_VIDEO),R.string.videoDes,true),
                    FileFragment.getInstance(listOf(Constant.QQ_T_FILE,Constant.QQ_T_FILE),R.string.fileDes),
                    FileFragment.getInstance(Constant.QQ_YUYIN,R.string.voiceDes)
            )
        }else{
            barTitle.text = resources.getText(R.string.weChatFile)
            titleRes= listOf(R.string.picture,R.string.CircleFriends,R.string.emoji,R.string.video,R.string.file,R.string.voice,R.string.picOrVideo)
            iconRes= listOf(R.drawable.selector_depth_face,R.drawable.selector_depth_sns,
                    R.drawable.selector_depth_face,R.drawable.selector_depth_video,
                    R.drawable.selector_depth_file,R.drawable.selector_depth_voice)
            f=listOf(
                    FileFragment.getInstance(Constant.WE_CHAT_PIC,R.string.ChatPictures,true),
                    FileFragment.getInstance(Constant.WE_FRIEND,R.string.circleFriendsDes,true),
                    FileFragment.getInstance(Constant.WE_ENOJI,R.string.emojiDes,true),
                    FileFragment.getInstance(Constant.WE_VIDEO,R.string.videoDes,true,Constant.VIDEO_FORMAT.toList()),
                    FileFragment.getInstance(listOf(Constant.WE_FILE,Constant.WE_FILE_DATA),R.string.fileDes),
                    FileFragment.getInstance(Constant.WE_YUYIN,R.string.voiceDes)
//                    FileFragment.getInstance(listOf(Constant.WE_SAVE_PIC),R.string.SavedPicturesOrVideos,true)
            )
        }
        viewPage.adapter=object :FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int)=f[position]

            override fun getCount()= f.size
        }
        tableLayout.setupWithViewPager(viewPage)
        viewPage.offscreenPageLimit=f.size

        for (i in 0 until tableLayout.tabCount){
            val tab=tableLayout.getTabAt(i)?:break
            val view= LayoutInflater.from(this).inflate(R.layout.item_file_tab,tableLayout,false)
            view.findViewById<ImageView>(R.id.tabItemIcon).setImageResource(iconRes[i])
            view.findViewById<TextView>(R.id.tabItemTitle).setText(titleRes[i])
            tab.customView=view
        }
    }

    override fun initListener() {
        goBack.setOnClickListener {
            finish()
        }
    }
}