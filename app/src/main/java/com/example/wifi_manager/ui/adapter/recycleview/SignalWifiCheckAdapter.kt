package com.example.wifi_manager.ui.adapter.recycleview

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ItemSignalCheckWifiContainerBinding
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
class SignalWifiCheckAdapter:BaseQuickAdapter<ItemBean, BaseDataBindingHolder<ItemSignalCheckWifiContainerBinding>>(R.layout.item_signal_check_wifi_container) {
    private val mList: MutableList<ItemBean> = ArrayList()
    private fun addCurrentList(total: Int) {
        mList.clear()
        for (i in 0 until total)
            mList.add(data[i])
    }

     fun cleanCurrentList(){
        mList.clear()
        notifyDataSetChanged()
    }

    private var currentState = StepState.NONE
    fun setStepState(state: StepState) {
        currentState = state
        notifyDataSetChanged()
    }

    override fun convert(
        holder: BaseDataBindingHolder<ItemSignalCheckWifiContainerBinding>,
        item: ItemBean
    ) {
        holder.dataBinding?.apply {
            signalCheckHint.text=item.title
            signalCheckState.setImageResource(item.icon)
            when (currentState) {
                StepState.ONE -> addCurrentList(1)
                StepState.TWO -> addCurrentList(2)
                StepState.THREE -> addCurrentList(3)
                StepState.FOUR -> addCurrentList(4)
            }
            if (mList.contains(item)) {
                signalCheckState.visibility=View.VISIBLE
                signalLoading.visibility=View.GONE

            } else {
                signalCheckState.visibility=View.GONE
                signalLoading.visibility=View.VISIBLE
            }

        }
    }

}