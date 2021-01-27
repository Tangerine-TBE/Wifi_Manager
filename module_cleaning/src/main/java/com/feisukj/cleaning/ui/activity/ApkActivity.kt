package com.feisukj.cleaning.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.feisukj.cleaning.R
import com.feisukj.cleaning.ui.fragment.ApkFragment
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_apk_clean.*

class ApkActivity : FragmentActivity(R.layout.activity_apk_clean) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barTitle.text="安装包管理"
        goBack.setOnClickListener { finish() }
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .statusBarColor(android.R.color.transparent)
                .init()
        val f= listOf(ApkFragment.getInstance(true), ApkFragment.getInstance(false))
        apkViewPager.adapter=object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(p0: Int): Fragment {
                return f[p0]
            }
            override fun getCount(): Int {
                return f.size
            }
        }
        apkTabLayout.setupWithViewPager(apkViewPager)
        val t= listOf("已安装","未安装")
        for (i in 0 until apkTabLayout.tabCount){
            val tab=apkTabLayout.getTabAt(i)?:continue
            val tabView= LayoutInflater.from(this).inflate(R.layout.item_tab_clean,apkTabLayout,false)
            tab.customView=tabView
            tabView?.findViewById<TextView>(R.id.tabItemTitle)?.text=t[i]
        }
    }
}