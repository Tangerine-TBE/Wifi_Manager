package com.example.wifi_manager.ui.adapter.recycleview

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ItemCancelShareApplyContainerBinding
import com.example.wifi_manager.databinding.ItemHardwareContainerBinding
import com.example.wifi_manager.domain.ItemBean

import java.util.HashMap

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/12 17:31:06
 * @class describe
 */
class CancelShareApplyAdapter:BaseQuickAdapter<ItemBean, BaseDataBindingHolder<ItemCancelShareApplyContainerBinding>>(R.layout.item_cancel_share_apply_container) {
    private val mContentMap:MutableMap<Int,String> = HashMap()
    override fun convert(holder:  BaseDataBindingHolder<ItemCancelShareApplyContainerBinding>, item: ItemBean) {
        holder.dataBinding?.apply {
            val position = holder.adapterPosition
            mShareHelp.visibility=if (position==1) View.VISIBLE else View.GONE
            mApplyTitle.text=item.title
            mApplyInput.hint=item.hint

            mApplyInput.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                }
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    mContentMap[position] = s.toString()
                }
            })

        }
    }

    fun getInputContent()=mContentMap
}