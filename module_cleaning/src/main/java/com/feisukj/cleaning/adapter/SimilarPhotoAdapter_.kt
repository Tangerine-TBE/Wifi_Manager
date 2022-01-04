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
import com.feisukj.cleaning.bean.ImageBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.utils.Constant
import com.feisukj.cleaning.utils.getSizeString

class SimilarPhotoAdapter_(data: List<SectionData<TitleBean_Group, ImageBean>>):
    BaseSectionAdapter<TitleBean_Group, ImageBean>(data) {
    var allChooserListener:AllChooserListener?=null

    var adView: FrameLayout?=null

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
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_title_clean,parent,false)
        return RecyclerViewHolder(view)
    }

    override fun onCreateSubItemViewHolder(parent: ViewGroup): RecyclerViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_pic_similar_clean,parent,false)
        return RecyclerViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerViewHolder, groupItem: TitleBean_Group?, treeData: SectionData<TitleBean_Group, ImageBean>) {
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
            if (view.isSelected){
                if (data.all { it.groupData?.isCheck==groupItem.isCheck }){
                    allChooserListener?.onChooserChange(groupItem.isCheck)
                }
            }else{
                allChooserListener?.onChooserChange(false)
            }
        }
    }

    override fun onBindSubItemViewHolder(holder: RecyclerViewHolder, treeData: SectionData<TitleBean_Group, ImageBean>, subItem: ImageBean) {
        if (subItem.format in Constant.VIDEO_FORMAT) {
            holder.getView<ImageView>(R.id.foreground).also {
                if (it.visibility != View.VISIBLE) {
                    it.visibility = View.VISIBLE
                }
            }
        }
        if (treeData.getItemData().maxByOrNull { it.fileSize }==subItem){
            holder.getView<View>(R.id.goodPicture).visibility=View.VISIBLE
        }else{
            holder.getView<View>(R.id.goodPicture).visibility=View.GONE
        }
        holder.setImage(R.id.image,subItem.absolutePath)
        holder.setImageSelect(R.id.imageSelect,subItem.isCheck)
        holder.setText(R.id.describe, getSizeString(subItem.fileSize,1))
        holder.getView<ImageView>(R.id.imageSelect).setOnClickListener { view ->
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

    override fun onFold(isFold: Boolean, treeData: SectionData<TitleBean_Group, ImageBean>) {
        super.onFold(isFold, treeData)
        notifyItemChanged(treeData.groupPosition)
    }

//    override fun addSubItem(treeData: StickySectionData<TitleBean_Group, ImageBean>, subItem: ImageBean) {
//        super.addSubItem(treeData, subItem)
//        notifyItemChanged(treeData.groupPosition)
//    }

//    override fun onAddHeaderData(treeData: StickySectionData<TitleBean_Group, ImageBean>) {
//        super.onAddHeaderData(treeData)
//        val sortData=data.sortedBy {
//            -it.id
//        }
//        for (i in sortData.indices){
//            val sortItemData=sortData[i]
//            if (sortItemData!=data[i]){
//                notifyHeaderMoved(sortData[i],data[i].groupPosition)
//                data.remove(sortItemData)
//                data.add(i,sortItemData)
//                val changeStartIndex=data.indexOf(sortItemData)
//                for (j in changeStartIndex until data.size){
//                    if (j==0){
//                        data[j].groupPosition=0
//                    }else{
//                        data[j].groupPosition=data[j-1].groupPosition+data[j-1].itemData.size+1
//                    }
//                }
//            }
//        }
//    }
}