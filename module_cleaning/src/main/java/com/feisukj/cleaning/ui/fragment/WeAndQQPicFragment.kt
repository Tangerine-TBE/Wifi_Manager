package com.feisukj.cleaning.ui.fragment

import android.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.module_base.cleanbase.BaseFragment
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.SectionData
import com.example.module_base.cleanbase.UserActivityManager


import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.PhotoAdapter_
import com.feisukj.cleaning.bean.ImageBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.ui.activity.WeAndQQManagerActivity
import com.feisukj.cleaning.ui.activity.WeChatAndQQCleanActivity
import com.feisukj.cleaning.utils.toAppOpenFile
import kotlinx.android.synthetic.main.act_music_clean.*
import kotlinx.android.synthetic.main.frag_we_and_qq_pic_clean.*
import kotlinx.android.synthetic.main.frag_we_and_qq_pic_clean.recyclerView
import java.io.File

class WeAndQQPicFragment: BaseFragment(), BaseSectionAdapter.ItemSelectListener<TitleBean_Group,ImageBean>, BaseSectionAdapter.AllChooserListener {
    companion object{
        const val KEY="key"
    }
    private val act: WeChatAndQQCleanActivity? by lazy {
        UserActivityManager.getInstance().mActivityStack.find { it is WeChatAndQQCleanActivity } as? WeChatAndQQCleanActivity
    }
    private val stack=HashSet<ImageBean>()
    private var adapter:PhotoAdapter_?=null
    private val key:Int by lazy {
        arguments?.getInt(KEY) ?: -1
    }

    override fun getLayoutId()= R.layout.frag_we_and_qq_pic_clean

    override fun initView() {
        super.initView()
        if (act==null){
            return
        }
        if (key==-1)
            return
        val list=act?.map?.get(key)?.fileList?:return
        var i=0
        while (i<list.size){
            if (!list[i].exists()||!list[i].isFile){
                list.remove(list[i])
            }else{
                i++
            }
        }
        val stickySectionData=ArrayList<SectionData<TitleBean_Group, ImageBean>>()
        stickySectionData.add(SectionData<TitleBean_Group,ImageBean>().apply {
            this.groupData=TitleBean_Group().also {
                it.title="一个月以内"
                it.id=1
            }
            this.id=1
        })
        stickySectionData.add(SectionData<TitleBean_Group,ImageBean>().apply {
            this.groupData=TitleBean_Group().also {
                it.title="三个月以内"
                it.id=2
            }
            this.id=2
        })
        stickySectionData.add(SectionData<TitleBean_Group,ImageBean>().apply {
            this.groupData=TitleBean_Group().also {
                it.title="很久以前"
                it.id=3
            }
            this.id=3
        })
        recyclerView.layoutManager= GridLayoutManager(context, 4)
        adapter=PhotoAdapter_(stickySectionData)
        adapter?.itemSelectListener=this
        recyclerView.adapter=adapter
        initClick()
        adapter?.allChooserListener=this
        loadData(list)
        val f= FrameLayout(activity?:return).apply { this.layoutParams= ViewGroup.LayoutParams((resources.displayMetrics.density*300).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT) }
        adapter?.adView=f

    }

    private fun initClick(){
        clean.setOnClickListener {
            val data=ArrayList(adapter?.getData()?:return@setOnClickListener)
            val thread=Thread{
                for (subItem in stack){
                    File(subItem.absolutePath).delete()
                }
                var i=0
                while (i<data.size){
                    val subItems=data[i].getItemData()
                    data[i].removeAllItem(stack)
                    if (subItems.isEmpty()){
                        data.removeAt(i)
                    }else{
                        i++
                    }
                }
                activity?.runOnUiThread {
                    (context as? WeAndQQManagerActivity)?.setCount(key,stack.size)
                    stack.clear()
                    loadingDialog.dismiss()
                    allChoose.isSelected=false
                    adapter?.setData(data)
                    upText()
                }
            }

            AlertDialog.Builder(context)
                    .setTitle(R.string.deleteFile)
                    .setMessage(getString(R.string.askDelete).format(stack.size.toString()))
                    .setNegativeButton(R.string.no) { dialog, which -> }
                    .setPositiveButton(R.string.yes) { dialog, which ->
                        loadingDialog.setCancelable(false)
                        loadingDialog.setTitleText("正在删除...")
                        showLoading()
                        try {
                            thread.start()
                        }catch (e: Exception){
                            Toast.makeText(context,R.string.runDelete, Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                    .show()
        }
        allChoose.setOnClickListener { view ->
            view.isSelected=!view.isSelected
            if (adapter?.getData()?.any { it.groupData?.isCheck!=view.isSelected }==true){
                adapter?.getData()?.forEach { sectionData ->
                    sectionData.groupData?.isCheck=view.isSelected
                    onSelectHeader(view.isSelected,sectionData)
                    sectionData.getItemData().forEach {
                        it.isCheck=view.isSelected
                    }
                }
                adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun loadData(list:List<File>){
        val currentTime=System.currentTimeMillis()
        val oneMonth=currentTime-30*24*60*60*1000L
        val threeMonth=currentTime-30*24*60*60*1000L*3

        Thread{
            list.map { file ->
                ImageBean(file).also { imageBean ->
                    imageBean.group= when {
                        imageBean.fileLastModified>oneMonth -> {
                            1
                        }
                        imageBean.fileLastModified>threeMonth -> {
                            2
                        }
                        else -> {
                            3
                        }
                    }
                    val s=adapter?.getData()?.find { it.id==imageBean.group }?:SectionData<TitleBean_Group,ImageBean>().also { sectionData ->
                        val titleBean= TitleBean_Group()
                        sectionData.groupData= titleBean
                        sectionData.id=imageBean.group
                        adapter?.addHeaderItem(sectionData)
                        activity?.runOnUiThread {
                            adapter?.addHeaderItem(sectionData)
                        }
                    }
                    activity?.runOnUiThread {
                        adapter?.addSubItem(s,imageBean)
                    }
                }
            }
        }.start()

    }

    private fun upText(){
        if (stack.isNullOrEmpty()){
            bottomButton.visibility=View.GONE
        }else{
            bottomButton.visibility=View.VISIBLE
            clean.text=getString(R.string.deletePicC).format(stack.size.toString())
        }
    }

    override fun onSelectHeader(isSelect: Boolean, treeData: SectionData<TitleBean_Group, ImageBean>) {
        if (isSelect) {
            stack.addAll(treeData.getItemData())
        }else{
            stack.removeAll(treeData.getItemData())
        }
        upText()
    }

    override fun onSelectSubItem(isSelect: Boolean, treeData: SectionData<TitleBean_Group, ImageBean>, subItem: ImageBean) {
        if (isSelect) {
            stack.add(subItem)
        }else{
            stack.remove(subItem)
        }
        upText()
    }

    override fun onClickSubItem(treeData: SectionData<TitleBean_Group, ImageBean>, subItem: ImageBean) {
        toAppOpenFile(this, File(subItem.absolutePath))
    }

    override fun onChooserChange(isAllSelector: Boolean) {
        allChoose.isSelected=isAllSelector
    }
}