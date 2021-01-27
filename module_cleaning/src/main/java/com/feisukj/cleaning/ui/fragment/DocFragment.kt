package com.feisukj.cleaning.ui.fragment

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.cleanbase.BaseFragment2
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
import com.feisukj.cleaning.utils.toAppOpenFile
import kotlinx.android.synthetic.main.frag_doc_clean.*
import java.io.File

@SuppressLint("UseSparseArrays")
class DocFragment : BaseFragment2(),NextFileCallback, BaseSectionAdapter.ItemSelectListener<TitleBean_Group, AllFileBean>{
    companion object {
        const val KEY="title"
        val tabTitle= listOf(FileType.doc,FileType.pdf,FileType.ppt,FileType.xls,FileType.txt)

        //        private val docData=HashMap<Int,ArrayList<AllFileBean>>()
//        private val pdfData=HashMap<Int,ArrayList<AllFileBean>>()
//        private val pptData=HashMap<Int,ArrayList<AllFileBean>>()
//        private val xlsData=HashMap<Int,ArrayList<AllFileBean>>()
//        private val txtData=HashMap<Int,ArrayList<AllFileBean>>()
//
//        fun addData(type: FileType,fileBean: AllFileBean){
//            val data=when(type){
//                FileType.doc -> docData
//                FileType.pdf -> pdfData
//                FileType.ppt -> pptData
//                FileType.xls -> xlsData
//                FileType.txt -> txtData
//                else->{
//                    return
//                }
//            }
//            data[fileBean.group].also {
//                if (it==null){
//                    data[fileBean.group]=ArrayList<AllFileBean>().apply {
//                        add(fileBean)
//                    }
//                }else{
//                    it.add(fileBean)
//                }
//            }
//        }
        private val docData = ArrayList<AllFileBean>()
        private val pdfData = ArrayList<AllFileBean>()
        private val pptData = ArrayList<AllFileBean>()
        private val xlsData = ArrayList<AllFileBean>()
        private val txtData = ArrayList<AllFileBean>()

        fun addData(type: FileType,fileBean: AllFileBean){
            val data=when(type){
                FileType.doc -> docData
                FileType.pdf -> pdfData
                FileType.ppt -> pptData
                FileType.xls -> xlsData
                FileType.txt -> txtData
                else->{
                    return
                }
            }
            data.add(fileBean)
        }
    }

    private var stack = HashSet<AllFileBean>()
    private var flag = tabTitle[0].name
    private var adapter = PhotoAndFileAdapter_(emptyList()){item ->
        val section= SectionData<TitleBean_Group,AllFileBean>()
        section.apply {
            val t=TitleBean_Group()
            t.title= UIConst.idToTitle.find { item.group==it.first }?.second?:"出错了"
            this.id=item.group
            this.addItem(item)
            this.groupData=t
        }
        section
    }
//    private var data: HashMap<Int, ArrayList<AllFileBean>>? = null
    private var data: ArrayList<AllFileBean>? = null
    private var docCount = 0
        set(value) {
            field=value
            docTitleCount.text="${field}项"
        }

    override fun getLayoutId()= R.layout.frag_doc_clean

