package com.feisukj.cleaning.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.module_base.cleanbase.RecyclerViewHolder

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.WeChatAndQQItemBean
import com.feisukj.cleaning.utils.getSizeString

class WeAndQQAdapter(val context: Context, val list: List<WeChatAndQQItemBean>) : RecyclerView.Adapter<RecyclerViewHolder>(){
    var itemOnClick:ItemOnClick<WeChatAndQQItemBean>?=null
    var adView:FrameLayout?=null

    override fun getItemViewType(position: Int): Int {
        if (position>=list.size){
            return -1
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerViewHolder {
        if (p1!=0){
            val frameLayout=FrameLayout(p0.context)
            frameLayout.layoutParams=ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            adView?.also {
                val p= it.parent
                if (p is ViewGroup){
                    p.removeAllViews()
                }
                it.layoutParams=FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        ,ViewGroup.LayoutParams.WRAP_CONTENT
                        ,Gravity.CENTER_HORIZONTAL)
                frameLayout.addView(it)
            }
            return RecyclerViewHolder(frameLayout)
        }

        return RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_wechat_and_qq_clean, p0, false))
    }
    override fun getItemCount()=list.size+if (adView==null) 0 else 1

    override fun onBindViewHolder(viewHolder: RecyclerViewHolder, position: Int) {
        if (position>=list.size){
            return
        }
        val item=list[position]
        viewHolder.setImage(R.id.icon,item.icon)
        viewHolder.setText(R.id.title,item.title)
        viewHolder.setImageSelect(R.id.select,item.isSelect)
        viewHolder.setText(R.id.size,getSizeString(item.fileTotalSize,2))
        viewHolder.getView<View>(R.id.selectRoot).setOnClickListener {
            item.isSelect=!item.isSelect
            viewHolder.setImageSelect(R.id.select,item.isSelect)
            if (itemOnClick!=null){
                itemOnClick?.onCheckItem(item,item.isSelect)
            }
        }
        viewHolder.itemView.setOnClickListener {
            if (itemOnClick!=null){
                itemOnClick?.onMyClick(it,list[position])
            }
        }
    }
}