package com.example.wifi_manager.ui.activity

import androidx.fragment.app.Fragment
import com.example.module_base.base.BaseViewActivity
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityMainBinding
import com.example.wifi_manager.ui.fragment.ClearFragment
import com.example.wifi_manager.ui.fragment.HomeFragment
import com.example.wifi_manager.ui.fragment.MyFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainViewActivity : BaseViewActivity<ActivityMainBinding>() {
    override fun getLayoutView(): Int = R.layout.activity_main
    private val mHomeFragment by lazy {  HomeFragment()}
    private val mClearFragment by lazy {  ClearFragment()}
    private val mMyFragment by lazy {  MyFragment()}
    override fun initView() {
        binding.bottomNavigationView.itemIconTintList = null;
        showFragment(mHomeFragment)
    }


    override fun initEvent() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
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