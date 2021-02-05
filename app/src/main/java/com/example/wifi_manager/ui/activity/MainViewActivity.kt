package com.example.wifi_manager.ui.activity

import android.view.Gravity
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.provider.ModuleProvider
import com.example.module_base.utils.Constants
import com.example.module_base.utils.checkAppPermission
import com.example.module_base.utils.showToast
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityMainBinding
import com.example.wifi_manager.ui.fragment.HomeFragment
import com.example.wifi_manager.ui.fragment.MyFragment
import com.example.wifi_manager.ui.popup.ExitPoPupWindow
import com.example.wifi_manager.utils.DataProvider
import com.feisukj.cleaning.file.FileManager
import com.feisukj.cleaning.ui.fragment.CleanFragment
import java.util.*

@Route(path = ModuleProvider.ROUTE_MAIN_ACTIVITY)
class MainViewActivity : BaseViewActivity<ActivityMainBinding>() {
    override fun getLayoutView(): Int = R.layout.activity_main
    private val mHomeFragment by lazy {  HomeFragment()}
    private val mClearFragment by lazy {  CleanFragment() }
    private val mMyFragment by lazy {  MyFragment()}
    private val mExitPoPupWindow by lazy { ExitPoPupWindow(this) }
    override fun initView() {
        binding.bottomNavigationView.itemIconTintList = null;
        showFragment(mHomeFragment)
        sp. putBoolean(Constants.IS_FIRST, false)
        FileManager.start()
    }


    override fun onResume() {
        super.onResume()
        when (intent.getIntExtra(ModuleProvider.FRAGMENT_ID, 5)) {
            3->showFragment(mMyFragment)
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
        if (oldFragment === fragment) {
            return
        }
        supportFragmentManager.beginTransaction().apply {
            if (fragment.isAdded) show(fragment) else add(R.id.nav_home_frameLayout,fragment)

            oldFragment?.let {
                hide(it)
            }


            oldFragment=fragment
            commitAllowingStateLoss()
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
            if (keyCode==KeyEvent.KEYCODE_BACK){
                mExitPoPupWindow.showPopupView(binding.navHomeFrameLayout,Gravity.BOTTOM)
                return true
            }
        return super.onKeyDown(keyCode, event)
    }
}