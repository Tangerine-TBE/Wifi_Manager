package com.feisukj.cleaning.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.format.Formatter
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.SectionData
import com.example.module_base.widget.LoadingDialog


import com.feisukj.cleaning.BuildConfig
import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.UnloadingResidueAdapter
import com.feisukj.cleaning.bean.AllFileBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.utils.toAppOpenFile
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_unloading_residue_clean.*
import kotlinx.android.synthetic.main.item_action_bar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

open class UnloadingResidueActivity :FragmentActivity(), BaseSectionAdapter.ItemSelectListener<TitleBean_Group,AllFileBean>{

    private val deleteStack= HashSet<AllFileBean>()
    private var adapter:UnloadingResidueAdapter?=null
    private var anim = ScaleAnimation(0f,1f,1f,1f, Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0.5f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unloading_residue_clean)
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .init()
        goBack.setOnClickListener { finish() }
        titleText.text="残留文件"
        val data=PhoneLoseActivity.unloadingData?: emptyList()
        unloadingRecycleView.layoutManager= LinearLayoutManager(this)
        adapter=UnloadingResidueAdapter(data)
        unloadingRecycleView.adapter= adapter
        adapter?.itemSelectListener=this

        adapter?.getData()?.forEach { sectionData ->
            sectionData.groupData?.isCheck=true
            onSelectHeader(true,sectionData)
            sectionData.getItemData().forEach {
                it.isCheck=true
            }
        }
        adapter?.notifyDataSetChanged()

        anim.apply {
            this.duration = 1500
            this.interpolator = LinearInterpolator()
            this.repeatMode = Animation.RESTART
            this.repeatCount = -1
        }
        img_anim.animation = anim

        tabClean.setOnClickListener {
            if (deleteStack.isEmpty()){
                return@setOnClickListener
            }
            val loadingDelete= LoadingDialog(this)
                    .setCancelable(false)
                    .setTitleText("正在删除...")
            loadingDelete.show()
            GlobalScope.launch {
                for (c in deleteStack) {
                    if (!BuildConfig.DEBUG){
                        File(c.absolutePath).delete()
                    }
                }

                runOnUiThread {
                    runOnUiThread {
                        adapter?.removeAllItem(deleteStack.toList())
                    }
                    loadingDelete.dismiss()
                    upText()
                }
            }
        }

        val f= FrameLayout(this).apply { this.layoutParams= ViewGroup.LayoutParams((resources.displayMetrics.density*300).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT) }
        adapter?.adView=f

    }

    private fun upText(){
        if (deleteStack.isNullOrEmpty()){
            bottomButton?.visibility= View.GONE
        }else{
            val s=deleteStack.map { it.fileSize }.sum()
            bottomButton?.visibility= View.VISIBLE
            tabClean.text="删除${Formatter.formatFileSize(this,s)}，${deleteStack.size}个文件"
        }
    }

    override fun onSelectHeader(isSelect: Boolean, treeData: SectionData<TitleBean_Group, AllFileBean>) {
        if (isSelect) {
            deleteStack.addAll(treeData.getItemData())
        }else{
            deleteStack.removeAll(treeData.getItemData())
        }
        upText()
    }

    override fun onSelectSubItem(isSelect: Boolean, treeData: SectionData<TitleBean_Group, AllFileBean>, subItem: AllFileBean) {
        if (isSelect) {
            deleteStack.add(subItem)
        }else{
            deleteStack.remove(subItem)
        }
        upText()
    }

    override fun onClickSubItem(treeData: SectionData<TitleBean_Group, AllFileBean>, subItem: AllFileBean) {
        toAppOpenFile(this, File(subItem.absolutePath))
    }
}