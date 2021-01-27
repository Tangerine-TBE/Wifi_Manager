package com.feisukj.cleaning.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MySimpViewPageAdapter(fragmentManager: FragmentManager, val fragments:List<Fragment>) : FragmentPagerAdapter(fragmentManager){
    override fun getItem(p0: Int)=fragments[p0]

    override fun getCount()=fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return super.getPageTitle(position)
    }
}