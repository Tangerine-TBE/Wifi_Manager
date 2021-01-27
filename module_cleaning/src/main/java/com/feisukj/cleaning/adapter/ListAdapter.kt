package com.feisukj.cleaning.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.module_base.cleanbase.BaseSectionAdapter
import com.example.module_base.cleanbase.RecyclerViewHolder

import java.util.*
import kotlin.collections.ArrayList

open class ListAdapter<T>(private val layoutId:Int, listData:List<T>): RecyclerView.Adapter<RecyclerViewHolder>() {
    companion object{
        const val UN_KNOW_VIEW_TYPE=-1
    }
    protected var listData=LinkedList<T>()
    init {
        this.listData= LinkedList(listData)
    }

    var bindView:((RecyclerViewHolder,T)->Unit)?=null

    open fun setData(listData: List<T>){
        this.listData= LinkedList(listData)
        notifyDataSetChanged()
    }

    fun getData():List<T>{
        return listData
    }

    override fun getItemViewType(position: Int): Int {
        return getItemStatusByPosition(position)?.viewType?: BaseSectionAdapter.UN_KNOW_VIEW_TYPE
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerViewHolder {
        return if (p1 == ItemStatus.VIEW_TYPE_GROUP_ITEM) {
            val itemView = LayoutInflater.from(p0.context).inflate(layoutId, p0, false)
            RecyclerViewHolder(itemView)
        }else{
            RecyclerViewHolder(View(p0.context))
        }
    }

    override fun getItemCount(): Int =listData.size

    override fun onBindViewHolder(p0: RecyclerViewHolder, p1: Int) {
        val itemStatus=getItemStatusByPosition(p1)?:return
        bindView?.invoke(p0,listData[p1])
    }

    open fun addItem(item:T){
        listData.add(item)
        notifyItemInserted(listData.size-1)
    }

    open fun removeItem(item: T){
        val index=listData.indexOf(item)
        if (index!=-1){
            listData.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    open fun changeItem(item: T){
        val index=listData.indexOf(item)
        if (index!=-1){
            notifyItemChanged(index)
        }
    }

    private fun getItemStatusByPosition(position: Int): ListAdapter.ItemStatus? {
        var count=0
        var itemStatus: ListAdapter.ItemStatus?=null
        for (index in listData.indices){
            count++
            if (position==(count-1)){
                itemStatus= ListAdapter.ItemStatus()
                itemStatus.viewType= ListAdapter.ItemStatus.VIEW_TYPE_GROUP_ITEM
                itemStatus.groupItemIndex=index
                break
            }
        }
        return itemStatus
    }

    private class ItemStatus{
        companion object{
            const val VIEW_TYPE_GROUP_ITEM=1
        }
        var viewType= VIEW_TYPE_GROUP_ITEM
        var groupItemIndex=0
    }
}