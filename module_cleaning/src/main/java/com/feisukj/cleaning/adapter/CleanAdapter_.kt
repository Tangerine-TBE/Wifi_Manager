package com.feisukj.cleaning.adapter

import android.text.format.Formatter
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.RecyclerViewHolder
import com.example.module_base.cleanbase.SectionData

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.GarbageBean
import com.feisukj.cleaning.bean.TitleBean_Group

class CleanAdapter_(data: List<SectionData<TitleBean_Group, GarbageBean>>): BaseSectionAdapter<TitleBean_Group, GarbageBean>(data) {
    var adView:View?=null

    override fun getItemCount(): Int {
        return super.getItemCount()+1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position==itemCount-1){
            -1
        }else{
            super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return if (viewType==-1){
            adView.let { view ->
                if (view==null){
                    RecyclerViewHolder(View(parent.context))
                }else{
                    val linearLayout=LinearLayout(parent.context)
                    view.parent?.let {
                        if (it is ViewGroup){
                            it.removeAllViews()
                        }
                    }
                    linearLayout.layoutParams=LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                    linearLayout.orientation=LinearLayout.VERTICAL
                    linearLayout.gravity=Gravity.CENTER_HORIZONTAL
                    linearLayout.addView(view)
                    RecyclerViewHolder(linearLayout)
                }
            }
        }else{
            super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_clean_title_clean,parent,false)
        return RecyclerViewHolder(view)
    }

    override fun onCreateSubItemViewHolder(parent: ViewGroup): RecyclerViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_clean_subitem_clean,parent,false)
        return RecyclerViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerViewHolder, groupItem: TitleBean_Group?, treeData: SectionData<TitleBean_Group, GarbageBean>) {
        if (groupItem==null)
            return
        holder.getView<TextView>(R.id.describe).text=Formatter.formatFileSize(holder.itemView.context,treeData.getItemData().map { it.fileSize }.sum())

        holder.setText(R.id.title1,groupItem.title)
        holder.getView<View>(R.id.icon).isSelected=!treeData.isFold
        holder.setImageSelect(R.id.allChoose,groupItem.isCheck)
        groupItem.icon?.let { holder.setImage(R.id.img, it) }
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
        }
    }

    override fun onBindSubItemViewHolder(holder: RecyclerViewHolder, treeData: SectionData<TitleBean_Group, GarbageBean>, subItem: GarbageBean) {
        if (!subItem.title.isNullOrEmpty()){
            holder.setText(R.id.label, subItem.title)
        }else{
            holder.setText(R.id.label,subItem.label)
        }
        subItem.icon.let {
            if (it!=null) {
                holder.setImage(R.id.icon, it)
            }else{
                holder.setImage(R.id.icon,R.mipmap.icon_wjj)
            }
        }
        if (treeData.groupData?.isCheck==true){
            subItem.isCheck=true
        }
        holder.setImageSelect(R.id.select,subItem.isCheck)
        holder.setText(R.id.describe,Formatter.formatFileSize(holder.itemView.context,subItem.fileSize))

        holder.getView<View>(R.id.select).setOnClickListener { view ->
            subItem.isCheck=!subItem.isCheck
            holder.setImageSelect(R.id.select, subItem.isCheck)
            if (treeData.getItemData().all { it.isCheck==subItem.isCheck }){
                treeData.groupData?.isCheck=subItem.isCheck
                notifyItemChanged(treeData.groupPosition)
            }else{
                if (treeData.groupData?.isCheck==true){
                    treeData.groupData?.isCheck=false
                    notifyItemChanged(treeData.groupPosition)
                }else {
                    notifyItemChanged(treeData.groupPosition)//若选择子项,不会引起父项改变则该行代码可以删除
                }
            }
            itemSelectListener?.onSelectSubItem(subItem.isCheck,treeData,subItem)
        }
        holder.itemView.setOnClickListener {
            itemSelectListener?.onClickSubItem(treeData,subItem)
        }
    }

    override fun changeSubItem(treeData: SectionData<TitleBean_Group, GarbageBean>, subItem: GarbageBean) {
        super.changeSubItem(treeData, subItem)
        changeHeaderItem(treeData)
    }

    override fun addSubItem(treeData: SectionData<TitleBean_Group, GarbageBean>, subItem: GarbageBean) {
        super.addSubItem(treeData, subItem)
        changeHeaderItem(treeData)
    }
}