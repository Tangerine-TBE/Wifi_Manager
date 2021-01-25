package com.example.wifi_manager.ui.adapter.recycleview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.module_base.domain.AppInfo
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ItemAppInfoContainerBinding


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/22 10:21:30
 * @class describe
 */
class SignalAppInfoAdapter : BaseQuickAdapter<AppInfo, BaseDataBindingHolder<ItemAppInfoContainerBinding>>(R.layout.item_app_info_container) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemAppInfoContainerBinding>,
        item: AppInfo
    ) {
        holder.dataBinding?.apply {
            item.appIcon?.let {
                signalAppIcon.setImageDrawable(it)
                signalAppName.text=item.appName
            }
        }
    }
}