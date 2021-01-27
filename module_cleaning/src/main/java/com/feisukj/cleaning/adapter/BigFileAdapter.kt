package com.feisukj.cleaning.adapter

import android.content.Context
import android.os.Environment
import androidx.recyclerview.widget.RecyclerView
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.module_base.cleanbase.RecyclerViewHolder

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.AllFileBean
import com.feisukj.cleaning.ui.activity.BigFileActivity

class BigFileAdapter(val context:Context, list: List<AllFileBean>, private val flag:Int=1) : RecyclerView.Adapter<RecyclerViewHolder>(){
    private var data=ArrayList(list)
    var itemOnClick:ItemOnClick<AllFileBean>?=null

    fun setData(list: List<AllFileBean>){
        data= ArrayList(list)
        notifyDataSetChanged()
    }

    fun getData():List<AllFileBean>{
        return data.toList()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_file_clean, p0, false))
    }
    override fun getItemCount()=data.size
    override fun onBindViewHolder(viewHolder: RecyclerViewHolder, p1: Int) {
        val fileBean=data[p1]
        if(flag==0){
            viewHolder.setVisibility(R.id.fileCheck, View.GONE)
        }
        viewHolder.setText(R.id.fileName,fileBean.fileName)
        viewHolder.setImage(R.id.fileIcon, fileBean.fileIcon)
        if (flag== BigFileActivity.BIG){
            val fromT=context.resources.getString(R.string.fromDir).format(fileBean.absolutePath.removePrefix(Environment.getExternalStorageDirectory().absolutePath)?.removeSuffix(fileBean.fileName))
            viewHolder.setText(R.id.fileDate,fromT)
        }else {
            viewHolder.setText(R.id.fileDate, fileBean.fileDate)
        }
        viewHolder.setImageSelect(R.id.fileCheck,fileBean.isCheck)
        if (fileBean.isFile){
            viewHolder.setText(R.id.fileSize, Formatter.formatFileSize(viewHolder.itemView.context,fileBean.fileSize))
        }else{
            val countT=context.resources.getString(R.string.fileCount).format(fileBean.dirCount)
            viewHolder.setText(R.id.fileSize,countT)
        }
        viewHolder.getView<ImageView>(R.id.fileCheck).setOnClickListener {
            it.isSelected=!it.isSelected
            if (itemOnClick!=null ){
                itemOnClick!!.onCheckItem(data[p1],it.isSelected)
                data[p1].isCheck=it.isSelected
            }
        }
        viewHolder.itemView.setOnClickListener {
            if (itemOnClick!=null){
                itemOnClick!!.onMyClick(it,data[p1])
            }
        }
    }

    fun addItem(allFileBean: AllFileBean){
        data.add(allFileBean)
        notifyItemInserted(data.size-1)
    }
}