package com.feisukj.cleaning.adapter

import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.module_base.cleanbase.RecyclerViewHolder

import com.feisukj.cleaning.bean.AppBean

class AppAdapter(layoutInt: Int, listData:List<AppBean>):ListAdapter<AppBean>(layoutInt,listData) {
    var adView: FrameLayout?=null

    override fun getItemCount(): Int {
        return super.getItemCount()+if (adView==null) 0 else 1
    }

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
    private var sort={a:AppBean,b:AppBean->
        val v=a.totalSize-b.totalSize
        if(v>0){
            if (v>Int.MAX_VALUE){
                Int.MAX_VALUE
            }else{
                v.toInt()
            }
        }else{
            if (v<Int.MIN_VALUE){
                Int.MIN_VALUE
            }else{
                v.toInt()
            }
        }
    }

    override fun addItem(item: AppBean) {
        if (listData.isEmpty()){
            super.addItem(item)
            return
        }
        for (i in listData.indices){
            val c=listData[i]
            if (sort(item,c)>0){//item.fileSize>c.fileSize
                listData.add(i,item)
                notifyItemInserted(i)
                return
            }else if (i==listData.size-1){
                super.addItem(item)
            }
        }
    }

    fun addItemOnUseTime(item: AppBean) {
        if (listData.isEmpty()){
            super.addItem(item)
            return
        }
        for (i in listData.indices){
            val c=listData[i]
            if (item.lastUseTimeInterval?:return>c.lastUseTimeInterval?:return){
                listData.add(i,item)
                notifyItemInserted(i)
                return
            }else if (i==listData.size-1){
                super.addItem(item)
            }
        }
    }

    fun sortItem(sort:(AppBean,AppBean)->Int){
        listData.sortWith(Comparator { o1, o2 ->
            return@Comparator sort(o1,o2)
        })
        this.sort=sort
        notifyDataSetChanged()
    }

    fun removeItem(packageName: String) {
        listData.find { it.packageName==packageName }?.let {
            removeItem(it)
        }
    }
}