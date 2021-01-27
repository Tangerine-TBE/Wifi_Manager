package com.example.module_base.cleanbase

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

abstract class BaseSectionAdapter<H,I>(data: List<SectionData<H, I>>) : RecyclerView.Adapter<RecyclerViewHolder>(){
    companion object{
        const val UN_KNOW_VIEW_TYPE=-1
    }

    protected var data=ArrayList<SectionData<H, I>>(data)
    var itemSelectListener:ItemSelectListener<H,I>?=null

    open fun setData(data: List<SectionData<H, I>>){
        this.data=ArrayList(data)
        notifyDataSetChanged()
    }

    fun getData():List<SectionData<H, I>>{
        return data.toList()
    }

    override fun getItemViewType(position: Int): Int {
        return getItemStatusByPosition(position)?.viewType?:UN_KNOW_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return if (viewType== ItemStatus.VIEW_TYPE_GROUP_ITEM){
            onCreateHeaderViewHolder(parent)
        }else if (viewType==ItemStatus.VIEW_TYPE_SUB_ITEM){
            onCreateSubItemViewHolder(parent)
        }else{
            RecyclerViewHolder(View(parent.context))
        }
    }

    abstract fun onCreateHeaderViewHolder(parent: ViewGroup):RecyclerViewHolder

    abstract fun onCreateSubItemViewHolder(parent: ViewGroup):RecyclerViewHolder

    override fun getItemCount(): Int {
        var count=0
        data.forEach {
            if (!it.isFold){
                count+= it.getItemData().size
            }
            count++
        }
        return count
    }

//    protected open fun isOpenFoldSwitch():Boolean{
//        return true
//    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val itemStatus=getItemStatusByPosition(position)?:return
        val treeData=data[itemStatus.groupItemIndex]
        if (holder.itemViewType== ItemStatus.VIEW_TYPE_GROUP_ITEM){
            treeData.groupPosition=position
            onBindHeaderViewHolder(holder,treeData.groupData,treeData)
            holder.itemView.setOnClickListener {
//                if (!isOpenFoldSwitch()){
//                    return@setOnClickListener
//                }
                treeData.isFold=!treeData.isFold
                onFold(treeData.isFold,treeData)
                notifyItemChanged(position)
                if (treeData.getItemData().isNotEmpty()) {
                    if (!treeData.isFold) {
                        notifyItemRangeInserted(position + 1, treeData.getItemData().size)
                    } else {
                        notifyItemRangeRemoved(position + 1, treeData.getItemData().size)
                    }
                }
            }
        }else{
            onBindSubItemViewHolder(holder,treeData,treeData.getItemData()[itemStatus.subItemIndex])
        }
    }

    protected open fun onFold(isFold:Boolean,treeData: SectionData<H, I>){}

    abstract fun onBindHeaderViewHolder(holder: RecyclerViewHolder,groupItem:H?,treeData:SectionData<H,I>)

    abstract fun onBindSubItemViewHolder(holder: RecyclerViewHolder, treeData:SectionData<H,I>, subItem:I)

    fun addHeaderItem(treeData:SectionData<H,I>){
        data.add(treeData)
        val count=if (!treeData.isFold){
            1+treeData.getItemData().size
        }else{
            1
        }
        notifyItemRangeInserted(this.itemCount-count,count)
        onAddHeaderData(treeData)
    }

    open fun removeAllItem(subItem: Collection<I>){
        for (i in 0 until data.size) {
            if (i>=data.size){
                break
            }
            val item=data[i]
            item.removeAllItem(subItem)
            if (item.getItemData().isEmpty()){
                data.removeAt(i)
            }
        }
        notifyDataSetChanged()
    }

    protected open fun onAddHeaderData(treeData:SectionData<H,I>){

    }

    fun notifyHeaderMoved(moveTreeData: SectionData<H, I>, toPosition: Int){
        val count=if (moveTreeData.isFold){
            1
        }else{
            moveTreeData.getItemData().size+1
        }
        for (i in 0 until count){
            notifyItemMoved(moveTreeData.groupPosition+i,toPosition+i)
        }
    }

    fun removeHeaderItem(treeData: SectionData<H, I>){
        val isExist=data.remove(treeData)
        if (isExist){
            val count=if (!treeData.isFold) 1+treeData.getItemData().size else 1
            notifyItemRangeRemoved(treeData.groupPosition,count)
        }
    }

    fun changeHeaderItem(treeData: SectionData<H, I>){
        val index=data.indexOf(treeData)
        if (index!=-1){
//            val count=if (!treeData.isFold) 1+treeData.itemData.size else 1
//            notifyItemRangeChanged(treeData.groupPosition,count)
            notifyItemChanged(treeData.groupPosition)
        }
    }

