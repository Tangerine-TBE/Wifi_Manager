package com.feisukj.cleaning.ui.activity

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import android.widget.Toast
import com.example.module_base.cleanbase.BaseActivity2
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.SectionData


import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.SimilarPhotoAdapter_
import com.feisukj.cleaning.bean.ImageBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.ui.fragment.ProposalPhotoManagerFragment
import com.feisukj.cleaning.utils.toAppOpenFile
import kotlinx.android.synthetic.main.act_similar_photo_clean.*
import java.io.File

class SimilarPhotoActivity : BaseActivity2(), BaseSectionAdapter.ItemSelectListener<TitleBean_Group,ImageBean>{
    private var adapter:SimilarPhotoAdapter_?=null
    private val stack=HashSet<ImageBean>()

    override fun getLayoutId()= R.layout.act_similar_photo_clean

    override fun getStatusBarColor(): Int {
        return R.color.transparent
    }

    override fun initView() {
        setContentText(R.string.SimilarPictures)
        recyclerView.layoutManager= GridLayoutManager(this, 4)
        val data=ArrayList<SectionData<TitleBean_Group, ImageBean>>(ProposalPhotoManagerFragment.similarPhotoData)
        data.forEach { it.isFold=false }
        adapter= SimilarPhotoAdapter_(data)
        recyclerView.adapter=adapter
        adapter?.itemSelectListener=this

        val f= FrameLayout(this).apply { this.layoutParams= ViewGroup.LayoutParams((resources.displayMetrics.density*300).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT) }
        adapter?.adView=f

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
                    ProposalPhotoManagerFragment.similarPhotoData.forEach {
                        it.removeAllItem(stack)
                    }
                    val temp=ProposalPhotoManagerFragment.similarPhotoData.filter { it.getItemData().size<=1 }
                    ProposalPhotoManagerFragment.similarPhotoData.removeAll(temp)
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
            brainpower.isSelected=false
        }
        brainpower.setOnClickListener { view ->
            view.isSelected=!view.isSelected
            brainpower(view.isSelected)
        }
    }

    private fun brainpower(enabled:Boolean){
        val data=adapter?.getData()?:return
        for (item in stack.toList()){
            item.isCheck=false
            data.find { it.getItemData().contains(item) }?.also {
                adapter?.changeSubItem(it,item)
            }
        }
        if (stack.isNotEmpty()) {
            stack.clear()
        }
        if (enabled){
            data.forEach { stickySectionData ->
                val imageBean=stickySectionData.getItemData().maxByOrNull { it.fileSize }
                stickySectionData.getItemData().toList().forEach {
                    if (it!=imageBean){
                        it.isCheck=true
                        adapter?.changeSubItem(stickySectionData,it)
                        stack.add(it)
                    }
                }
            }
        }
        upText()
    }

    private fun upText(){
        if (stack.isNullOrEmpty()){
            clean.isSelected=false
            clean.setText(R.string.delete)
        }else{
            clean.isSelected=true
            clean.text=String.format(resources.getString(R.string.deletePicC),stack.size.toString())
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