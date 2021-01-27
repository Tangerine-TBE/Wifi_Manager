package com.feisukj.cleaning.ui.fragment

import android.text.format.Formatter
import android.view.View
import com.example.module_base.cleanbase.RecyclerViewHolder
import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.AppAdapter_
import com.feisukj.cleaning.bean.AppBean

class SizeSortAppFragment:AbstractAppFragment() {

    override fun getAdapter(): AppAdapter_ {
        return AppAdapter_(R.layout.item_software2_clean, emptyList(), Comparator { o1, o2 ->
            if (o1.fileSize>o2.fileSize){
                -1
            }else{
                1
            }
        })
    }

    override fun onBindView(vh: RecyclerViewHolder, appBean: AppBean) {
        appBean.icon?.get().let {
            if (it==null){
                vh.setImage(R.id.appIcon,android.R.mipmap.sym_def_app_icon)
            }else{
                vh.setImage(R.id.appIcon,it)
            }
        }
        vh.setText(R.id.appLabel,appBean.label)
        vh.setText(R.id.appDes, Formatter.formatFileSize(context,appBean.appBytes+appBean.dataBytes+appBean.cacheBytes+appBean.fileSize))

        val appChooseView=vh.getView<View>(R.id.appChoose)
        appChooseView.isSelected=appBean.isCheck
        appChooseView.setOnClickListener {
            it.isSelected=!it.isSelected
            appBean.isCheck=it.isSelected
            val isChange=if (it.isSelected){
                chooseApp.add(appBean)
            }else{
                chooseApp.remove(appBean)
            }
            if (isChange){
                updateCleanText()
            }
        }
        vh.itemView.setOnClickListener {
            toAppSetting(appBean.packageName?:return@setOnClickListener)
        }
    }

    override fun onPackageAdd(appBean: AppBean) {
        super.onPackageAdd(appBean)
    }
}