//    fun changeFold(treeData: StickySectionData<H, I>){
//        val index=data.indexOf(treeData)
//        if (index!=-1){
//            val count=if (!treeData.isFold) 1+treeData.itemData.size else 1
//            notifyItemRangeChanged(treeData.groupPosition,count)
//        }
//    }

    open fun addSubItem(treeData: SectionData<H, I>, subItem:I){
        val isExist=data.contains(treeData)
        treeData.addItem(subItem)
        if (isExist){
            if (!treeData.isFold) {
                val groupPosition=treeData.groupPosition
                notifyItemInserted(groupPosition+treeData.getItemData().size)
            }
        }else{
            addHeaderItem(treeData)
        }
    }

    open fun removeSubItem(treeData: SectionData<H, I>, subItem:I){
        if (!data.contains(treeData))
            return
        val isExist=treeData.removeItem(subItem)
        if (isExist&&!treeData.isFold){
            notifyItemRemoved(treeData.groupPosition+treeData.getItemData().size+1)
        }
        if (treeData.getItemData().isEmpty()){
            data.remove(treeData)
            notifyItemRemoved(treeData.groupPosition)
        }
    }

    open fun changeSubItem(treeData: SectionData<H, I>, subItem: I){
        if (!data.contains(treeData))
            return
        val subIndex=treeData.getItemData().indexOf(subItem)
        if (subIndex!=-1&&!treeData.isFold){
            notifyItemChanged(treeData.groupPosition+subIndex+1)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val lm=recyclerView.layoutManager
        if (lm is GridLayoutManager){
            lm.spanSizeLookup=object : GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    val itemStatus=getItemStatusByPosition(position)?:return lm.spanCount
                    return if (itemStatus.viewType== ItemStatus.VIEW_TYPE_GROUP_ITEM){
                        getGroupItemSpanSize(lm.spanCount)
                    }else{
                        getSubItemSpanSize(lm.spanCount)
                    }
                }

            }
        }
    }

    protected open fun getSubItemSpanSize(spanCont:Int):Int{
        return 1
    }

    protected open fun getGroupItemSpanSize(spanCont: Int):Int{
        return spanCont
    }

    private fun getItemStatusByPosition(position: Int): ItemStatus? {
        var count=0
        var itemStatus: ItemStatus?=null
        for (index in data.indices){
            val treeData=data[index]
            count++
            if (position==(count-1)){
                itemStatus= ItemStatus()
                itemStatus.viewType= ItemStatus.VIEW_TYPE_GROUP_ITEM
                itemStatus.groupItemIndex=index
                break
            }
            if (!treeData.isFold) {
                count += treeData.getItemData().size
            }
            if (count>position){
                itemStatus=ItemStatus()
                val start=count- treeData.getItemData().size
                val subItemIndex=position-start
                itemStatus.viewType= ItemStatus.VIEW_TYPE_SUB_ITEM
                itemStatus.groupItemIndex=index
                itemStatus.subItemIndex=subItemIndex
                break
            }
        }
        return itemStatus
    }

    private class ItemStatus{
        companion object{
            const val VIEW_TYPE_GROUP_ITEM=1
            const val VIEW_TYPE_SUB_ITEM=2
        }
        var viewType= VIEW_TYPE_SUB_ITEM
        var groupItemIndex=0
        var subItemIndex=-1
    }

    interface ItemSelectListener<H,I>{
        fun onSelectHeader(isSelect:Boolean,treeData:SectionData<H,I>)

        fun onSelectSubItem(isSelect: Boolean, treeData:SectionData<H,I>, subItem:I)

        fun onClickSubItem(treeData:SectionData<H,I>, subItem:I)
    }

    interface AllChooserListener{
        fun onChooserChange(isAllSelector:Boolean)
    }
}