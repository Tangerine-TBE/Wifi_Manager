package com.feisukj.cleaning.ui.activity

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.cleanbase.BaseActivity2
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.SectionData


import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.PhotoAndFileAdapter_
import com.feisukj.cleaning.bean.AllFileBean
import com.feisukj.cleaning.bean.FileBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.file.FileManager
import com.feisukj.cleaning.file.FileType
import com.feisukj.cleaning.file.NextFileCallback
import com.feisukj.cleaning.ui.UIConst
import com.feisukj.cleaning.utils.getSizeString
import com.feisukj.cleaning.utils.toAppOpenFile
import kotlinx.android.synthetic.main.act_music_clean.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MusicActivity: BaseActivity2(), NextFileCallback, BaseSectionAdapter.ItemSelectListener<TitleBean_Group, AllFileBean>{
    companion object {
        val musicData=ArrayList<AllFileBean>()
        fun addData(type: FileType, file: AllFileBean){
            if (type!= FileType.music){
                return
            }
            musicData.add(file)
        }
    }
    private var adapter:PhotoAndFileAdapter_?=null
    private var stack=HashSet<AllFileBean>()
    override fun getLayoutId()= R.layout.act_music_clean

    override fun isActionBar(): Boolean = false

    override fun initView() {
        val adapterData=ArrayList<SectionData<TitleBean_Group, AllFileBean>>()
        immersionBar.statusBarDarkFont(true).statusBarColor(android.R.color.transparent).init()
        musicData.toList().forEach {fileBean->
            val section=adapterData.find { it.id==fileBean.group }?:SectionData<TitleBean_Group,AllFileBean>().also {
                it.groupData=TitleBean_Group().also {
                    it.title= UIConst.idToTitle.find { fileBean.group==it.first }?.second?:"出错了"
                }
                it.id=fileBean.group
                it.isFold=true
                adapterData.add(it)
            }
            section.addItem(fileBean)
        }
        adapterData.sortBy { -it.id }
        FileManager.addCallBack(this)
        barTitle.text = "音频管理"
        recyclerView.layoutManager= LinearLayoutManager(this)
        adapter= PhotoAndFileAdapter_(adapterData){item ->
            val section=SectionData<TitleBean_Group,AllFileBean>()
            section.apply {
                val t=TitleBean_Group()
                t.title= UIConst.idToTitle.find { item.group==it.first }?.second?:"出错了"
                this.id=item.group
                this.addItem(item)
                this.groupData=t
            }
            section
        }
        recyclerView.adapter=adapter
        if (adapter?.getData()?.isEmpty()!!){
            noData.visibility = View.VISIBLE
        }else{
            noData.visibility = View.GONE
        }
        adapter?.itemSelectListener=this
        adapter?.isFinished = true
        for ((key,value) in adapter!!.map){
            if (value.currentView.id== R.id.load)
                value.showNext()
        }
        val f= FrameLayout(this).apply { this.layoutParams= ViewGroup.LayoutParams((resources.displayMetrics.density*300).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT) }
        adapter?.adView=f

    }

    override fun initListener() {
        buttonClean.setOnClickListener {
            val adapterData=ArrayList(adapter?.getData()?:return@setOnClickListener)
            val thread=Thread{
                for (subItem in stack){
                    File(subItem.absolutePath).delete()
                }
                var i=0
                while (i<adapterData.size){
                    val subItems=adapterData[i].getItemData()
                    adapterData[i].removeAllItem(stack)
                    if (subItems.isEmpty()){
                        adapterData.removeAt(i)
                    }else{
                        i++
                    }
                }
                runOnUiThread {
                    musicData.removeAll(stack)
                    stack.clear()
                    upText()
                    dismissLoadingDialog()
                    adapter?.setData(adapterData)
                }
            }
            if (!loadingDialog.isShowing) {
                AlertDialog.Builder(this)
                        .setTitle(R.string.deleteFile)
                        .setMessage(getString(R.string.askDelete).format(stack.size.toString()))
                        .setNegativeButton(R.string.no) { dialog, which -> }
                        .setPositiveButton(R.string.yes) { dialog, which ->
                            loadingDialog.setCancelable(false)
                            loadingDialog.setTitleText("正在删除...")
                            loadingDialog.show()
                            thread.start()
                        }
                        .show()
            }else{
                Toast.makeText(this,R.string.runDelete, Toast.LENGTH_SHORT).show()
            }
        }
        goBack.setOnClickListener {
            finish()
        }
        timeType1.setOnClickListener {
            timeType1.isSelected = true
            timeType2.isSelected = false
            timeType3.isSelected = false
            timeType4.isSelected = false
            selectType(0)
        }
        timeType2.setOnClickListener {
            timeType1.isSelected = false
            timeType2.isSelected = true
            timeType3.isSelected = false
            timeType4.isSelected = false
            selectType(1)
        }
        timeType3.setOnClickListener {
            timeType1.isSelected = false
            timeType2.isSelected = false
            timeType3.isSelected = true
            timeType4.isSelected = false
            selectType(2)
        }
        timeType4.setOnClickListener {
            timeType1.isSelected = false
            timeType2.isSelected = false
            timeType3.isSelected = false
            timeType4.isSelected = true
            selectType(3)
        }
    }

    private fun selectType(position : Int){
        adapter?.getData()?.forEach {
            it.isFold = true
        }
        adapter?.notifyDataSetChanged()
        when(position) {
            0 -> adapter?.getData()?.find { it.groupData?.title.equals("一个月内") }?.isFold = false
            1 -> adapter?.getData()?.find { it.groupData?.title.equals("三个月内") }?.isFold = false
            2 -> adapter?.getData()?.find { it.groupData?.title.equals("半年内") }?.isFold = false
            3 -> adapter?.getData()?.find { it.groupData?.title.equals("半年前") }?.isFold = false
            else ->return
        }
        adapter?.notifyDataSetChanged()
    }

    override fun onNextFile(type: FileType,fileBean: FileBean) {
        if (type!=FileType.music){
            return
        }
        if (fileBean !is AllFileBean){
            return
        }
        runOnUiThread {
            val section=adapter?.getData()?.find { it.id==fileBean.group }
            if(section==null){
                SectionData<TitleBean_Group,AllFileBean>().also {
                    it.groupData=TitleBean_Group().also {
//                        it.title="${fileBean.year}年${fileBean.month+1}月"
                        it.title=UIConst.idToTitle.find { fileBean.group==it.first }?.second?:"出错了"
                    }
                    adapter?.addHeaderItem(it)
                    it.id=fileBean.group
                }
            }else{
                adapter?.addSubItem(section,fileBean)
            }
        }
    }

    override fun onComplete(){
        if (loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
    }


    override fun onSelectHeader(isSelect: Boolean, treeData: SectionData<TitleBean_Group, AllFileBean>) {
        if (isSelect) {
            stack.addAll(treeData.getItemData())
        }else{
            stack.removeAll(treeData.getItemData())
        }
        upText()
    }

    override fun onSelectSubItem(isSelect: Boolean, treeData: SectionData<TitleBean_Group, AllFileBean>, subItem: AllFileBean) {
        if (isSelect) {
            stack.add(subItem)
        }else{
            stack.remove(subItem)
        }
        upText()
    }

    override fun onClickSubItem(treeData: SectionData<TitleBean_Group, AllFileBean>, subItem: AllFileBean) {
        toAppOpenFile(this, File(subItem.absolutePath))
    }

    private fun upText(){
        if (stack.size==0){
            relative.visibility=View.GONE
        }else{
            if(relative.visibility==View.GONE)
                relative.visibility=View.VISIBLE
            buttonClean.text=String.format(resources.getString(R.string.cleanVar,getSizeString(stack.map { it.fileSize }.sum())))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FileManager.removeCallBack(this)
        musicData.filter { it.isCheck }.forEach {
            it.isCheck = false
        }
    }

}