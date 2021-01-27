package com.feisukj.cleaning.adapter

import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.RecyclerViewHolder
import com.example.module_base.cleanbase.SectionData

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.AppBean
import com.feisukj.cleaning.bean.TitleBean_Group

abstract class ApkAndAppAdapter(data: List<SectionData<TitleBean_Group, AppBean>>): BaseSectionAdapter<TitleBean_Group, AppBean>(data){
    var allChooserListener:AllChooserListener?=null
    var adView: FrameLayout?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        if (viewType== UN_KNOW_VIEW_TYPE){
            val frameLayout= FrameLayout(parent.context)
            frameLayout.layoutParams= ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            adView?.also {
                val p=it.parent
                if (p is ViewGroup){
                    p.removeAllViews()
                }
                it.layoutParams= FrameLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300f,parent.context.resources.displayMetrics).toInt()
                        , ViewGroup.LayoutParams.WRAP_CONTENT
                        , Gravity.CENTER_HORIZONTAL)
                frameLayout.addView(it)
            }
            return RecyclerViewHolder(frameLayout)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return super.getItemCount()+if (adView==null) 0 else 1
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_software_title_clean,parent,false)
        return RecyclerViewHolder(view)
    }

    override fun onCreateSubItemViewHolder(parent: ViewGroup): RecyclerViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_software_clean,parent,false)
        return RecyclerViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerViewHolder, groupItem: TitleBean_Group?, treeData: SectionData<TitleBean_Group, AppBean>) {
        groupItem?:return
        if (data.indexOf(treeData)!=0){
            holder.getView<View>(R.id.itemDecoration).visibility=View.VISIBLE
        }else{
            holder.getView<View>(R.id.itemDecoration).visibility=View.GONE
        }
        holder.setText(R.id.titleSoftware,groupItem.title)
        holder.getView<View>(R.id.allChoose).setOnClickListener {view->
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

}