package com.feisukj.cleaning.adapter

import android.widget.ImageView
import com.example.module_base.cleanbase.RecyclerViewHolder
import com.example.module_base.cleanbase.SectionData

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.ApkSectionData
import com.feisukj.cleaning.bean.AppBean
import com.feisukj.cleaning.bean.TitleBean_Group

class ApkAdapter(data: List<ApkSectionData<TitleBean_Group, AppBean>>):ApkAndAppAdapter(data) {

    override fun onBindHeaderViewHolder(holder: RecyclerViewHolder, groupItem: TitleBean_Group?, treeData: SectionData<TitleBean_Group, AppBean>) {
        groupItem?:return
        super.onBindHeaderViewHolder(holder, groupItem, treeData)
        holder.setText(R.id.childCount,"(${treeData.getItemData().size})")
        holder.setImageSelect(R.id.allChoose,groupItem.isCheck)
    }

    override fun onBindSubItemViewHolder(holder: RecyclerViewHolder, treeData: SectionData<TitleBean_Group, AppBean>, subItem: AppBean) {
        holder.setText(R.id.name,subItem.label)
        subItem.icon.let { reference ->
            if (reference==null){
                holder.setImage(R.id.icon,R.drawable.ic_apk_no_icon)
            }else{
                reference.get().let {
                    if (it==null){
                        holder.setImage(R.id.icon,R.drawable.ic_apk_no_icon)
                    }else{
                        holder.setImage(R.id.icon,it)
                    }
                }
            }
        }
        holder.setText(R.id.installDes,if (subItem.isInstall) R.string.alreadyInstalled else R.string.NotInstalled)
        val describe=holder.itemView.context.getString(R.string.size).format(subItem.fileSizeString)
        holder.setText(R.id.describe,describe)
        holder.getView<ImageView>(R.id.select).isSelected = subItem.isCheck

        holder.getView<ImageView>(R.id.select).setOnClickListener { view ->
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

    override fun addSubItem(treeData: SectionData<TitleBean_Group, AppBean>, subItem: AppBean) {
        super.addSubItem(treeData, subItem)
        notifyItemChanged(treeData.groupPosition)
    }
}