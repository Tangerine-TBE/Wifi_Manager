package com.feisukj.cleaning.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.Formatter
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.module_ad.advertisement.AdType
import com.example.module_ad.advertisement.BannerHelper
import com.example.module_ad.advertisement.FeedHelper
import com.example.module_ad.advertisement.InsertHelper
import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.AppBean
import com.feisukj.cleaning.ui.fragment.*
import com.feisukj.cleaning.viewmodel.AppViewModel
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_app_clean.*
import kotlinx.android.synthetic.main.activity_cooling_complete.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AppActivity2 : FragmentActivity(R.layout.activity_app_clean){
    private val viewModel by lazy { ViewModelProvider(this).get(AppViewModel::class.java) }
    val fragments= listOf(NameSortAppFragment(),FrequencySortAppFragment(), DataSortAppFragment(),UseSpaceSortAppFragment(), SizeSortAppFragment())
    val t= listOf("按名称","按频率","按日期","按占用空间","按软件大小")
    private var job: Job?=null
    private val  unInstallAppReceiver =object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action=intent?.action?:return
            val packageName=intent.data?.schemeSpecificPart?:return

            if (action == Intent.ACTION_PACKAGE_ADDED){
                val appBean=AppBean.getAppBean(context ?: return, packageName) ?: return
                fragments.forEach { it.onPackageAdd(appBean) }
                viewModel.listData.add(appBean)
            }else if (action == Intent.ACTION_PACKAGE_REMOVED){
                // 卸载成功
                fragments.forEach { it.onPackageRemove(packageName) }
                viewModel.listData.remove(viewModel.listData.find { it.packageName==packageName })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var appCount=0
        var appTotalSize=0L
        job=GlobalScope.launch {
            viewModel.doScanAllApp(this@AppActivity2) {appBean->
                appTotalSize+=appBean.totalSize
                runOnUiThread {
                    appCount++
                    occupyStorageValue.text=Formatter.formatFileSize(this@AppActivity2,appTotalSize)
                    totalApp.text=appCount.toString()
                    fragments.forEach {
                        it.onPackageAdd(appBean)
                    }
                }
            }
        }
        barTitle.text="应用管理"
        goBack.setOnClickListener { finish() }
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .statusBarColor(android.R.color.transparent)
                .init()
        appViewPager.adapter=object : FragmentPagerAdapter(supportFragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
            override fun getItem(p0: Int): Fragment {
                return fragments[p0]
            }

            override fun getCount(): Int {
                return fragments.size
            }

        }
        appTabLayout.setupWithViewPager(appViewPager)
        for (i in 0 until appTabLayout.tabCount){
            val tab=appTabLayout.getTabAt(i)?:continue
            val tabView=LayoutInflater.from(this).inflate(R.layout.item_app_tab_clean,appTabLayout,false)
            tab.customView=tabView
            tabView?.findViewById<TextView>(R.id.tabItemTitle)?.text=t[i]
        }

        search.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?:return
                viewModel.doSearch(s.toString())
            }

        })
//        actionMenu.visibility= View.VISIBLE
//        actionMenu.setOnClickListener {
//            currentFragment?.clickMenu(it)
//
//            if (p0!=0){
//                actionMenu.visibility=View.GONE
//            }else{
//                actionMenu.visibility=View.VISIBLE
//            }
//        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addDataScheme("package")
        registerReceiver(unInstallAppReceiver,intentFilter)



        insertHelper.showAd(AdType.SOFTWARE_MANAGEMENT_PAGE)
        bannerHelper.showAd(AdType.CHARGE_PAGE)
    }



    private val insertHelper by lazy {
        InsertHelper(this)
    }


    private val bannerHelper by lazy {
        BannerHelper(this,top_ad)
    }


    override fun onDestroy() {
        super.onDestroy()
        insertHelper.releaseAd()
        unregisterReceiver(unInstallAppReceiver)
        job?.cancel()
    }
}