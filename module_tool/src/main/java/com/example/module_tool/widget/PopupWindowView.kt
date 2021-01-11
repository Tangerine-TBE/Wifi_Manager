package com.example.module_tool.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_tool.R


class PopupWindowView(val context: Context,val data:Array<ArrayList<String>>,val inViewDown:View,val colorBackground:Int=0) {
    private val contentView:ViewGroup by lazy {
        LayoutInflater.from(context).inflate(R.layout.popupwindow_view_cjy,null).also {
        if (colorBackground!=0)
            it.setBackgroundColor(colorBackground)
        } as ViewGroup
    }
    private val recyclerView: RecyclerView by lazy { contentView.findViewById<RecyclerView>(R.id.recyclerView) }
    val adapter:RecyclerViewAdapter by lazy { RecyclerViewAdapter() }
    var textColor:Int?=null
    var itemOnClick:ItemOnClick?=null

    val popupWindow:PopupWindow by lazy {
        PopupWindow(contentView,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT).apply {
//            isTouchable=true
            isFocusable=true
        }
    }
    init {
        recyclerView.layoutManager= LinearLayoutManager(context)
        recyclerView.adapter=adapter
    }
    fun show(){
        popupWindow.showAsDropDown(inViewDown)
    }

    fun dismiss(){
        popupWindow.dismiss()
    }

    fun onDismissInvoke(unit:()->Unit){
        popupWindow.setOnDismissListener {
            unit.invoke()
        }
    }

    inner class RecyclerViewAdapter: RecyclerView.Adapter<ItemViewHolder>(){
        override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ItemViewHolder {
            return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.popupwindow_item,contentView,false))
        }

        override fun getItemCount(): Int {
            if (data.isEmpty()){
                return 0
            }else{
                return data[0].size
            }
        }
        override fun onBindViewHolder(viewHolder: ItemViewHolder, p1: Int) {
            viewHolder.titleView.text=data[0][p1]
            if (data.size>=2){
                viewHolder.unitView.text=data[1][p1]
            }
            textColor?.also {
                viewHolder.titleView.setTextColor(it)
                viewHolder.unitView.setTextColor(it)
            }
            viewHolder.itemView.setOnClickListener {
                itemOnClick?.onItemClick(p1)
            }
        }
    }

    inner class ItemViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val titleView:TextView by lazy { itemView.findViewById<TextView>(R.id.title) }
        val unitView:TextView by lazy { itemView.findViewById<TextView>(R.id.unit) }
    }

    interface ItemOnClick{
        fun onItemClick(position:Int)
    }
}