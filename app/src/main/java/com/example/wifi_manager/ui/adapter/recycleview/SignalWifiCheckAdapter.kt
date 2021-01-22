package com.example.wifi_manager.ui.adapter.recycleview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.domain.ItemBean
import com.example.wifi_manager.utils.StepState
import kotlinx.android.synthetic.main.item_hardware_container.view.*
import kotlinx.android.synthetic.main.item_signal_check_wifi_container.view.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/22 11:56:13
 * @class describe
 */
class SignalWifiCheckAdapter:BaseQuickAdapter<ItemBean,BaseViewHolder>(R.layout.item_signal_check_wifi_container) {

     val mList: MutableList<ItemBean> = ArrayList()

    override fun convert(holder: BaseViewHolder, item: ItemBean) {
        holder.itemView.apply {
            signalCheckHint.text=item.title
            signalCheckState.setImageResource(item.icon)
            when (currentState) {
                StepState.ONE -> addCurrentList(1)
                StepState.TWO -> addCurrentList(2)
                StepState.THREE -> addCurrentList(3)
                StepState.FOUR -> addCurrentList(4)
            }

            if (mList.contains(item)) {
                signalCheckState.setImageResource(R.mipmap.icon_signal_select)
            } else {
                signalCheckState.setImageResource(R.mipmap.icon_signal_normal)
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