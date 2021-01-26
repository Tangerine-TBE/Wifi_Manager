package com.example.wifi_manager.ui.popup

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.PopupSafetyWindowBinding
import com.example.wifi_manager.domain.ItemBean
import com.example.wifi_manager.ui.adapter.recycleview.SafetyCheckAdapter
import com.example.wifi_manager.utils.DataProvider

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/26 15:01:39
 * @class describe
 */
class SafetyCheckPopup(activity: FragmentActivity? ):BasePopup<PopupSafetyWindowBinding>(activity, R.layout.popup_safety_window,ViewGroup.LayoutParams.MATCH_PARENT) {

    private val mAdapter by lazy {
        SafetyCheckAdapter()
    }

    init {
        mView.apply {
            safetyCheckContainer.layoutManager = LinearLayoutManager(activity)
            safetyCheckContainer.adapter=mAdapter
        }

    }


    fun setCheckData( list:MutableList<ItemBean>){
       mAdapter.setList(list)
    }

}