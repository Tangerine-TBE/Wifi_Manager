package com.example.wifi_manager.ui.adapter.recycleview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ItemMyBottomContainerBinding

import com.example.wifi_manager.domain.ItemBean


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/8 11:50:23
 * @class describe
 */
class MyBottomAdapter:BaseQuickAdapter<ItemBean, BaseDataBindingHolder<ItemMyBottomContainerBinding>>(R.layout.item_my_bottom_container) {
    override fun convert(holder:  BaseDataBindingHolder<ItemMyBottomContainerBinding>, item: ItemBean) {
        holder.dataBinding?.apply {
            mMyBottomTitle.text=item.title
        }
    }
}