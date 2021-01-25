package com.example.wifi_manager.ui.adapter.recycleview

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ItemHardwareContainerBinding
import com.example.wifi_manager.databinding.ItemHomeTopContainerBinding
import com.example.wifi_manager.domain.ItemBean
import com.example.wifi_manager.utils.StepState


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/20 10:04:25
 * @class describe
 */
class HardwareAdapter : BaseQuickAdapter<ItemBean, BaseDataBindingHolder<ItemHardwareContainerBinding>>(R.layout.item_hardware_container) {
    private val mList: MutableList<ItemBean> = ArrayList()
    override fun convert(holder: BaseDataBindingHolder<ItemHardwareContainerBinding>, item: ItemBean) {
        holder.dataBinding?.apply {
            optimizeHint.text = item.title
            when (currentState) {
                StepState.TWO -> addCurrentList(1)
                StepState.THREE -> addCurrentList(2)
                StepState.FOUR -> addCurrentList(3)
                StepState.FIVE -> addCurrentList(4)
            }

            if (mList.contains(item)) {
                optimizeIcon.setImageResource(R.mipmap.icon_hardware_success)
                optimizeHint.setTextColor(Color.WHITE)
            } else {
                optimizeIcon.setImageResource(R.mipmap.icon_hardware_loading)
            }

        }
    }

    private fun addCurrentList(total: Int) {
        mList.clear()
        for (i in 0 until total)
            mList.add(data[i])
    }

    private var currentState = StepState.NONE
    fun setStepState(state: StepState) {
        currentState = state
        notifyDataSetChanged()
    }
}