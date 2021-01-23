package com.example.wifi_manager.ui.adapter.recycleview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.domain.DeviceBean
import kotlinx.android.synthetic.main.item_device_container.view.*

/**
 * @author: 铭少
 * @date: 2021/1/17 0017
 * @description：
 */
class DevicesAdapter:BaseQuickAdapter<DeviceBean,BaseViewHolder>(R.layout.item_device_container) {
    override fun convert(holder: BaseViewHolder, item: DeviceBean) {
        holder.itemView.apply {
            deviceName.text=item.deviceName
            deviceIpMac.text=item.deviceIp+"\n"+item.deviceMac
            deviceTab.text= if(item.deviceSign!="") item.deviceSign+"" else "标记"
        }
    }
}