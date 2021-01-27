package com.example.module_base.cleanbase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class RecyclerViewHolder(view:View) : RecyclerView.ViewHolder(view) {

    private val context:Context by lazy { itemView.context }
    private val views=SparseArray<View>()
    fun <V : View> getView(viewId:Int):V{
        var view=views[viewId]
        if (view==null){
            view=itemView.findViewById(viewId)
            views.put(viewId,view)
        }
        return view as V
    }
    val options: RequestOptions by lazy { RequestOptions() }

    fun setText(viewId: Int,text:String){
        getView<TextView>(viewId).text=text
    }

    fun setText(viewId: Int,text:Int){
        getView<TextView>(viewId).setText(text)
    }

    fun setImage(viewId: Int,resId:Int){
        Glide.with(context)
                .load(resId)
                .apply(options)
                .into(getView(viewId))
    }
    fun setImage(viewId: Int,drawable:Drawable){
        Glide.with(context)
                .load(drawable)
                .apply(options)
                .into(getView(viewId))
    }
    fun setImage(viewId: Int,bitmap:Bitmap){
        Glide.with(context)
                .load(bitmap)
                .apply(options)
                .into(getView(viewId))
    }
    fun setImage(viewId: Int,photoPath:String?){
        if (photoPath.isNullOrEmpty())
            return
        Glide.with(context)
                .load(photoPath)
                .apply(options)
                .into(getView(viewId))
    }
    fun setImageSelect(viewId: Int,isSelect:Boolean){
        getView<ImageView>(viewId).isSelected=isSelect
    }
    fun setViewSelect(viewId: Int,isSelect:Boolean){
        getView<View>(viewId).isSelected=isSelect
    }
    fun setCheckBox(viewId: Int,isCheck:Boolean){
        getView<CheckBox>(viewId).isChecked=isCheck
    }
    fun setVisibility(viewId: Int,visibility:Int){
        getView<View>(viewId).visibility=visibility
    }
}