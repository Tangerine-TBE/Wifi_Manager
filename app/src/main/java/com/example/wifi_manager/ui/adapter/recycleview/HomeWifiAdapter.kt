package com.example.wifi_manager.ui.adapter.recycleview

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.domain.WifiMessageBean
import kotlinx.android.synthetic.main.item_wifi_container.view.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 17:09:41
 * @class describe
 */
class HomeWifiAdapter:BaseQuickAdapter<WifiMessageBean,BaseViewHolder>(R.layout.item_wifi_container) {
    override fun convert(holder: BaseViewHolder, item: WifiMessageBean) {
        holder.itemView.apply {
            mWifiName.text=item.wifiName
            item.encryptionWay.let {
                if (it.contains("WPA2") and it.contains("WPS") || it.contains("WPA")) {
                    mWifiStateHint.text="加密wifi"
                    mWifiStateIcon.visibility=View.VISIBLE
                }
                else{
                    mWifiStateHint.text="免费wifi"
                    mWifiStateIcon.visibility=View.GONE
                }
            }
        }
    }
}