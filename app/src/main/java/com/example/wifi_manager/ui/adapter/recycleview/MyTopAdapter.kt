package com.example.wifi_manager.ui.adapter.recycleview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.domain.ItemBean
import kotlinx.android.synthetic.main.item_my_top_container.view.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/8 11:50:23
 * @class describe
 */
class MyTopAdapter:BaseQuickAdapter<ItemBean,BaseViewHolder>(R.layout.item_my_top_container) {
    override fun convert(holder: BaseViewHolder, item: ItemBean) {
        holder.itemView.apply {
            mMyTopIcon.setImageResource(item.icon)
            mMyTopTitle.text=item.title
        }
    }
}