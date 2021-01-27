package com.feisukj.cleaning.ui.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.module_base.cleanbase.BaseFragment2


import com.feisukj.cleaning.R
import com.feisukj.cleaning.file.FileManager
import com.feisukj.cleaning.ui.activity.PhotoActivity
import com.feisukj.cleaning.utils.Constant
import com.feisukj.cleaning.utils.getSizeString
import kotlinx.android.synthetic.main.frag_other_photo_manager.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class OtherPhotoManagerFragment : BaseFragment2(){
    override fun getLayoutId()= R.layout.frag_other_photo_manager

    override fun initView() {
        GlobalScope.launch { loadData() }
        activity?.let {
        }
    }

    override fun initListener() {
        
    }

    private fun loadData(){
        Constant.otherPicture.toList().forEach { pair ->
            val f=FileManager.scanDirFile(File(pair.first),{this},{Constant.PICTURE_FORMAT.any { this.absolutePath.endsWith(it) }})
            if (f.isNotEmpty()){
                val sum=f.map { it.length() }.sum()
                activity?.runOnUiThread {
                    val itemView=LayoutInflater.from(context).inflate(R.layout.item_photo_manager_style2,contentView,false)
                    itemView.findViewById<TextView>(R.id.photoItemName).text = pair.second
                    val image=itemView.findViewById<ImageView>(R.id.photoItemPreView)
                    Glide.with(this).load(f.first()).into(image)
                    itemView.findViewById<TextView>(R.id.photoItemCount).text=f.size.toString()+"张"
                    itemView.findViewById<TextView>(R.id.photoItemSize).text= getSizeString(sum)

                    itemView.setOnClickListener {
                        val intent=Intent(context,PhotoActivity::class.java)
                        intent.putExtra(PhotoActivity.PATH_KEY,pair.first)
                        intent.putExtra(PhotoActivity.TITLE_RES_KEY,pair.second)
                        startActivity(intent)
                    }
                    contentView.addView(itemView)
                }
            }

        }
    }
}