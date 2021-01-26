package com.example.wifi_manager.ui.adapter.recycleview

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.binder.QuickDataBindingItemBinder
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ItemSafetyCheckContainerBinding
import com.example.wifi_manager.domain.ItemBean

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/26 15:08:11
 * @class describe
 */
class SafetyCheckAdapter:BaseQuickAdapter<ItemBean, BaseDataBindingHolder<ItemSafetyCheckContainerBinding>>(R.layout.item_safety_check_container) {
    override fun convert(holder: BaseDataBindingHolder<ItemSafetyCheckContainerBinding>, item: ItemBean) {
        holder.dataBinding?.apply {
            checkTitle.text=item.title
            checkHint.text=item.hint

            checkHint.setTextColor(  if (item.state) Color.RED else Color.GREEN)

        }
    }
}