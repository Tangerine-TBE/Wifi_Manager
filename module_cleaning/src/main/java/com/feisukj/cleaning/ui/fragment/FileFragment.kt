package com.feisukj.cleaning.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import android.widget.ViewSwitcher
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_ad.advertisement.AdType
import com.example.module_ad.advertisement.FeedHelper
import com.example.module_base.cleanbase.BaseConstant
import com.example.module_base.cleanbase.BaseFragment
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.SectionData

import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.PhotoAndFileAdapter_
import com.feisukj.cleaning.bean.AllFileBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.file.DirNextFileCallback
import com.feisukj.cleaning.file.FileManager
import com.feisukj.cleaning.ui.UIConst
import com.feisukj.cleaning.utils.toAppOpenFile
import com.feisukj.cleaning.view.SmallLoading
import kotlinx.android.synthetic.main.frag_qq_and_we_file_clean.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class FileFragment : BaseFragment(), BaseSectionAdapter.ItemSelectListener<TitleBean_Group,AllFileBean>, BaseSectionAdapter.AllChooserListener{
    companion object {
        private const val IS_GRID_KEY="is_grid_key"
        private const val PATH_KEY="path"
        private const val TITLE_RES_KEY="title_key"
        private const val FILE_FORMAT_KEY="file_format_key"

        fun getInstance(paths:List<String>, titleRes:Int, isGrid:Boolean=false,fileFormat:List<String>?=null):FileFragment{
            val f=FileFragment()
            val bundle= Bundle()
            bundle.putStringArrayList(PATH_KEY, ArrayList(paths))
            bundle.putBoolean(IS_GRID_KEY,isGrid)
            bundle.putInt(TITLE_RES_KEY,titleRes)
            fileFormat?.let {
                bundle.putStringArrayList(FILE_FORMAT_KEY, ArrayList(it))
            }
            f.arguments=bundle
            return f
        }
    }
    private var stack=HashSet<AllFileBean>()
    private var adapter: PhotoAndFileAdapter_?=null
    override fun getLayoutId()= R.layout.frag_qq_and_we_file_clean

    private val feedHelper by lazy {
        FeedHelper(activity,FrameLayout(requireContext()).also { adapter?.adView=it })
    }

    override fun initView() {
        super.initView()
        loadingViewSwitch(true,chooseViewSwitch)
        val path=arguments?.getStringArrayList(PATH_KEY)
        arguments?.getInt(TITLE_RES_KEY)?.let {
            docTitleTip.setText(it)
        }

        val isGrid=arguments?.getBoolean(IS_GRID_KEY)
        val layoutManager =if (isGrid!=true) {
            LinearLayoutManager(context)
        }else{
            GridLayoutManager(context, 4)
        }
        recyclerView.itemAnimator=null
        recyclerView.layoutManager=layoutManager
        adapter= PhotoAndFileAdapter_(listOf(),isGrid==true){item ->
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
        adapter?.itemSelectListener=this
        adapter?.allChooserListener=this
        recyclerView.adapter=adapter
        activity?.let { activity ->
            feedHelper.showAd(AdType.ALBUM_VIDEO_MUSIC_FILE_PACKAGE_PAGE)
        }
        initClick()
        GlobalScope.launch{
            path?.let { requestData(it) }
            activity?.runOnUiThread {
                try{
                    docTitleCount.text="${adapter?.getData()?.map { it.getItemData().size }?.sum()?:0}项"
                    adapter?.getData()?.firstOrNull()?.let {
                        it.isFold=false
                        adapter?.changeHeaderItem(it)
                    }
                    if (adapter?.getData()?.isEmpty()!!){
                        noData.visibility = View.VISIBLE
                    }else{
                        noData.visibility = View.GONE
                    }
                    recyclerView.scrollToPosition(0)
                    adapter?.isFinished = true
                    for ((key,value) in adapter!!.map){
                        if (value.currentView.id== R.id.load)
                            value.showNext()
                    }
                    if (chooseViewSwitch.currentView == load){
                        chooseViewSwitch.showNext()
                    }
                }catch (e:java.lang.Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun initClick(){
        clean.setOnClickListener {
            val data=ArrayList(adapter?.getData()?:return@setOnClickListener)
            val cleanStack=stack.toList()
            val thread=Thread{
                for (subItem in cleanStack){
                    File(subItem.absolutePath).delete()
                }
                BaseConstant.mainHandler.post {
                    var i=0
                    while (i<data.size&&i>=0){
                        val subItems=data[i].getItemData()
                        data[i].removeAllItem(cleanStack)
                        if (subItems.isEmpty()){
                            data.removeAt(i)
                        }else{
                            i++
                        }
                    }
                    stack.removeAll(cleanStack)
                    upText()
                    dismissLoading()
                    adapter?.setData(data)
                }
            }
            if (!loadingDialog.isShowing&&isAdded) {
                androidx.appcompat.app.AlertDialog.Builder(context!!)
                        .setTitle(R.string.deleteFile)
                        .setMessage(getString(R.string.askDelete).format(stack.size.toString()))
                        .setNegativeButton(R.string.no) { dialog, which -> }
                        .setPositiveButton(R.string.yes) { dialog, which ->
                            loadingDialog.setCancelable(false)
                            loadingDialog.setTitleText("正在删除...")
                            loadingDialog.show()
                            try {
                                thread.start()
                            }catch (e: Exception){

                            }
                        }
                        .show()
            }else{
                Toast.makeText(context,R.string.runDelete, Toast.LENGTH_SHORT).show()
            }
        }
        allChoose.setOnClickListener {view->
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

    private fun requestData(paths:List<String>){
        val f=paths.map { File(it) }

        val format=arguments?.getStringArrayList(FILE_FORMAT_KEY)

        var count=0
        FileManager.scanDirsFile(f,{
            AllFileBean(it).apply {
//                this.group=this.year *12+ this.month
                val currentDate= Date(this.fileLastModified)
                this.group = when{
                    UIConst.monthWithinTime.before(currentDate)->{
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
            }
        },{file->
            format==null||format.any { file.name.endsWith(it,true) }
        },object :DirNextFileCallback<AllFileBean>{
            override fun onNextFile(item: AllFileBean) {
                count++
                activity?.runOnUiThread {
                    if (!isAdded){
                        return@runOnUiThread
                    }
                    docTitleCount.text="${count}项"
                    adapter?.addItem(item)
                }
            }
        },isStop = {!isAdded})
    }

    @SuppressLint("SetTextI18n")
    private fun upText(){
        if (stack.isNullOrEmpty()){
            bottomButton?.visibility= View.GONE
        }else{
            bottomButton?.visibility= View.VISIBLE
            clean.text=resources.getString(R.string.cleanFielTip).format(stack.size.toString())
        }
    }

    override fun onChooserChange(isAllSelector: Boolean) {
        allChoose.isSelected=isAllSelector
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

    private fun loadingViewSwitch(isSwitch:Boolean,switchView: ViewSwitcher){
        val currentView=switchView.currentView
        if (isSwitch){
            if (currentView is SmallLoading){
                currentView
            }else{
                switchView.showNext()
                switchView.currentView as? SmallLoading
            }?.startAnim()
        }else{
            if (currentView is SmallLoading){
                switchView.showNext()
                currentView
            }else{
                switchView.nextView as? SmallLoading
            }?.stopAnim()
        }
    }
}