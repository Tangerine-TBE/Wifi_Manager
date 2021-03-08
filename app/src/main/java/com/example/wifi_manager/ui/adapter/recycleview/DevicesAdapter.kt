package com.example.wifi_manager.ui.adapter.recycleview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ItemDeviceContainerBinding
import com.example.wifi_manager.domain.DeviceBean


/**
 * @author: 铭少
 * @date: 2021/1/17 0017
 * @description：
 */
class DevicesAdapter:BaseQuickAdapter<DeviceBean, BaseDataBindingHolder<ItemDeviceContainerBinding>>(R.layout.item_device_container) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemDeviceContainerBinding>,
        item: DeviceBean
    ) {
        holder?.dataBinding?.apply {
            item?.let {
                deviceName.text="${it.deviceName}"
                deviceIpMac.text="${it.deviceIp}"+"\n"+"${it.deviceMac}"
                deviceTab.text= if(it.deviceSign!="") it.deviceSign+"" else "标记"
            }

        }
    }
}