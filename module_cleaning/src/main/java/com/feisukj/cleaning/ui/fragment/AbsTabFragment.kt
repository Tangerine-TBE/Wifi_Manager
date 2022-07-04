package com.feisukj.cleaning.ui.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.SectionData
import com.example.module_base.widget.LoadingDialog


import com.feisukj.cleaning.BuildConfig
import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.AbsTabAdapter
import com.feisukj.cleaning.bean.FileBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.file.FileManager
import com.feisukj.cleaning.filevisit.FileR
import com.feisukj.cleaning.ui.UIConst
import com.feisukj.cleaning.utils.toAppOpenFile
import kotlinx.android.synthetic.main.fragment_abs_tab_clean.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

abstract class AbsTabFragment<T:FileBean>: Fragment(), BaseSectionAdapter.ItemSelectListener<TitleBean_Group,T> {

    private var job:Job?=null
    protected val deleteStack=HashSet<T>()
    protected val adapter=AbsTabAdapter<T>(emptyList()){ item ->
        val section= SectionData<TitleBean_Group,T>()
        section.apply {
            val t=TitleBean_Group()
            t.title=UIConst.idToTitle.find { item.group==it.first }?.second?:"出错了"
            this.id=item.group
            this.addItem(item)
            this.groupData=t
        }
        section
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val paths=getPath()
        if (paths!=null&&paths.isNotEmpty()){
            job=GlobalScope.launch {
                for (file in paths.map { FileR(it) }) {
                    if (!isActive){
                        return@launch
                    }
                    if (!file.exists()){
                        continue
                    }
                    FileManager.scanDirFile2(file,onNext = {
                        val item=onNextFile(it)
                        item?.let {
                            activity?.runOnUiThread {
                                adapterAddItem(item)
                            }
                        }
                        isActive&&isNextFile()
                    })
                }
                onComplete()
            }
        }
        val f= context?.let { FrameLayout(it).apply { this.layoutParams= ViewGroup.LayoutParams((resources.displayMetrics.density*300).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT) } }
        adapter?.adView=f
        if (f != null) {

        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_abs_tab_clean,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabRecyclerView.layoutManager= LinearLayoutManager(context)
        tabRecyclerView.adapter=adapter
        adapter.itemSelectListener=this
        initListener()
    }

    protected fun initListener(){
        tabClean.setOnClickListener {
            if (deleteStack.isEmpty()){
                return@setOnClickListener
            }
            val loadingDelete= LoadingDialog(context?:return@setOnClickListener)
                    .setCancelable(false)
                    .setTitleText("正在删除...")
            loadingDelete.show()
            GlobalScope.launch {
                for (c in deleteStack) {
                    if (!BuildConfig.DEBUG){
                        File(c.absolutePath).delete()
                    }
                }
                activity?.runOnUiThread {
                    adapter.removeAllItem(deleteStack)
                    onRemoveAllItem(deleteStack)
                    deleteStack.clear()
                    loadingDelete.dismiss()
                    upText()
                }
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
    }

    protected open fun adapterAddItem(item:T){
        adapter.addItem(item)
    }

    protected open fun onRemoveAllItem(item: Collection<T>){}

    protected open fun isNextFile()=true

    protected abstract fun getPath():List<String>?

    protected abstract fun onNextFile(file:FileR):T?

    protected abstract fun onComplete()

    protected fun upText(){
        if (deleteStack.isNullOrEmpty()){
            tabClean?.visibility= View.GONE
        }else{
            val s=deleteStack.map { it.fileSize }.sum()
            tabClean?.visibility= View.VISIBLE
            tabClean.text="删除${Formatter.formatFileSize(context,s)}，${deleteStack.size}个文件"
        }
    }

    override fun onSelectHeader(isSelect: Boolean, treeData: SectionData<TitleBean_Group, T>) {
        if (isSelect) {
            deleteStack.addAll(treeData.getItemData())
        }else{
            deleteStack.removeAll(treeData.getItemData())
        }
        upText()
    }

    override fun onSelectSubItem(isSelect: Boolean, treeData: SectionData<TitleBean_Group, T>, subItem: T) {
        if (isSelect) {
            deleteStack.add(subItem)
        }else{
            deleteStack.remove(subItem)
        }
        upText()
    }

    override fun onClickSubItem(treeData: SectionData<TitleBean_Group, T>, subItem: T) {
        toAppOpenFile(this, File(subItem.absolutePath))
    }
}