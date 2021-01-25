package com.example.wifi_manager.ui.adapter.recycleview

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ItemProtectCheckWifiContainerBinding
import com.example.wifi_manager.domain.ItemBean
import com.example.wifi_manager.utils.StepState


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/22 11:56:13
 * @class describe
 */
class ProtectWifiCheckAdapter:BaseQuickAdapter<ItemBean, BaseDataBindingHolder<ItemProtectCheckWifiContainerBinding>>(R.layout.item_protect_check_wifi_container) {
     val mList: MutableList<ItemBean> = ArrayList()
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

    override fun convert(
        holder: BaseDataBindingHolder<ItemProtectCheckWifiContainerBinding>,
        item: ItemBean
    ) {
        holder.dataBinding?.apply {
            protectCheckHint.text=item.title
            protectCheckState.setImageResource(item.icon)
            when (currentState) {
                StepState.ONE -> addCurrentList(1)
                StepState.TWO -> addCurrentList(2)
                StepState.THREE -> addCurrentList(3)
                StepState.FOUR -> addCurrentList(4)
            }

            if (mList.contains(item)) {
                when (holder.bindingAdapterPosition) {
                    2 -> {
                        numberHistory.visibility = View.VISIBLE
                        protectCheckState.visibility = View.GONE
                        protecting.visibility = View.GONE
                        protectLoading.visibility=View.GONE
                    }
                    3 -> {
                        numberHistory.visibility = View.GONE
                        protectCheckState.visibility = View.GONE
                        protecting.visibility = View.VISIBLE
                        protectLoading.visibility=View.GONE
                    }
                    else -> {
                        numberHistory.visibility = View.GONE
                        protectCheckState.visibility = View.VISIBLE
                        protecting.visibility = View.GONE
                        protectLoading.visibility=View.GONE
                    }
                }
            } else {
                numberHistory.visibility = View.GONE
                protectCheckState.visibility = View.GONE
                protecting.visibility = View.GONE
                protectLoading.visibility=View.VISIBLE
            }
        }
    }

}