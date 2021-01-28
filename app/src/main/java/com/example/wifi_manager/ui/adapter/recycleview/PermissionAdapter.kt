package com.example.wifi_manager.ui.adapter.recycleview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ItemPermissionContainerBinding

import com.example.wifi_manager.domain.ItemBean

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/28 14:04:44
 * @class describe
 */
class PermissionAdapter:
    BaseQuickAdapter<ItemBean, BaseDataBindingHolder<ItemPermissionContainerBinding>>(R.layout.item_permission_container) {
    override fun convert(holder: BaseDataBindingHolder<ItemPermissionContainerBinding>, item: ItemBean) {
        holder.dataBinding?.apply {
            psIcon.setImageResource(item.icon)
            psName.text = item.title
            psHint.text=item.hint
        }
    }
}