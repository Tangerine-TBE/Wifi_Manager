package com.example.wifi_manager.ui.adapter.recycleview

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ItemSafetyContainerBinding
import com.example.wifi_manager.domain.ItemBean

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/25 9:51:43
 * @class describe
 */
class SafetyItemAdapter : BaseQuickAdapter<ItemBean, BaseDataBindingHolder<ItemSafetyContainerBinding>>(R.layout.item_safety_container) {

    override fun convert(holder: BaseDataBindingHolder<ItemSafetyContainerBinding>, item: ItemBean) {
        holder.dataBinding?.apply {
            safetyHint.text=item.hint
            safetyTitle.text=item.title
            safetyMore.text=item.actionHint
        }
    }

}