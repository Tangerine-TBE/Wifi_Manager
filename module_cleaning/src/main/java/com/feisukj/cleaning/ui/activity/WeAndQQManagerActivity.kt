package com.feisukj.cleaning.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.widget.TextView
import com.example.module_base.cleanbase.BaseActivity
import com.example.module_base.cleanbase.UserActivityManager

import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.MySimpViewPageAdapter
import com.feisukj.cleaning.ui.fragment.WeAndQQPicFragment
import kotlinx.android.synthetic.main.act_we_and_qq_manager_clean.*
import kotlinx.android.synthetic.main.bar_clean.*

class WeAndQQManagerActivity : BaseActivity(){
    companion object{
        const val KEY="key"//通过该key将WeChatAndQQCleanActivity中的weSet或qqSet中的item传递过来
    }
    private var flag:Int=0//用于接收通过KEY传过来的值
    private var currentItem=0
    private var count=ArrayList<Pair<Int,Int>>()
    private val preAct:WeChatAndQQCleanActivity? by lazy {
        UserActivityManager.getInstance().mActivityStack.find { it is WeChatAndQQCleanActivity } as? WeChatAndQQCleanActivity
    }

    override fun getStatusBarColor(): Int {
        return Color.TRANSPARENT
    }

    override fun getLayoutId()= R.layout.act_we_and_qq_manager_clean

    override fun initView() {
        if (preAct==null)
            return
        flag=intent.getIntExtra(KEY,0)
        val fragments:List<Fragment>
        val titles:List<String>
        val set=if (flag in preAct!!.qqSet){
            barTitle.setText(R.string.qqManager)
            preAct!!.qqSet
        } else {
            barTitle.setText(R.string.weChatManager)
            preAct!!.weSet
        }
        fragments=set.map {
            val fragment=WeAndQQPicFragment()
            val bundle=Bundle()
            bundle.putInt(WeAndQQPicFragment.KEY,it)
            fragment.arguments=bundle
            fragment
        }
        set.map {
            Pair(it,(preAct!!.map[it]?.fileList?.size?:0))
        }.let {
            count.addAll(it)
        }
        titles=set.map {
            preAct!!.map[it]!!.title
        }
        val iterator=set.iterator()
        while (iterator.hasNext()){
            if (flag!=iterator.next()){
                currentItem++
            }else{
                break
            }
        }

        viewPage.adapter=MySimpViewPageAdapter(supportFragmentManager,fragments)
        tableLayout.setupWithViewPager(viewPage)
        tableLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"))
        addTab(titles,count)

        viewPage.currentItem=currentItem

        viewPage.offscreenPageLimit=3
        initClick()
    }

    override fun isStatusBarDarkFont(): Boolean {
        return true
    }

    @SuppressLint("UseSparseArrays")
    private val countView=HashMap<Int,TextView>()
    fun setCount(key:Int,deleteCount:Int){
        var count=count.find { it.first==key }?.second
        if (count!=null){
            count-=deleteCount
        }
        countView[key]?.text=count.toString()
    }
    private fun addTab(titles:List<String>,counts:List<Pair<Int,Int>>){
        for (i in 0 until tableLayout.tabCount){
            val tab=tableLayout.getTabAt(i)
            val view=LayoutInflater.from(this).inflate(R.layout.item_we_and_qq_manager_clean,null)
            tab?.customView=view
            view.findViewById<TextView>(R.id.title).text=titles[i]
            val textView=view.findViewById<TextView>(R.id.count)
            countView[counts[i].first]=textView
            textView.text=counts[i].second.toString()
        }
    }
    private fun initClick(){
        barBack.setOnClickListener { finish() }

        tableLayout.getTabAt(tableLayout.selectedTabPosition)?.customView?.findViewById<TextView>(R.id.title)?.setTextColor(Color.parseColor("#3392FD"))
        tableLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p: TabLayout.Tab?) {
                p?.customView?.findViewById<TextView>(R.id.title)?.setTextColor(Color.parseColor("#222F3A"))
            }

            override fun onTabSelected(p: TabLayout.Tab?) {
                p?.customView?.findViewById<TextView>(R.id.title)?.setTextColor(Color.parseColor("#3392FD"))
            }
        })
    }
}