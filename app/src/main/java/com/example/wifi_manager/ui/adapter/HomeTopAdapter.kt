package com.example.wifi_manager.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.domain.ItemBean
import kotlinx.android.synthetic.main.item_home_top_container.view.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 14:32:29
 * @class describe
 */
class HomeTopAdapter:BaseQuickAdapter<ItemBean,BaseViewHolder>(R.layout.item_home_top_container) {
    override fun convert(holder: BaseViewHolder, item: ItemBean) {
        holder.itemView.apply {
            mHomeTopIcon.setImageResource(item.icon)
            mHomeTopTitle.text=item.title
        }
    }
}