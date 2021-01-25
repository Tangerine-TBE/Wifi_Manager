package com.example.wifi_manager.ui.activity

import androidx.fragment.app.Fragment
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.utils.Constants
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.calLastedTime
import com.example.module_base.utils.showToast
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityMainBinding
import com.example.wifi_manager.ui.fragment.ClearFragment
import com.example.wifi_manager.ui.fragment.HomeFragment
import com.example.wifi_manager.ui.fragment.MyFragment
import com.example.wifi_manager.utils.ConstantsUtil
import com.example.wifi_manager.utils.WifiUtils
import com.tamsiree.rxkit.view.RxToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class MainViewActivity : BaseViewActivity<ActivityMainBinding>() {
    override fun getLayoutView(): Int = R.layout.activity_main
    private val mHomeFragment by lazy {  HomeFragment()}
    private val mClearFragment by lazy {  ClearFragment()}
    private val mMyFragment by lazy {  MyFragment()}
    override fun initView() {
        binding.bottomNavigationView.itemIconTintList = null;
        showFragment(mHomeFragment)
        sp.apply {
            //陪伴天数计数
            val isFirst = getBoolean(Constants.IS_FIRST, true)
            if (isFirst) {
                putLong(Constants.FIRST_TIME,System.currentTimeMillis())
                putBoolean(Constants.IS_FIRST, false)
            }
        }
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