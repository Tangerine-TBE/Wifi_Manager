package com.feisukj.cleaning.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.module_base.cleanbase.RecyclerViewHolder

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.AllFileBean
import com.feisukj.cleaning.bean.TitleBean_Group

class VideoAdapter(val context:Context,val list:List<Any>) : RecyclerView.Adapter<RecyclerViewHolder>(){
    var itemOnClick:ItemOnClick<Any>?=null
    private val TITLE_TYPE=1
    private val ITEM_TYPE=2
    override fun getItemViewType(position: Int): Int {
        return when(list[position]){
            is AllFileBean ->{
                ITEM_TYPE
            }
            is TitleBean_Group ->{
                TITLE_TYPE
            }
            else ->ITEM_TYPE
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): RecyclerViewHolder {
        val resId=when (type) {
            TITLE_TYPE -> {
                R.layout.item_video_title_clean
            }
            ITEM_TYPE -> {
                R.layout.item_video_clean
            }
            else -> {
                R.layout.item_video_clean
            }
        }
        return RecyclerViewHolder(LayoutInflater.from(context).inflate(resId, viewGroup, false))
    }
    override fun getItemCount()=list.size
    override fun onBindViewHolder(viewHolder: RecyclerViewHolder, position: Int) {
        when(getItemViewType(position)){
            TITLE_TYPE->{
                val title=list[position]
                if (title is TitleBean_Group){
                    viewHolder.setText(R.id.title,title.title)
                    viewHolder.setImageSelect(R.id.allChoose,title.isCheck)
                    viewHolder.getView<View>(R.id.allChoose).setOnClickListener {
                        val isChecked=!it.isSelected
                        it.isSelected=isChecked
                        title.isCheck=isChecked
                        itemOnClick?.onCheckItem(title,isChecked)
                    }
                }
            }
            ITEM_TYPE->{
                val item=list[position]
                if (item is AllFileBean){
                    viewHolder.setText(R.id.fileName,item.fileName)
                    viewHolder.setImage(R.id.fileIcon,item.fileIcon)
                    viewHolder.setText(R.id.fileSize,item.fileSizeString)
                    viewHolder.setText(R.id.fileDate,item.fileDate)
                    viewHolder.setImageSelect(R.id.fileCheck,item.isCheck)
                    viewHolder.getView<View>(R.id.fileCheck).setOnClickListener {
                        val isChecked=!it.isSelected
                        it.isSelected=isChecked
                        item.isCheck=isChecked
                        itemOnClick?.onCheckItem(item,isChecked)
                    }
                }
                viewHolder.itemView.setOnClickListener { if (itemOnClick!=null) itemOnClick?.onMyClick(it,item) }
            }
        }
    }
}