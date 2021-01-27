package com.feisukj.cleaning.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.ImageBean

class PhotoCleanItemView_(parentView: ViewGroup, val title:String) {
    private val itemView:View = LayoutInflater.from(parentView.context).inflate(R.layout.item_photoclean_view_clean_,parentView,true)
    private val titleFold by lazy { itemView.findViewById<ImageView>(R.id.titleFold) }
    private val titleView by lazy { itemView.findViewById<View>(R.id.titleView) }
    private val allChoose by lazy { itemView.findViewById<ImageView>(R.id.allChoose) }
    private val morePhotoCount by lazy { itemView.findViewById<TextView>(R.id.morePhotoCount) }
    private val photoContent by lazy { itemView.findViewById<ViewGroup>(R.id.photoContent) }
    private val smallLoading by lazy { itemView.findViewById<SmallLoading>(R.id.smallLoading) }
    private var photoCount=0
        set(value) {
            if (value!=field){
                field=value
                if (value>4){
                    morePhotoCount.visibility=View.VISIBLE
                    morePhotoCount.text="+${value-4}"
                }else{
                    morePhotoCount.visibility=View.GONE
                }
            }
        }

    init {
        itemView.findViewById<TextView>(R.id.titleText).text=title
        titleView.setOnClickListener {
            titleFold.isSelected=!titleFold.isSelected
            if (titleFold.isSelected){
                photoContent.visibility=View.GONE
            }else{
                photoContent.visibility=View.VISIBLE
            }
        }
        smallLoading.visibility=View.VISIBLE
        allChoose.isSelected=true
        smallLoading.startAnim()
    }

    fun initPhoto(list:List<ImageBean>){
        if (list.isEmpty()){
            return
        }
        for (i in photoCount until 4){
            if (list.size<=i){
                break
            }
            photoContent.getChildAt(i).let {
                if (it is ImageView){
                    Glide.with(it).load(list[i].absolutePath).into(it)
                }
            }
        }
        photoCount=list.size
    }

    fun setAllChooseClick(clickListener:(View)->Unit){
        allChoose.setOnClickListener(clickListener)
    }

    fun getAllChooseState():Boolean= allChoose.isSelected

    fun setOnClickListener(click:(View)->Unit){
        photoContent.setOnClickListener(click)
    }

    fun onNextPhoto(vararg imageBeans: ImageBean){
        if (photoCount<4){
            imageBeans.forEach {imageBean->
                if (photoContent.childCount>photoCount){
                    photoContent.getChildAt(photoCount).let {
                        if (it is ImageView){
                            Glide.with(it).load(imageBean.absolutePath).into(it)
                        }
                    }
                }else{
                    return@forEach
                }
            }
        }
        photoCount++
    }

    fun onCompleteUpdate(list:List<ImageBean>){
        smallLoading.isStop=true
        smallLoading.visibility=View.GONE
        for (i in photoCount until 4){
            if (list.size<=i){
                break
            }
            photoContent.getChildAt(i).let {
                if (it is ImageView){
                    Glide.with(it).load(list[i].absolutePath).into(it)
                }
            }
        }
        photoCount=list.size
    }
}