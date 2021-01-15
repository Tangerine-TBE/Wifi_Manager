package com.example.wifi_manager.ui.adapter.recycleview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.domain.ItemBean
import kotlinx.android.synthetic.main.item_wifi_protect_cotainer.view.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/15 18:07:42
 * @class describe
 */
class WifiProtectAdapter:BaseQuickAdapter<ItemBean,BaseViewHolder>(R.layout.item_wifi_protect_cotainer) {
    override fun convert(holder: BaseViewHolder, item: ItemBean) {
        holder.itemView.apply {
            mWifiProtectIcon.setImageResource(item.icon)
            mWifiProtectTitle.text=item.title
        }
    }
}