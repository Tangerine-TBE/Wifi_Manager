package com.example.wifi_manager.adapter

import android.net.wifi.ScanResult
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import kotlinx.android.synthetic.main.item_wifi_container.view.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.adapter
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 17:09:41
 * @class describe
 */
class HomeWifiAdapter:BaseQuickAdapter<ScanResult,BaseViewHolder>(R.layout.item_wifi_container) {
    override fun convert(holder: BaseViewHolder, item: ScanResult) {
        holder.itemView.apply {
            mWifiName.text=item.SSID
            mWifiStateHint.text=item.BSSID
        }
    }
}