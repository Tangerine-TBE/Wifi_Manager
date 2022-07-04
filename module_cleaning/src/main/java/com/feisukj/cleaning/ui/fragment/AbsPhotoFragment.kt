package com.feisukj.cleaning.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.SectionData
import com.example.module_base.widget.LoadingDialog
import com.feisukj.cleaning.BuildConfig
import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.AbsPhotoAdapter
import com.feisukj.cleaning.bean.FileBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.file.FileManager
import com.feisukj.cleaning.filevisit.FileR
import com.feisukj.cleaning.ui.UIConst
import com.feisukj.cleaning.ui.UIConst.MONTH_TIME_ID
import com.feisukj.cleaning.ui.UIConst.SIX_MONTH_AGO
import com.feisukj.cleaning.ui.UIConst.SIX_MONTH_TIME_ID
import com.feisukj.cleaning.ui.UIConst.THREE_MONTH_TIME_ID
import com.feisukj.cleaning.ui.UIConst.monthWithinTime
import com.feisukj.cleaning.ui.UIConst.sixMonthWithinTime
import com.feisukj.cleaning.ui.UIConst.threeMonthWithinTime
import com.feisukj.cleaning.utils.toAppOpenFile
import kotlinx.android.synthetic.main.frag_qq_and_we_file_clean.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

abstract class AbsPhotoFragment<T:FileBean>: Fragment(), BaseSectionAdapter.ItemSelectListener<TitleBean_Group,T> {

    private var count = 0
    private var job:Job?=null
    protected val deleteStack=HashSet<T>()
    protected val adapter=AbsPhotoAdapter<T>(emptyList()){ item ->
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_qq_and_we_file_clean,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager= GridLayoutManager(context, 4)
        recyclerView.adapter=adapter
        recyclerView.itemAnimator=null

        adapter.itemSelectListener=this
        initListener()
    }

    protected fun initListener(){
        clean.setOnClickListener {
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
        adapter.getData().forEach {
            it.isFold = true
        }
        adapter.notifyDataSetChanged()
        when(position) {
            0 -> adapter.getData().find { it.groupData?.title.equals("一个月内") }?.isFold = false
            1 -> adapter.getData().find { it.groupData?.title.equals("三个月内") }?.isFold = false
            2 -> adapter.getData().find { it.groupData?.title.equals("三个月前") }?.isFold = false
            else ->return
        }
        adapter.notifyDataSetChanged()
//        tabRecyclerView.scrollToPosition(position)
//        var treeData = adapter.getData()[position]
//        treeData.isFold=!treeData.isFold
//        adapter.notifyItemChanged(treeData.groupPosition)
//        adapter.notifyItemChanged(position)
//        if (treeData.getItemData().isNotEmpty()) {
//            if (!treeData.isFold) {
//                adapter.notifyItemRangeInserted(position + 1, treeData.getItemData().size)
//                adapter.notifyItemRangeChanged(position + treeData.getItemData().size + 1, adapter.itemCount - (position + treeData.getItemData().size + 1))
//            } else {
//                adapter.notifyItemRangeRemoved(position + 1, treeData.getItemData().size)
//                adapter.notifyItemRangeChanged(position + 1, adapter.itemCount - (position + 1))
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    protected open fun adapterAddItem(item:T){
        val d=Date(item.fileLastModified)
        count++
        item.group=when{
//            todayZeroTime.before(d)-> {
//                TODAY_TIME_ID
//            }
            monthWithinTime.before(d)-> {
                MONTH_TIME_ID
            }
            threeMonthWithinTime.before(d)-> {
                THREE_MONTH_TIME_ID
            }
            sixMonthWithinTime.before(d)-> {
                SIX_MONTH_TIME_ID
            }
            else->{
                SIX_MONTH_AGO
            }
        }
        adapter.addItem(item)
    }

    protected open fun isNextFile()=true

    protected abstract fun getPath():List<String>?

    protected abstract fun onNextFile(file:FileR):T?

    protected abstract fun onComplete()

    protected fun upText(){
        if (deleteStack.isNullOrEmpty()){
            bottomButton?.visibility= View.GONE
        }else{
            val s=deleteStack.map { it.fileSize }.sum()
            bottomButton?.visibility= View.VISIBLE
//            clean.text="删除${Formatter.formatFileSize(context,s)}"
            clean.text="清理${deleteStack.size}个文件"
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