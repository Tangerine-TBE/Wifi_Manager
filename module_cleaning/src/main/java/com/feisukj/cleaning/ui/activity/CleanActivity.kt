package com.feisukj.cleaning.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
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
import com.feisukj.cleaning.adapter.CleanAdapter_
import com.feisukj.cleaning.bean.GarbageBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.ui.fragment.CleanFragment
import com.feisukj.cleaning.utils.getSizeString
import kotlinx.android.synthetic.main.act_clean_clean.*
class CleanActivity : BaseActivity2(), BaseSectionAdapter.ItemSelectListener<TitleBean_Group,GarbageBean> {
    private lateinit var adapter:CleanAdapter_
    private val stack=HashSet<GarbageBean>()
    private var anim = ScaleAnimation(0f,1f,1f,1f, Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0.5f)

    override fun getLayoutId()=R.layout.act_clean_clean

    override fun isActionBar(): Boolean = false

    override fun initView() {
//        setContentText(R.string.assuredClean)
        immersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()
        adapter= CleanAdapter_(CleanFragment.adapterData)
        adapter.itemSelectListener=this
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.adapter=adapter
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.bottom=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,6.5f,resources.displayMetrics).toInt()
            }
        })

        CleanFragment.adapterData.toList().forEach {
            if (it.groupData?.isCheck==true){
                onSelectHeader(true,it)
            }
        }
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
            setResult(200)
            val data=ArrayList(adapter.getData())
            val thread=Thread{
                for (subItem in stack){
                    subItem.getItems().toTypedArray().forEach { garbageItemBean ->
                        garbageItemBean.getFiles().forEach {
                            if(!BuildConfig.DEBUG) {
                                it.delete()
                            }
                        }
                    }
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
            }
            if (!loadingDialog.isShowing) {
                AlertDialog.Builder(this)
                        .setTitle(R.string.deleteFile)
                        .setMessage(getString(R.string.askDelete_).format(clean.text))
                        .setNegativeButton(R.string.no) { dialog, which -> }
                        .setPositiveButton(R.string.yes) { dialog, which ->
                            loadingDialog.setCancelable(false)
                            loadingDialog.setTitleText("正在删除")
                            loadingDialog.show()
                            val intent= Intent(this,CompleteActivity::class.java)
                            var size=0L
                            stack.forEach { bean ->
                                size+=bean.fileSize
                            }
                            intent.putExtra(CompleteActivity.SIZE_KEY,size)
                            startActivity(intent)
                            setResult(CleanFragment.CLEAN_ALREADY_CODE)
                            thread.start()
                            finish()
                        }
                        .show()
            }else{
                Toast.makeText(this,R.string.runDelete, Toast.LENGTH_SHORT).show()
            }
        }
        goBack.setOnClickListener {
            finish()
        }
    }
    override fun onSelectHeader(isSelect: Boolean, treeData: SectionData<TitleBean_Group, GarbageBean>) {
        if (isSelect) {
            stack.addAll(treeData.getItemData())
        }else{
            stack.removeAll(treeData.getItemData())
        }
        upText()
    }

    override fun onSelectSubItem(isSelect: Boolean, treeData: SectionData<TitleBean_Group, GarbageBean>, subItem: GarbageBean) {
        if (isSelect) {
            stack.add(subItem)
        }else{
            stack.remove(subItem)
        }
        upText()
    }

    override fun onClickSubItem(treeData: SectionData<TitleBean_Group, GarbageBean>, subItem: GarbageBean) {

    }

    @SuppressLint("SetTextI18n")
    private fun upText(){
        if(stack.isEmpty()){
            bottomView.visibility=View.GONE
            clean.text=getString(R.string.clean)
        }else{
            var size=0L
            stack.forEach { bean ->
                size+=bean.fileSize
            }
            clean.text= getString(R.string.cleanVar,getSizeString(size))
            bottomView.visibility=View.VISIBLE
        }
    }
}