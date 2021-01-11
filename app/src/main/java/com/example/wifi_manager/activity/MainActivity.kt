package com.example.wifi_manager.activity

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.module_base.base.BaseActivity
import com.example.wifi_manager.R
import com.example.wifi_manager.fragment.ClearFragment
import com.example.wifi_manager.fragment.HomeFragment
import com.example.wifi_manager.fragment.MyFragment
import com.tamsiree.rxfeature.tool.RxQRCode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun getLayoutView(): Int = R.layout.activity_main
    private val mHomeFragment by lazy {  HomeFragment()}
    private val mClearFragment by lazy {  ClearFragment()}
    private val mMyFragment by lazy {  MyFragment()}
    override fun initView() {
        showFragment(mHomeFragment)
    }


    override fun initEvent() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homeFragment->showFragment(mHomeFragment)
                R.id.clearFragment->showFragment(mClearFragment)
                R.id.myFragment->showFragment(mMyFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private var oldFragment:Fragment?=null
    private fun showFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            oldFragment?.let { if (oldFragment!=fragment) hide(it) }
            if (fragment.isAdded) show(fragment) else add(R.id.nav_home_frameLayout,fragment)
            oldFragment=fragment
            commitAllowingStateLoss()
        }
    }
}