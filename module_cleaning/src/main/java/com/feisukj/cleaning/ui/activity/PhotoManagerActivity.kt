package com.feisukj.cleaning.ui.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.example.module_base.cleanbase.BaseActivity2

import com.feisukj.cleaning.R
import com.feisukj.cleaning.ui.fragment.OtherPhotoManagerFragment
import com.feisukj.cleaning.ui.fragment.ProposalPhotoManagerFragment
import kotlinx.android.synthetic.main.act_photo_manager.*

class PhotoManagerActivity: BaseActivity2() {

    override fun getLayoutId()=R.layout.act_photo_manager

    override fun isActionBar(): Boolean = false

    override fun initView() {
        barTitle.text = resources.getText(R.string.AlbumManagement)
        immersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()
        val fragments= listOf(ProposalPhotoManagerFragment(),OtherPhotoManagerFragment())
        photoManagerViewPager.adapter=object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment {
                return if (position<fragments.size){
                    fragments[position]
                }else{
                    fragments.last()
                }
            }
            override fun getCount()=2
        }
        photoManagerTableLayout.setupWithViewPager(photoManagerViewPager)
        for (i in 0 until photoManagerTableLayout.tabCount){
            val tab=photoManagerTableLayout.getTabAt(i)
            if (i==0){
                tab?.setText(R.string.myAlbum)
            }else{
                tab?.setText(R.string.otherAlbum)
            }
        }
    }

    override fun initListener() {
        goBack.setOnClickListener {
            finish()
        }
    }
}