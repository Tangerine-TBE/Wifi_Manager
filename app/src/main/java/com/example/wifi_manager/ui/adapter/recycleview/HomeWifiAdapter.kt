package com.example.wifi_manager.ui.adapter.recycleview

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ItemMyBottomContainerBinding
import com.example.wifi_manager.databinding.ItemWifiContainerBinding
import com.example.wifi_manager.domain.WifiMessageBean
import com.example.wifi_manager.viewmodel.HomeViewModel


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.adapter
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 17:09:41
 * @class describe
 */
class HomeWifiAdapter:BaseQuickAdapter<WifiMessageBean, BaseDataBindingHolder<ItemWifiContainerBinding>>(R.layout.item_wifi_container) {
    override fun convert(holder:  BaseDataBindingHolder<ItemWifiContainerBinding>, item: WifiMessageBean) {
        holder.dataBinding?.apply {
            mWifiName.text=item.wifiName
            mWifiLevel.setImageResource(wifiSignalState(item.wifiLevel))
            item.wifiProtectState.let {
                if (it!=HomeViewModel.OPEN) {
                    mWifiStateIcon.visibility=View.VISIBLE
                    mWifiStateIcon.setImageResource( if (item.shareState) R.mipmap.icon_home_open_lock else R.mipmap.icon_wifi_protect)
                    when {
                        item.shareState -> {
                            mWifiStateHint.text="推荐"
                        }
                        !item.shareState and item.saveWifiPwdState -> {
                            mWifiStateHint.text = "已保存密码"
                        }


                    }

                }
                else{
                    mWifiStateIcon.visibility=View.GONE
                }
            }




        }
    }

    private fun wifiSignalState(level: Int) =
            when(level){
                in 0 downTo -20->R.mipmap.icon_signal_five
                in -20 downTo -50->R.mipmap.icon_signal_four
                in -50 downTo -60-> R.mipmap.icon_signal_three
                in -60 downTo -70->R.mipmap.icon_signal_two
                in -70 downTo -1000->R.mipmap.icon_signal_one
                else->R.mipmap.icon_signal_five
            }
}