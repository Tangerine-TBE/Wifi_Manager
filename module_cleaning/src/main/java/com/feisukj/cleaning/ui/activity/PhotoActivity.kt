package com.feisukj.cleaning.ui.activity

import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.example.module_base.cleanbase.BaseActivity2
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.SectionData


import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.PhotoAdapter_
import com.feisukj.cleaning.bean.ImageBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.file.DirNextFileCallback
import com.feisukj.cleaning.file.FileManager
import com.feisukj.cleaning.ui.UIConst
import com.feisukj.cleaning.utils.*
import kotlinx.android.synthetic.main.act_photo_clean_.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

/**
 * 通过 TITLE_KEY 传入标题
 * 通过 PATH_KEY 传入路径
 */
class PhotoActivity : BaseActivity2(), BaseSectionAdapter.ItemSelectListener<TitleBean_Group,ImageBean>{
    companion object{
        const val TITLE_RES_KEY="title_key"
        const val PATH_KEY="path_key"
        const val IS_VIDEO_KEY="is_video"
        const val ARRAY_PATH_KEY="array_path_key"
    }

    private val stack=HashSet<ImageBean>()
    private var adapter: PhotoAdapter_?=null
    private var isVideo=false

    override fun getLayoutId()= R.layout.act_photo_clean_
    override fun isActionBar(): Boolean = false
    override fun initView() {
        intent.getIntExtra(TITLE_RES_KEY,-1).let {
            if (it!=-1){
                barTitle.text = resources.getText(it)
            }
        }
        immersionBar.statusBarDarkFont(true).statusBarColor(android.R.color.transparent).init()
        intent.getStringExtra(TITLE_RES_KEY)?.let {
            try {
                if (it.isNotEmpty()){
                    barTitle.text = it
                }
            }catch (e:Exception){

            }
        }
        isVideo=intent.getBooleanExtra(IS_VIDEO_KEY,false)
        val adapterData= ArrayList<SectionData<TitleBean_Group, ImageBean>>()
        recyclerView.layoutManager= GridLayoutManager(this, 4)
        adapter= PhotoAdapter_(adapterData)
        adapter?.itemSelectListener=this
        recyclerView.adapter=adapter
        GlobalScope.launch {
            getData()
            runOnUiThread {
                recyclerView.scrollToPosition(0)
            }
        }

    }

    private fun getData(){
        val path=intent.getStringExtra(PATH_KEY)
        val paths=intent.getStringArrayListExtra(ARRAY_PATH_KEY)
        val files=
        if (path==null&&paths.isNullOrEmpty()){
            return
        }else{
            if (path!=null){
                listOf(File(path))
            }else if (!paths.isNullOrEmpty()){
                paths.map { File(it) }
            }else{
                return
            }
        }
        FileManager.scanDirsFile(dirFiles = files,toT = { file ->
            ImageBean(file).also {
                val currentDate= Date(it.fileLastModified)
                it.group = when{
                    UIConst.monthWithinTime.before(currentDate)-> {
                        UIConst.MONTH_TIME_ID
                    }
                    UIConst.threeMonthWithinTime.before(currentDate)-> {
                        UIConst.THREE_MONTH_TIME_ID
                    }
                    UIConst.sixMonthWithinTime.before(currentDate)-> {
                        UIConst.SIX_MONTH_TIME_ID
                    }
                    else->{
                        UIConst.SIX_MONTH_AGO
                    }
                }
//                it.group=it.year*12+it.month
            }
        },isWith = {file->
            if (isVideo){
                Constant.VIDEO_FORMAT.any {
                    file.name.endsWith(it)
                }
            }else{
                Constant.PICTURE_FORMAT.any {
                    file.name.endsWith(it)
                }
            }
        },dirNextFileCallback = object :DirNextFileCallback<ImageBean>{
            override fun onNextFile(item: ImageBean) {
                runOnUiThread {
                    val key=item.group
                    val groupData=adapter?.getData()?.find { it.id==key }?:SectionData<TitleBean_Group,ImageBean>().apply {
                        this.groupData=TitleBean_Group().also { group ->
//                            val date=if (language == Language.zh_CN)
//                                "${item.year}年${item.month +1}月"
//                            else
//                                (MonthIndexMap.values().find { it.month==item.month }?: MonthIndexMap.Undecimber).name+" ${item.year}"
//                            group.title=date
                            group.title = UIConst.idToTitle.find { item.group==it.first }?.second?:"出错了"
                        }
                        this.id=key
                        this.isFold=true
                    }
                    adapter?.addSubItem(groupData,item)
                }
            }
        })
        runOnUiThread {
            if (adapter?.getData()?.isEmpty()!!){
                noData.visibility = View.VISIBLE
            }else{
                noData.visibility = View.GONE
            }
        }
    }

    override fun initListener() {
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
                runOnUiThread {
                    stack.clear()
                    upText()
                    dismissLoadingDialog()
                    adapter?.setData(data)
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
    private fun upText(){
        if (stack.isNullOrEmpty()){
            bottomButton.visibility= View.GONE
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
}