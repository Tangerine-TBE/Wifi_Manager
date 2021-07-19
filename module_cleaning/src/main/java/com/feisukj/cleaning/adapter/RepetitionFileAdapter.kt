package com.feisukj.cleaning.adapter

import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.RecyclerViewHolder
import com.example.module_base.cleanbase.SectionData

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.*
import com.feisukj.cleaning.utils.getSizeString

class RepetitionFileAdapter(data: List<SectionData<TitleBean_Group, FileBean>>):
    BaseSectionAdapter<TitleBean_Group, FileBean>(data) {
    var adView: FrameLayout?=null

    override fun getItemCount(): Int {
        return super.getItemCount()//+if (adView==null) 0 else 1
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
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_title_clean,parent,false)
        return RecyclerViewHolder(view)
    }

    override fun onCreateSubItemViewHolder(parent: ViewGroup): RecyclerViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_repetition_file_clean,parent,false)
        return RecyclerViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerViewHolder, groupItem: TitleBean_Group?, treeData: SectionData<TitleBean_Group, FileBean>) {
        if (groupItem==null)
            return
        holder.setText(R.id.title,groupItem.title)
        holder.setImageSelect(R.id.foldIcon,treeData.isFold)
        holder.setImageSelect(R.id.allChoose,groupItem.isCheck)
        val totalSize=treeData.getItemData().map { it.fileSize }.sum()
        holder.setText(R.id.groupSize, getSizeString(totalSize,1))
        holder.getView<ImageView>(R.id.allChoose).setOnClickListener { view ->
            view.isSelected=!view.isSelected
            groupItem.isCheck=view.isSelected
            treeData.getItemData().forEach {
                it.isCheck=view.isSelected
            }
            if (!treeData.isFold){
                notifyItemRangeChanged(treeData.groupPosition+1,treeData.getItemData().size)
            }
            itemSelectListener?.onSelectHeader(view.isSelected,treeData)

            //通知全选
//            if (view.isSelected){
//                if (data.all { it.groupData?.isCheck==groupItem.isCheck }){
//                    allChooserListener?.onChooserChange(groupItem.isCheck)
//                }
//            }else{
//                allChooserListener?.onChooserChange(false)
//            }
        }
    }

    override fun onBindSubItemViewHolder(holder: RecyclerViewHolder, treeData: SectionData<TitleBean_Group, FileBean>, subItem: FileBean) {
        if (treeData.getItemData().minBy { it.fileName.length }==subItem){
            holder.getView<View>(R.id.goodPicture).visibility=View.VISIBLE
        }else{
            holder.getView<View>(R.id.goodPicture).visibility=View.GONE
        }
        when(subItem){
            is AllFileBean ->{
                holder.setImage(R.id.repetitionFileIcon,subItem.fileIcon)
            }
            is ImageBean ->{
                holder.setImage(R.id.repetitionFileIcon,subItem.absolutePath)
            }
            is AppBean ->{
                subItem.icon?.get().let {
                    if (it==null){
                        holder.setImage(R.id.repetitionFileIcon,android.R.mipmap.sym_def_app_icon)
                    }else{
                        holder.setImage(R.id.repetitionFileIcon,it)
                    }
                }
            }
        }
        holder.setImageSelect(R.id.imageSelect,subItem.isCheck)
        holder.setText(R.id.describe, subItem.fileName)
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

    override fun onFold(isFold: Boolean, treeData: SectionData<TitleBean_Group, FileBean>) {
        super.onFold(isFold, treeData)
        notifyItemChanged(treeData.groupPosition)
    }

    fun removeAll(list:Collection<FileBean>){
        var i=0
        while (i<data.size){
            val subItems=data[i].getItemData()
            data[i].removeAllItem(list)
            if (subItems.isEmpty()){
                data.removeAt(i)
            }else{
                i++
            }
        }
        notifyDataSetChanged()
    }
}