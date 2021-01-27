package com.feisukj.cleaning.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.module_base.cleanbase.BaseActivity2

import com.feisukj.cleaning.R
import com.feisukj.cleaning.file.FileType
import com.feisukj.cleaning.ui.fragment.DocFragment
import kotlinx.android.synthetic.main.act_doc_clean.*

class DocActivity : BaseActivity2(){

    override fun getStatusBarColor(): Int {
        return android.R.color.transparent
    }

    override fun isActionBar(): Boolean = false

    override fun getLayoutId()= R.layout.act_doc_clean

    override fun initView() {
        barTitle.text = resources.getText(R.string.doc)
        immersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()
        val icons= listOf(R.mipmap.ic_doc,R.mipmap.ic_pdf,
                R.mipmap.ic_ppt,R.mipmap.ic_xls,
                R.mipmap.ic_txt)
        val pagerAdapter=MyViewPageAdapter(supportFragmentManager)
        viewPage.adapter=pagerAdapter
        tableLayout.setupWithViewPager(viewPage)
        viewPage.offscreenPageLimit=5
        for (i in 0 until tableLayout.tabCount){
            val tab=tableLayout.getTabAt(i)?:break
            val view=LayoutInflater.from(this).inflate(R.layout.item_file_tab,tableLayout,false)
            if(i<icons.size){
                view.findViewById<ImageView>(R.id.tabItemIcon).setImageResource(icons[i])
            }
            view.findViewById<TextView>(R.id.tabItemTitle).text=pagerAdapter.getPageTitle(i)
            tab.customView=view
        }
//        AdController.Builder(this, ADConstants.COMMONLY_USED_PAGE_SECOND_LEVEL)
//                .setBannerContainer(bannerAd)
//                .create()
//                .show()
    }

    override fun initListener() {
        goBack.setOnClickListener {
            finish()
        }
    }

    private class MyViewPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm){
        override fun getItem(position: Int): Fragment {
            val fragment= DocFragment()
            val bundle=Bundle()
            bundle.putString(DocFragment.KEY, DocFragment.tabTitle[position].name)
            fragment.arguments=bundle
            return fragment
        }
        override fun getCount()= DocFragment.tabTitle.size
        override fun getPageTitle(position: Int): CharSequence? {
            return when (DocFragment.tabTitle[position]){
                FileType.txt->{
                    "TXT"
                }
                FileType.doc -> {
                    "DOC"
                }
                FileType.pdf -> {
                    "PDF"
                }
                FileType.ppt -> {
                    "PPT"
                }
                FileType.xls -> {
                    "XLS"
                }
                else->{
                    ""
                }
            }
        }
    }
}