package com.example.wifi_manager.ui.adapter.recycleview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ItemHomeTopContainerBinding
import com.example.wifi_manager.databinding.ItemWifiContainerBinding
import com.example.wifi_manager.domain.ItemBean


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 14:32:29
 * @class describe
 */
class HomeTopAdapter:BaseQuickAdapter<ItemBean, BaseDataBindingHolder<ItemHomeTopContainerBinding>>(R.layout.item_home_top_container) {
    override fun convert(holder: BaseDataBindingHolder<ItemHomeTopContainerBinding>, item: ItemBean) {
        holder.dataBinding?.apply {
            mHomeTopIcon.setImageResource(item.icon)
            mHomeTopTitle.text=item.title
        }
    }
}