package com.example.wifi_manager.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.domain.WifiMessage
import kotlinx.android.synthetic.main.item_wifi_container.view.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 17:09:41
 * @class describe
 */
class HomeWifiAdapter:BaseQuickAdapter<WifiMessage,BaseViewHolder>(R.layout.item_wifi_container) {
    override fun convert(holder: BaseViewHolder, item: WifiMessage) {
        holder.itemView.apply {
            mWifiName.text=item.wifiName
            item.encryptionWay.let {
                mWifiStateHint.text=if (it.contains("WPA2") and it.contains("WPS") || it.contains("WPA")) "加密wifi" else "免费wifi"
            }
        }
    }
}