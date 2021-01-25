package com.example.wifi_manager.ui.adapter.recycleview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

import com.example.wifi_manager.R

import com.example.wifi_manager.databinding.ItemWifiProtectCotainerBinding
import com.example.wifi_manager.domain.ItemBean


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/15 18:07:42
 * @class describe
 */
class WifiProtectAdapter:BaseQuickAdapter<ItemBean, BaseDataBindingHolder<ItemWifiProtectCotainerBinding>>(R.layout.item_wifi_protect_cotainer) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemWifiProtectCotainerBinding>,
        item: ItemBean
    ) {
        holder.dataBinding?.apply {
            mWifiProtectIcon.setImageResource(item.icon)
            mWifiProtectTitle.text=item.title
        }
    }
}