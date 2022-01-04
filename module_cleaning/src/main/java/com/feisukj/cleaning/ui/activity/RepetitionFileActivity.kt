package com.feisukj.cleaning.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import android.text.format.Formatter
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.SectionData
import com.example.module_base.widget.LoadingDialog


import com.feisukj.cleaning.BuildConfig
import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.RepetitionFileAdapter
import com.feisukj.cleaning.bean.FileBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.file.RepetitionFileManager
import com.feisukj.cleaning.utils.toAppOpenFile
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.act_similar_file_clean.*
import kotlinx.android.synthetic.main.item_action_bar.*
import java.io.File

class RepetitionFileActivity:FragmentActivity(), BaseSectionAdapter.ItemSelectListener<TitleBean_Group,FileBean> {
    private var repetitionAdapter:RepetitionFileAdapter?=null
    private val stack=HashSet<FileBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_similar_file_clean)
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .init()
        goBack.setOnClickListener { finish() }
        initListener()
        titleText.text="重复文件"
        repetitionAdapter=if(RepetitionFileManager.isComplete){
            val d=RepetitionFileManager.similarFile.entries.sortedBy { -it.key.length }.map {
                val s= SectionData<TitleBean_Group,FileBean>()
                s.groupData=TitleBean_Group().apply {
                    this.title=if (it.key.length==0L){
                        "空文件"
                    }else{
                        if (it.key.format==null){
                            "无格式文件"
                        }else{
                            it.key.format+" 格式文件"
                        }
                    }
                }
                s.addAllItem(it.value)
                s.isFold=false
                s
            }
            RepetitionFileAdapter(d)
        }else{
            RepetitionFileAdapter(emptyList())
        }
        repetitionAdapter?.itemSelectListener=this
        reFileRecyclerView.layoutManager= GridLayoutManager(this, 4)
        reFileRecyclerView.adapter=repetitionAdapter

        val f= FrameLayout(this).apply { this.layoutParams= ViewGroup.LayoutParams((resources.displayMetrics.density*300).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT) }
        repetitionAdapter?.adView=f

    }

    private fun initListener(){
        reClean.setOnClickListener {
            val loadingDelete= LoadingDialog(this)
                    .setCancelable(false)
                    .setTitleText("正在删除...")
            val thread=Thread{
                for (subItem in stack){
                    if (!BuildConfig.DEBUG) {
                        File(subItem.absolutePath).delete()
                    }
                }
                runOnUiThread {
                    loadingDelete.dismiss()
                    repetitionAdapter?.removeAll(stack)
                    stack.clear()
                    upText()
                }
            }
            AlertDialog.Builder(this)
                    .setTitle(R.string.deleteFile)
                    .setMessage(getString(R.string.askDelete).format(stack.size.toString()))
                    .setNegativeButton(R.string.no) { dialog, which -> }
                    .setPositiveButton(R.string.yes) { dialog, which ->
                        try{
                            loadingDelete.show()
                            thread.start()
                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                    }
                    .show()
            brainpower.isSelected=false
        }

        brainpower.setOnClickListener { view ->
            view.isSelected=!view.isSelected
            brainpower(view.isSelected)
        }
    }

    private fun brainpower(enabled:Boolean){
        val data=repetitionAdapter?.getData()?:return
        for (item in stack.toList()){
            item.isCheck=false
            data.find { it.getItemData().contains(item) }?.also {
                repetitionAdapter?.changeSubItem(it,item)
            }
        }
        if (stack.isNotEmpty()) {
            stack.clear()
        }
        if (enabled){
            data.forEach { stickySectionData ->
                val imageBean=stickySectionData.getItemData().minByOrNull { it.fileName.length }
                stickySectionData.getItemData().toList().forEach {
                    if (it!=imageBean){
                        it.isCheck=true
                        repetitionAdapter?.changeSubItem(stickySectionData,it)
                        stack.add(it)
                    }
                }
            }
        }
        upText()
    }

    private fun upText(){
        if (stack.isNullOrEmpty()){
            reClean.isSelected=false
            reClean.setText(R.string.delete)
        }else{
            reClean.isSelected=true
            reClean.text="删除${Formatter.formatShortFileSize(this,stack.map { it.fileSize }.sum())}(${stack.size}个)"
        }
    }

    override fun onSelectHeader(isSelect: Boolean, treeData: SectionData<TitleBean_Group, FileBean>) {
        if (isSelect) {
            stack.addAll(treeData.getItemData())
        }else{
            stack.removeAll(treeData.getItemData())
        }
        brainpower.isSelected=false
        upText()
    }

    override fun onSelectSubItem(isSelect: Boolean, treeData: SectionData<TitleBean_Group, FileBean>, subItem: FileBean) {
        if (isSelect) {
            stack.add(subItem)
        }else{
            stack.remove(subItem)
        }
        brainpower.isSelected=false
        upText()
    }

    override fun onClickSubItem(treeData: SectionData<TitleBean_Group, FileBean>, subItem: FileBean) {
        toAppOpenFile(this, File(subItem.absolutePath))
    }
}