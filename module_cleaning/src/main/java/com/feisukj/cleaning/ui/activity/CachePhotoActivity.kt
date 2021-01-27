package com.feisukj.cleaning.ui.activity

import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.Toast
import com.example.module_base.cleanbase.BaseActivity2
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.SectionData


import com.feisukj.cleaning.BuildConfig
import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.PhotoAdapter_
import com.feisukj.cleaning.bean.ImageBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.file.FileContainer
import com.feisukj.cleaning.ui.UIConst
import com.feisukj.cleaning.utils.*
import kotlinx.android.synthetic.main.act_photo_clean_.*
import kotlinx.android.synthetic.main.act_photo_clean_.allChoose
import kotlinx.android.synthetic.main.act_photo_clean_.bottomButton
import kotlinx.android.synthetic.main.act_photo_clean_.clean
import kotlinx.android.synthetic.main.act_photo_clean_.noData
import kotlinx.android.synthetic.main.act_photo_clean_.recyclerView
import kotlinx.android.synthetic.main.act_photo_clean_.timeType1
import kotlinx.android.synthetic.main.act_photo_clean_.timeType2
import kotlinx.android.synthetic.main.act_photo_clean_.timeType3
import kotlinx.android.synthetic.main.act_photo_clean_.timeType4
import kotlinx.android.synthetic.main.frag_qq_and_we_file_clean.*
import java.io.File
import java.lang.Exception

class CachePhotoActivity : BaseActivity2(), BaseSectionAdapter.ItemSelectListener<TitleBean_Group,ImageBean>{

    private val stack=HashSet<ImageBean>()
    private var adapter: PhotoAdapter_?=null
    private var anim = ScaleAnimation(0f,1f,1f,1f, Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0.5f)

    override fun getLayoutId()= R.layout.act_photo_clean_
    override fun initView() {
        setContentText(R.string.garbagePic)
        bar.visibility = View.GONE
        view.visibility = View.GONE
        recyclerView.layoutManager= GridLayoutManager(this, 4)
        val cachePhotoData=ArrayList<SectionData<TitleBean_Group, ImageBean>>()
        adapter= PhotoAdapter_(cachePhotoData)
        adapter?.itemSelectListener=this
        recyclerView.adapter=adapter
        recyclerView.itemAnimator=null
        loadData()
        anim.apply {
            this.duration = 1500
            this.interpolator = LinearInterpolator()
            this.repeatMode = Animation.RESTART
            this.repeatCount = -1
        }
        img_anim.animation = anim

    }

    override fun initListener() {
        clean.setOnClickListener {
            val data=ArrayList(adapter?.getData()?:return@setOnClickListener)
            val thread=Thread{
                for (subItem in stack){
                    if(!BuildConfig.DEBUG){
                        File(subItem.absolutePath).delete()
                    }
                }
                var i=0
                while (i<data.size){
                    val subItems=data[i].getItemData()
                    try {
                        data[i].removeAllItem(stack)
                    }catch (e : Exception){
                        e.printStackTrace()
                    }
                    if (subItems.isEmpty()){
                        data.removeAt(i)
                    }else{
                        i++
                    }
                }
                runOnUiThread {
                    FileContainer.removeAllPhotoFile(stack)
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
                            try {
                                loadingDialog.setCancelable(false)
                                loadingDialog.setTitleText("正在删除...")
                                loadingDialog.show()
                                thread.start()
                            }catch (e : Exception){
                                e.printStackTrace()
                            }
                        }
                        .show()
            }else{
                Toast.makeText(this,R.string.runDelete, Toast.LENGTH_SHORT).show()
            }
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
        recyclerView.scrollToPosition(0)
        when(position) {
            0 -> adapter?.getData()?.find { it.groupData?.title.equals("一个月内") }?.isFold = false
            1 -> adapter?.getData()?.find { it.groupData?.title.equals("三个月内") }?.isFold = false
            2 -> adapter?.getData()?.find { it.groupData?.title.equals("半年内") }?.isFold = false
            3 -> adapter?.getData()?.find { it.groupData?.title.equals("半年前") }?.isFold = false
            else ->return
        }
        adapter?.notifyDataSetChanged()
    }

    private fun loadData(){
        if (FileContainer.getPhotoFileList().isEmpty()){
            noData.visibility = View.VISIBLE
        }else{
            noData.visibility = View.GONE
        }
        FileContainer.getPhotoFileList().forEach {item->
            val key=item.group
            val groupData=adapter?.getData()?.find { it.id==key }?:SectionData<TitleBean_Group,ImageBean>().apply {
                this.groupData=TitleBean_Group().also { it ->
//                    val date=if (language == Language.zh_CN)
//                        "${item.year}年${item.month +1}月"
//                    else
//                        (MonthIndexMap.values().find { it.month==item.month }?: MonthIndexMap.Undecimber).name+" ${item.year}"
//                    it.title=date
                    it.title = UIConst.idToTitle.find { item.group==it.first }?.second?:"出错了"
                }
                this.id=key
                this.addItem(item)
                adapter?.addHeaderItem(this)
            }
            adapter?.addSubItem(groupData,item)
        }
        title_text.text = "${FileContainer.getPhotoFileList().size}张图片"
        adapter?.getData()?.lastOrNull()?.isFold = false
        adapter?.notifyDataSetChanged()

        adapter?.getData()?.forEach { sectionData ->
            sectionData.groupData?.isCheck=true
            onSelectHeader(true,sectionData)
            sectionData.getItemData().forEach {
                it.isCheck=true
            }
        }
        adapter?.notifyDataSetChanged()
        allChoose.isSelected = true
    }

    override fun onDestroy() {
        super.onDestroy()
        FileContainer.getPhotoFileList().forEach {
            it.isCheck=false
        }
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