    override fun initView() {
        flag=arguments?.getString(KEY)?: tabTitle[0].name
        data=when(FileType.valueOf(flag)){
            FileType.doc -> {
                docTitleTip.text="DOC文件"
                docData
            }
            FileType.pdf -> {
                docTitleTip.text="PDF文件"
                pdfData
            }
            FileType.ppt -> {
                docTitleTip.text="PPT文件"
                pptData
            }
            FileType.xls -> {
                docTitleTip.text="表格文件"
                xlsData
            }
            FileType.txt -> {
                docTitleTip.text="TXT文件"
                txtData
            }
            else->{
                docTitleTip.text="DOC文件"
                docData
            }
        }
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        ArrayList<SectionData<TitleBean_Group, AllFileBean>>()
//        val adapterData = data?.map { entry ->
//            val sectionData = SectionData<TitleBean_Group, AllFileBean>()
//            sectionData.groupData = TitleBean_Group().also {
//                val item = entry.value.firstOrNull() ?: return@also
//                it.title = "${item.year}年${item.month + 1}月"
//                sectionData.id = item.group
//            }
//            sectionData.addAllItem(entry.value.toList())
//            sectionData
//        }?.sortedBy { -it.id } ?: emptyList()
//        adapterData.firstOrNull()?.isFold = false
//        adapter = PhotoAndFileAdapter_(adapterData) { item ->
//            val section = SectionData<TitleBean_Group, AllFileBean>()
//            section.apply {
//                val t = TitleBean_Group()
//                t.title = UIConst.idToTitle.find { item.group == it.first }?.second ?: "出错了"
//                this.id = item.group
//                this.addItem(item)
//                this.groupData = t
//            }
//            section
//        }
        data?.toTypedArray()?.forEach {
            adapter.addItem(it)
        }
        recyclerView.adapter = adapter
        adapter?.itemSelectListener = this
//        docCount = adapterData.flatMap { it.getItemData() }.size
        FileManager.addCallBack(this)
        val context=activity?:return
        val f= FrameLayout(context).apply { this.layoutParams= ViewGroup.LayoutParams((resources.displayMetrics.density*300).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT) }
        adapter?.adView=f

    }

    override fun initListener() {
        clean.setOnClickListener {
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
                activity?.runOnUiThread {
//                    data?.toList()?.forEach {
//                        it.second.removeAll(stack)
//                    }
                    data?.removeAll(stack)
                    stack.clear()
                    upText()
                    adapter?.getData()?.sumBy { it.getItemData().size }?.let {
                        docCount=it
                    }
                    dismissLoadingDialog()
                    adapter?.setData(adapterData)
                }
            }
            if (loadingDialog?.isShowing!=true) {
                AlertDialog.Builder(context?:return@setOnClickListener)
                        .setTitle(R.string.deleteFile)
                        .setMessage(getString(R.string.askDelete).format(stack.size.toString()))
                        .setNegativeButton(R.string.no) { dialog, which -> }
                        .setPositiveButton(R.string.yes) { dialog, which ->
                            loadingDialog?.setCancelable(false)
                            loadingDialog?.setTitleText("正在删除...")
                            loadingDialog?.show()
                            thread.start()
                        }
                        .show()
            }else{
                Toast.makeText(context,R.string.runDelete, Toast.LENGTH_SHORT).show()
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

    override fun onComplete(){
        activity?.runOnUiThread {
            adapter?.getData()?.sumBy { it.getItemData().size }?.let {
                docCount=it
            }
            adapter?.getData().firstOrNull()?.isFold = false
            if (adapter?.getData()?.isEmpty()!!) {
                noData.visibility = View.VISIBLE
            } else {
                noData.visibility = View.GONE
            }
            adapter.isFinished = true
            for ((key,value) in adapter.map){
                if (value.currentView.id== R.id.load)
                    value.showNext()
            }
        }
    }

    override fun onNextFile(type: FileType,fileBean: FileBean) {
        if (FileType.valueOf(flag)!=type){
            return
        }
        if (fileBean !is AllFileBean){
            return
        }
        activity?.runOnUiThread {
            docCount++
            val section=adapter?.getData()?.find { it.id==fileBean.group }
            if(section==null){
                SectionData<TitleBean_Group,AllFileBean>().also { sectionData ->
                    sectionData.groupData=TitleBean_Group().also {
                        it.title="${fileBean.year}年${fileBean.month+1}月"
                    }
                    adapter?.addHeaderItem(sectionData)
                    sectionData.id=fileBean.group
                }
            }else{
                adapter?.addSubItem(section,fileBean)
            }
        }
    }

    private fun upText(){
        if (stack.isNullOrEmpty()){
            bottomButton.visibility= View.GONE
        }else{
            bottomButton.visibility= View.VISIBLE
            clean.text=resources.getString(R.string.cleanFielTip).format(stack.size.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FileManager.removeCallBack(this)
    }

    override fun onDestroy() {
        super.onDestroy()
//        data?.toList()?.forEach { pair ->
//            pair.second.toList().forEach {
//                it.isCheck = false
//            }
//        }
        data?.forEach {
            it.isCheck = false
        }
    }
}