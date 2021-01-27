package com.feisukj.cleaning.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.feisukj.cleaning.R
import com.google.android.material.tabs.TabLayout
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_abs_tab_clean.*

abstract class AbsTabActivity :FragmentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abs_tab_clean)
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .statusBarColor(android.R.color.transparent)
                .init()
        goBack.setOnClickListener { finish() }
        titleText.text=getTitleText()

        val f=getTabFragment()
        tabViewPager.adapter=object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(p0: Int): Fragment {
                return f[p0].first
            }
            override fun getCount(): Int {
                return f.size
            }
        }
        tabViewPager.offscreenPageLimit = f.size
        tabTabLayout.setupWithViewPager(tabViewPager)
        if (f.size>=5){
            tabTabLayout.tabMode= TabLayout.MODE_SCROLLABLE
        }
        for (i in 0 until tabTabLayout.tabCount){
            val tab=tabTabLayout.getTabAt(i)?:continue
            val tabView= LayoutInflater.from(this).inflate(R.layout.item_tab_clean,tabTabLayout,false)
            tab.customView=tabView
            tabView?.findViewById<TextView>(R.id.tabItemTitle)?.text=f[i].second
        }
    }

    protected abstract fun getTitleText():String

    protected abstract fun getTabFragment():List<Pair<Fragment,String>>
}