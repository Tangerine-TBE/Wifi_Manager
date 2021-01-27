package com.feisukj.cleaning.adapter

import android.content.Context
import android.os.Build
import android.text.format.Formatter
import android.util.ArrayMap
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ViewSwitcher
import androidx.annotation.RequiresApi
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.RecyclerViewHolder
import com.example.module_base.cleanbase.SectionData

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.AllFileBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.ui.UIConst
import com.feisukj.cleaning.utils.Constant
import com.feisukj.cleaning.view.SmallLoading
import java.lang.Exception
import java.util.*

class PhotoAndFileAdapter_(data: List<SectionData<TitleBean_Group, AllFileBean>>, private val isPicture:Boolean=false, private val section:(AllFileBean)->SectionData<TitleBean_Group,AllFileBean>):
    BaseSectionAdapter<TitleBean_Group, AllFileBean>(data) {
    var allChooserListener:AllChooserListener?=null
    var adView: FrameLayout?=null

    private var list = ArrayList<Int>()
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    var map = ArrayMap<Int,ViewSwitcher>()
    var isFinished = false

    override fun getItemCount(): Int {
        return super.getItemCount()+if (adView==null) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        if (viewType== UN_KNOW_VIEW_TYPE){
            val frameLayout=FrameLayout(parent.context)
            frameLayout.layoutParams=ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            adView?.also {
                val p=it.parent
                if (p is ViewGroup){
                    p.removeAllViews()
                }
                it.layoutParams=FrameLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300f,parent.context.resources.displayMetrics).toInt()
                        ,ViewGroup.LayoutParams.WRAP_CONTENT
                        , Gravity.CENTER_HORIZONTAL)
                frameLayout.addView(it)
            }
            return RecyclerViewHolder(frameLayout)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_title_clean_2,parent,false)
        return RecyclerViewHolder(view)
    }

    override fun onCreateSubItemViewHolder(parent: ViewGroup): RecyclerViewHolder {
        val view=
                if (isPicture) {
                    LayoutInflater.from(parent.context).inflate(R.layout.item_pic_clean, parent, false)
                }else{
                    LayoutInflater.from(parent.context).inflate(R.layout.item_file_clean, parent, false)
                }
        return RecyclerViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerViewHolder, groupItem: TitleBean_Group?, treeData: SectionData<TitleBean_Group, AllFileBean>) {
        if (groupItem==null)
            return
        holder.setText(R.id.title,groupItem.title)
        holder.setImageSelect(R.id.foldIcon,treeData.isFold)
        holder.setImageSelect(R.id.allChoose,groupItem.isCheck)
        if (list.isEmpty()|| treeData.id !in list){
            loadingViewSwitch(true,holder.getView(R.id.chooseViewSwitch))
            list.add(treeData.id)
        }
        val viewSwitcher=holder.getView<ViewSwitcher>(R.id.chooseViewSwitch)
        if (isFinished){
            if (viewSwitcher.currentView.id==R.id.load) {
                holder.getView<ViewSwitcher>(R.id.chooseViewSwitch).showNext()
            }
        }else{
            if (viewSwitcher.currentView.id!=R.id.load) {
                holder.getView<ViewSwitcher>(R.id.chooseViewSwitch).showNext()
            }
        }
        map[treeData.id] = holder.getView(R.id.chooseViewSwitch)
        val totalSize=treeData.getItemData().map {
            try {
                it.fileSize
            }catch (e:Exception){
                0L
            }
        }.sum()
        holder.setText(R.id.groupSize, Formatter.formatFileSize(holder.itemView.context,totalSize))
        holder.getView<ImageView>(R.id.allChoose).setOnClickListener { view ->
            view.isSelected=!view.isSelected
            groupItem.isCheck=view.isSelected
            treeData.getItemData().toList().forEach {
                it.isCheck=view.isSelected
            }
            if (!treeData.isFold){
                notifyItemRangeChanged(treeData.groupPosition+1,treeData.getItemData().size)
            }
            itemSelectListener?.onSelectHeader(view.isSelected,treeData)

            //通知全选
            if (view.isSelected){
                if (data.toList().all { it.groupData?.isCheck==groupItem.isCheck }){
                    allChooserListener?.onChooserChange(groupItem.isCheck)
                }
            }else{
                allChooserListener?.onChooserChange(false)
            }
        }
    }

    override fun onBindSubItemViewHolder(holder: RecyclerViewHolder, treeData: SectionData<TitleBean_Group, AllFileBean>, subItem: AllFileBean) {
        val selectId:Int = if (isPicture){
            holder.setImage(R.id.image,subItem.absolutePath)
            if (subItem.format in Constant.VIDEO_FORMAT) {
                holder.getView<ImageView>(R.id.foreground).also {
                    if (it.visibility != View.VISIBLE) {
                        it.visibility = View.VISIBLE
                    }
                }
            }else{
                holder.getView<ImageView>(R.id.foreground).also {
                    if (it.visibility != View.GONE) {
                        it.visibility = View.GONE
                    }
                }
            }
            holder.setImageSelect(R.id.imageSelect,subItem.isCheck)
            holder.setText(R.id.describe, Formatter.formatFileSize(holder.itemView.context,subItem.fileSize))
            R.id.imageSelect
        }else{
            holder.setImage(R.id.fileIcon,subItem.fileIcon)
            holder.setText(R.id.fileName,subItem.fileName)
            holder.setText(R.id.fileSize,subItem.fileSizeString)
            holder.setText(R.id.fileDate,subItem.fileDate)
            holder.setImageSelect(R.id.fileCheck,subItem.isCheck)
            R.id.fileCheck
        }
        holder.getView<ImageView>(selectId).setOnClickListener { view ->
            view.isSelected=!view.isSelected
            subItem.isCheck=view.isSelected
            if (treeData.getItemData().all { it.isCheck==view.isSelected }){
                treeData.groupData?.isCheck=view.isSelected
                notifyItemChanged(treeData.groupPosition)

                //通知全选
                if (data.all { it.groupData?.isCheck==view.isSelected }){
                    allChooserListener?.onChooserChange(view.isSelected)
                }
            }else{
                if (treeData.groupData?.isCheck==true){
                    treeData.groupData?.isCheck=false
                    notifyItemChanged(treeData.groupPosition)

                    //通知全选
                    if (data.any { it.groupData?.isCheck==view.isSelected }){
                        allChooserListener?.onChooserChange(false)
                    }
                }
            }
            itemSelectListener?.onSelectSubItem(view.isSelected,treeData,subItem)
        }
        holder.itemView.setOnClickListener {
            itemSelectListener?.onClickSubItem(treeData,subItem)
        }
    }

    override fun onFold(isFold: Boolean, treeData: SectionData<TitleBean_Group, AllFileBean>) {
        super.onFold(isFold, treeData)
        notifyItemChanged(treeData.groupPosition)
    }

    fun addItem(subItem: AllFileBean){
//        val itemData=data.find {
//            it.id==subItem.group
//        }?: SectionData<TitleBean_Group,AllFileBean>().apply {
//            val groupData=TitleBean_Group()
//            this.groupData=groupData
//            groupData.title="${subItem.year}年${subItem.month + 1}月"
//            this.id=subItem.group
//        }
//        addSubItem(itemData,subItem)
        val currentDate= Date(subItem.fileLastModified)
        subItem.group= when{
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
        data.find { subItem.group==it.id }.let {
            if (it==null){
                addHeaderItem(section.invoke(subItem))
            }else{
                addSubItem(it,subItem)
            }
        }
    }

    override fun addSubItem(treeData: SectionData<TitleBean_Group, AllFileBean>, subItem: AllFileBean) {
        super.addSubItem(treeData, subItem)
        changeHeaderItem(treeData)
    }

    override fun onAddHeaderData(treeData: SectionData<TitleBean_Group, AllFileBean>) {
        if (data.size<=1||data[data.size-2].id>treeData.id){
            return
        }
        for (i in data.indices){
            val item=data[i]
            if (treeData.id>item.id){
                data.remove(treeData)
                data.add(i,treeData)
                notifyHeaderMoved(treeData,item.groupPosition)
                return
            }
        }
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