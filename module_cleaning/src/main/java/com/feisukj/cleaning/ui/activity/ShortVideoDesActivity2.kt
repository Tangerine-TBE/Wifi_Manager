package com.feisukj.cleaning.ui.activity

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.module_ad.advertisement.AdType
import com.example.module_ad.advertisement.FeedHelper
import com.example.module_base.cleanbase.BaseActivity2
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.SectionData


import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.ShortVideoAdapter_
import com.feisukj.cleaning.bean.ImageBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.file.ShortVideoPath
import com.feisukj.cleaning.presenter.ShortVideoHelper
import com.feisukj.cleaning.utils.formatFileSize
import com.feisukj.cleaning.utils.getIcon
import com.feisukj.cleaning.utils.getLabel
import com.feisukj.cleaning.utils.toAppOpenFile
import kotlinx.android.synthetic.main.act_shortvideodes2_clean.*
import kotlinx.android.synthetic.main.item_bottom_sing_clean.*
import java.io.File

class ShortVideoDesActivity2: BaseActivity2(),ShortVideoHelper.ShortVideoCallBack,
    BaseSectionAdapter.ItemSelectListener<TitleBean_Group,ImageBean> ,BaseSectionAdapter.AllChooserListener{
    override fun isActionBar(): Boolean {
        return false
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

    private val stack:HashSet<ImageBean> by lazy { HashSet<ImageBean>() }
    private var garbageSize=0L
        set(value) {
            if (field!=value){
                field=value
                val p= formatFileSize(this,field)
                textCount.text=p.first
                unit.text=p.second
            }
        }

    private var adapter: ShortVideoAdapter_?=null

    override fun getLayoutId()= R.layout.act_shortvideodes2_clean

    override fun initView() {
        setContentText(R.string.ShortVideoSP)
        immersionBar.statusBarColor(android.R.color.transparent).init()
        val adapterData= ArrayList<SectionData<TitleBean_Group, ImageBean>>()
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        adapter= ShortVideoAdapter_(adapterData)
        adapter?.itemSelectListener=this
        adapter?.allChooserListener=this
        recyclerView.adapter=adapter
        ShortVideoHelper.instance.requestData(this)

      //  val f= FrameLayout(this).apply { this.layoutParams= ViewGroup.LayoutParams((resources.displayMetrics.density*300).toInt(),ViewGroup.LayoutParams.WRAP_CONTENT) }
      //  adapter?.adView=f

        feedHelper.showAd(AdType.WX_QQ_SHORTVIDEO_PACKAGE_PICTURE_PAGE)
    }

    private val feedHelper by lazy {
        FeedHelper(this,FrameLayout(this).also { adapter?.adView=it })
    }

    override fun initListener() {
        back.setOnClickListener {
            finish()
        }
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
                    garbageSize-=stack.map { it.fileSize }.sum()
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
                        .setNegativeButton(R.string.no) { _, _ -> }
                        .setPositiveButton(R.string.yes) { _, _ ->
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

//        allSelector.setOnClickListener {view->
//            view.isSelected=!view.isSelected
//            if (adapter?.getData()?.any { it.groupData?.isCheck!=view.isSelected }==true){
//                adapter?.getData()?.forEach { sectionData ->
//                    sectionData.groupData?.isCheck=view.isSelected
//                    onSelectHeader(view.isSelected,sectionData)
//                    sectionData.getItemData().forEach {
//                        it.isCheck=view.isSelected
//                    }
//                }
//                adapter?.notifyDataSetChanged()
//            }
//        }
    }

    override fun onChooserChange(isAllSelector: Boolean) {
//        allSelector.isSelected=isAllSelector
    }

    override fun onShortVideo(files: List<File>, shortVideoData: ShortVideoPath) {
        if (files.isEmpty())
            return
        val itemData=SectionData<TitleBean_Group,ImageBean>()

        val title=TitleBean_Group()
        title.title= getLabel(shortVideoData.packageName)?:shortVideoData.appName?:(getString(R.string.unknow))
        title.iconD= getIcon(shortVideoData.packageName)
        itemData.groupData=title
        title.isCheck=true

        files.forEach {
            title.itemSize+=it.length()
            val imageBean=ImageBean(it)
            imageBean.isCheck=true
            itemData.addItem(imageBean)
            garbageSize+=it.length()
        }
        stack.addAll(itemData.getItemData())
        adapter?.addHeaderItem(itemData)
    }

    override fun onShortVideoComplete() {
        val item=adapter?.getData()?.findLast {
            it.getItemData().isNotEmpty()
        }
        item?.isFold=false
        adapter?.notifyDataSetChanged()
        upText()
    }

    private fun upText(){
        if (stack.isNullOrEmpty()){
            bottomButton.visibility=View.GONE
        }else{
            bottomButton.visibility=View.VISIBLE
            clean.text=String.format(resources.getString(R.string.deleteShortVideoC),stack.size.toString())
        }
    }
}