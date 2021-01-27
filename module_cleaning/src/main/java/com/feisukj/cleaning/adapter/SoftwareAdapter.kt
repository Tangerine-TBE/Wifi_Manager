package com.feisukj.cleaning.adapter

import android.content.Context
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.module_base.cleanbase.RecyclerViewHolder

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.AppBean
import com.feisukj.cleaning.bean.TitleBean_Group
import com.feisukj.cleaning.utils.getSizeString

class SoftwareAdapter(private val context: Context, private val list: ArrayList<Any>): RecyclerView.Adapter<RecyclerViewHolder>() {
    companion object{
        private const val TITLE=1
        private const val ITEM=2
        private const val UN_KNOW=-1
    }
    var itemOnClick:ItemOnClick<Any>?=null
    var adView:FrameLayout?=null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerViewHolder {
        return if (p1==ITEM)
            RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_software_clean, p0, false))
        else if (p1==TITLE)
            RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_software_title_clean, p0, false))
        else{
            val frameLayout=FrameLayout(p0.context)
            frameLayout.layoutParams=ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            adView?.also {
                val p=it.parent
                if (p is ViewGroup){
                    p.removeAllViews()
                }
                it.layoutParams=FrameLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300f,p0.context.resources.displayMetrics).toInt()
                        ,ViewGroup.LayoutParams.WRAP_CONTENT
                        , Gravity.CENTER_HORIZONTAL)
                frameLayout.addView(it)
            }
            return RecyclerViewHolder(frameLayout)
        }
    }
    override fun getItemCount(): Int {
        return list.size+if (adView==null) 0 else 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position>=list.size){
            return UN_KNOW
        }
        return if (list[position] is AppBean)
            ITEM
        else if (list[position] is TitleBean_Group)
            TITLE
        else UN_KNOW
    }
    override fun onBindViewHolder(holder: RecyclerViewHolder, p1: Int) {
        val type=getItemViewType(p1)
        if (type== ITEM){
            loadItem(holder,p1)
        }else if (type== TITLE){
            loadTitle(holder,p1)
        }else{
            return
        }
    }
    private fun loadItem(holder: RecyclerViewHolder, position: Int){
        val appBean=list[position]
        if (appBean is AppBean){
            holder.setText(R.id.name,appBean.label)
            appBean.icon.let { reference ->
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
            if (appBean.isApp==true){
                holder.setText(R.id.describe, context.resources.getString(R.string.appSizeT).format(appBean.fileSizeString))
                if (Build.VERSION.SDK_INT<Build.VERSION_CODES.O) {
                    holder.setText(R.id.installDes, context.resources.getString(R.string.storage).format(
                            if (appBean.totalSize==0L){
                                context.resources.getString(R.string.unknow)
                            }else{
                                getSizeString(appBean.totalSize,2)
                            }
                    ))
                }else{
                    holder.setText(R.id.installDes, context.resources.getString(R.string.versionNumber).format(appBean.versionName))
                }
            }else if (appBean.isApp==false){
                holder.setText(R.id.installDes,if (appBean.isInstall) context.getString(R.string.alreadyInstalled) else context.getString(R.string.NotInstalled))
                holder.setText(R.id.describe, context.resources.getString(R.string.size).format(appBean.fileSizeString))
            }
            holder.getView<ImageView>(R.id.select).isSelected = appBean.isCheck
            if (itemOnClick!=null){
                holder.itemView.setOnClickListener {
                    itemOnClick?.onMyClick(it,appBean)
                }
                holder.getView<View>(R.id.selectRoot).setOnClickListener {
                    val img=holder.getView<ImageView>(R.id.select)
                    img.isSelected=!img.isSelected
                    appBean.isCheck=img.isSelected
                    itemOnClick?.onCheckItem(appBean,img.isSelected)
                }
            }
        }
    }
    private fun loadTitle(holder: RecyclerViewHolder, position: Int){
        val titleBean= list[position] as? TitleBean_Group ?: return
        holder.setText(R.id.titleSoftware,"${titleBean.title}(${titleBean.itemCount})")
        holder.setImageSelect(R.id.allChoose,titleBean.isCheck)
        holder.getView<View>(R.id.allChooseRoot).setOnClickListener {
            val img=holder.getView<ImageView>(R.id.allChoose)
            img.isSelected=!img.isSelected
            titleBean.isCheck=img.isSelected
            itemOnClick?.onGroupTitleCheck(titleBean.id,img.isSelected)
        }
    }
}