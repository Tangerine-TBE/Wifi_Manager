package com.example.wifi_manager.ui.adapter.recycleview

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ItemSafetyContainerBinding
import com.example.wifi_manager.domain.ItemBean
import com.example.wifi_manager.utils.StepState

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/25 9:51:43
 * @class describe
 */
class SafetyItemAdapter : BaseQuickAdapter<ItemBean, BaseDataBindingHolder<ItemSafetyContainerBinding>>(R.layout.item_safety_container) {
    private val mList: MutableList<ItemBean> = ArrayList()


    fun cleanList() {
        mList.clear()
        notifyDataSetChanged()
    }

    override fun convert(holder: BaseDataBindingHolder<ItemSafetyContainerBinding>, item: ItemBean) {
        holder.dataBinding?.apply {
            safetyHint.text=item.hint
            safetyTitle.text=item.title
            safetyMore.text=item.actionHint
            when (currentState) {
                StepState.ONE -> addCurrentList(1)
                StepState.TWO -> addCurrentList(2)
                StepState.THREE -> addCurrentList(3)
                StepState.FOUR -> addCurrentList(4)
                StepState.FIVE -> addCurrentList(5)
            }

            if (mList.contains(item)) {
                safetyMore.visibility= View.VISIBLE
                safetyLoading.visibility= View.GONE

            } else {
                safetyMore.visibility= View.GONE
                safetyLoading.visibility= View.VISIBLE
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