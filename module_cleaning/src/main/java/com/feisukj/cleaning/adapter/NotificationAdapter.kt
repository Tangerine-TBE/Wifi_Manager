package com.feisukj.cleaning.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.module_base.cleanbase.RecyclerViewHolder

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.NotificationBean
import com.feisukj.cleaning.utils.getIcon
import com.feisukj.cleaning.utils.toTimeFormat

class NotificationAdapter(val context:Context,val list: List<NotificationBean>) : RecyclerView.Adapter<RecyclerViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification_clean, p0, false))
    }

    override fun getItemCount()=list.size

    override fun onBindViewHolder(viewHolder: RecyclerViewHolder, p1: Int) {
        val bean=list[p1]
        val pkg=bean.packageName
        val icon=if (pkg!=null) {
             getIcon(pkg)
        }else{
            null
        }
        if (icon!=null) {
            viewHolder.setImage(R.id.icon,icon )
        }
        viewHolder.setText(R.id.time, toTimeFormat(bean.time?:System.currentTimeMillis()))
        viewHolder.setText(R.id.title,bean.title?:"")
        viewHolder.setText(R.id.describe,bean.content?:"")
        viewHolder.itemView.setOnClickListener {
            bean.pendingIntent?.send()
        }
    }
}