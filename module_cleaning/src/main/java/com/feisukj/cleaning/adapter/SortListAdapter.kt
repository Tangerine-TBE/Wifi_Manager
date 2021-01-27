package com.feisukj.cleaning.adapter

import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.module_base.cleanbase.RecyclerViewHolder


open class SortListAdapter<T>( layoutId:Int, listData:List<T>,private val compare1:Comparator<T>) :ListAdapter<T>(layoutId,listData.sortedWith(compare1)){
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
    override fun setData(listData: List<T>) {
        super.setData(listData.sortedWith(compare1))
    }

    override fun addItem(item: T) {
        val index=findIndex(item)
        listData.add(index,item)
        notifyItemInserted(index)
    }

    private fun findIndex(item: T):Int{

        var lowIndex=0
        var highIndex=listData.size-1

        var currentIndex: Int
        while (highIndex>=lowIndex){
            currentIndex=(lowIndex+highIndex)/2
            if (highIndex-lowIndex<=1){
                val l=listData[lowIndex]
                val h=listData[highIndex]
                if (highIndex-lowIndex==0){
                    return if (compare1.compare(item,l)>0){
                        lowIndex+1
                    }else{
                        lowIndex
                    }
                }else if (highIndex-lowIndex==1){
                    return if (compare1.compare(item,h)>0){
                        highIndex+1
                    }else if (compare1.compare(item,l)<0){
                        lowIndex
                    }else{
                        lowIndex+1
                    }
                }
                return lowIndex+1
            }
            if (compare1.compare(item,listData[currentIndex])>0){
                lowIndex=currentIndex
            }else{
                highIndex=currentIndex
            }
        }
        return 0
    }
}