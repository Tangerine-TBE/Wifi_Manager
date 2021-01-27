package com.feisukj.cleaning.adapter

import android.content.Context
import android.os.Build
import android.os.Handler
import android.text.format.Formatter
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ViewSwitcher
import androidx.annotation.RequiresApi
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.RecyclerViewHolder
import com.example.module_base.cleanbase.SectionData

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.*
import com.feisukj.cleaning.utils.Constant
import com.feisukj.cleaning.view.SmallLoading

class AbsPhotoAdapter<T:FileBean>(data:List<SectionData<TitleBean_Group, T>>, private val section:(T)->SectionData<TitleBean_Group,T>): BaseSectionAdapter<TitleBean_Group, T>(data) {
    private lateinit var mContext :Context
    private var list = ArrayList<Int>()
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    var map = ArrayMap<Int,ViewSwitcher>()
    var isFinished = false
    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerViewHolder {
        mContext = parent.context
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_title_clean,parent,false)
        return RecyclerViewHolder(view)
    }

    override fun onCreateSubItemViewHolder(parent: ViewGroup): RecyclerViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_pic_clean, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerViewHolder, groupItem: TitleBean_Group?, treeData: SectionData<TitleBean_Group, T>) {
        if (groupItem==null)
            return
        holder.setText(R.id.title,groupItem.title)
        holder.setImageSelect(R.id.foldIcon,treeData.isFold)
        holder.setImageSelect(R.id.allChoose,groupItem.isCheck)
//        val image = holder.getView<ImageView>(R.id.title_pic)
//        Glide.with(mContext).load(treeData.getItemData()[0].absolutePath).into(image)
//        if (!treeData.isEnd){
//            if (list.isEmpty()|| treeData.id !in list){
//                loadingViewSwitch(true,holder.getView(R.id.chooseViewSwitch))
//                list.add(treeData.id)
//            }
//        }else{
//            if (list2.isEmpty()|| treeData.id !in list2){
//                holder.getView<ViewSwitcher>(R.id.chooseViewSwitch).showNext()
//                list2.add(treeData.id)
//            }
//        }
//        if (list.isEmpty()|| treeData.id !in list){
//            loadingViewSwitch(true,holder.getView(R.id.chooseViewSwitch))
//            list.add(treeData.id)
//        }
//        val viewSwitcher=holder.getView<ViewSwitcher>(R.id.chooseViewSwitch)
//        if (isFinished){
//            if (viewSwitcher.currentView.id==R.id.load4) {
//                holder.getView<ViewSwitcher>(R.id.chooseViewSwitch).showNext()
//            }
//        }else{
//            if (viewSwitcher.currentView.id!=R.id.load4) {
//                holder.getView<ViewSwitcher>(R.id.chooseViewSwitch).showNext()
//            }
//        }
//        map[treeData.id] = holder.getView(R.id.chooseViewSwitch)
        val totalSize=treeData.getItemData().map {
            try {
                it.fileSize
            }catch (e: Exception){
                0L
            }
        }.sum()
        holder.setText(R.id.groupSize, Formatter.formatFileSize(holder.itemView.context,totalSize))
//        holder.setText(R.id.fileNum,""+treeData.getItemData().size+"个文件")
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
//            if (view.isSelected){
//                if (data.toList().all { it.groupData?.isCheck==groupItem.isCheck }){
//                    allChooserListener?.onChooserChange(groupItem.isCheck)
//                }
//            }else{
//                allChooserListener?.onChooserChange(false)
//            }
        }
    }

    override fun onBindSubItemViewHolder(holder: RecyclerViewHolder, treeData: SectionData<TitleBean_Group, T>, subItem: T) {
        holder.getView<ImageView>(R.id.foreground).also { imageView ->
            if (Constant.VIDEO_FORMAT.any { subItem.fileName.endsWith(it,true) }) {
                if (imageView.visibility != View.VISIBLE) {
                    imageView.visibility = View.VISIBLE
                }
            }else{
                if (imageView.visibility != View.GONE) {
                    imageView.visibility = View.GONE
                }
            }
        }
        holder.setImage(R.id.image,subItem.absolutePath)
        holder.setImageSelect(R.id.imageSelect,subItem.isCheck)
        holder.setText(R.id.describe, subItem.fileSizeString)
        holder.getView<ImageView>(R.id.imageSelect).setOnClickListener { view ->
            view.isSelected=!view.isSelected
            subItem.isCheck=view.isSelected
            if (treeData.getItemData().all { it.isCheck==view.isSelected }){
                treeData.groupData?.isCheck=view.isSelected
                notifyItemChanged(treeData.groupPosition)

                //通知全选
//                if (data.all { it.groupData?.isCheck==view.isSelected }){
//                    allChooserListener?.onChooserChange(view.isSelected)
//                }
            }else{
                if (treeData.groupData?.isCheck==true){
                    treeData.groupData?.isCheck=false
                    notifyItemChanged(treeData.groupPosition)

                    //通知全选
//                    if (data.any { it.groupData?.isCheck==view.isSelected }){
//                        allChooserListener?.onChooserChange(false)
//                    }
                }
            }
            itemSelectListener?.onSelectSubItem(view.isSelected,treeData,subItem)
        }
        holder.itemView.setOnClickListener {
            itemSelectListener?.onClickSubItem(treeData,subItem)
        }
    }

    override fun onAddHeaderData(treeData: SectionData<TitleBean_Group, T>) {
        if (data.size<=1||data[data.size-2].id<treeData.id){
            return
        }
        for (i in data.indices){
            val item=data[i]
            if (treeData.id<item.id){
                data.remove(treeData)
                data.add(i,treeData)
                notifyHeaderMoved(treeData,item.groupPosition)
                return
            }
        }
    }

    override fun onFold(isFold: Boolean, treeData: SectionData<TitleBean_Group, T>) {
        super.onFold(isFold, treeData)
        notifyItemChanged(treeData.groupPosition)
    }

    override fun addSubItem(treeData: SectionData<TitleBean_Group, T>, subItem: T) {
        super.addSubItem(treeData, subItem)
        changeHeaderItem(treeData)
    }

    fun addItem(subItem: T){
        data.find { subItem.group==it.id }.let {
            if (it==null){
                addHeaderItem(section.invoke(subItem))
            }else{
                addSubItem(it,subItem)
            }
        }
    }

    fun removeItem(subItem: T){
        data.find { subItem.group==it.id }.let {
            if (it!=null) {
                removeSubItem(it, subItem)
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