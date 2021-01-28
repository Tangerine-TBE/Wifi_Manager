package com.example.wifi_manager.ui.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.utils.*
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityWifiProtectInfoBinding
import com.example.wifi_manager.livedata.ScoreLiveData
import com.example.wifi_manager.livedata.WifiStateLiveData
import com.example.wifi_manager.ui.adapter.recycleview.ProtectWifiCheckAdapter
import com.example.wifi_manager.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class WifiProtectInfoViewActivity : BaseViewActivity<ActivityWifiProtectInfoBinding>() {
    override fun getLayoutView(): Int = R.layout.activity_wifi_protect_info

    private val mProtectInfoAdapter by lazy {
        ProtectWifiCheckAdapter()
    }

    override fun initView() {
        binding.apply {
            setToolBar(this@WifiProtectInfoViewActivity,"开启保护",mProtectInfoToolbar)
            protectWifiName.text=WifiUtils.getConnectWifiName()+""
            val endTime = sp.getLong(ConstantsUtil.SP_WIFI_PROTECT_TIME, 0L)
            protectTime.text="已保护${
                calLastedTime(
                    Date(endTime),
                    Date(System.currentTimeMillis())
                )
            }天"

            val dayTime = sp.getLong(ConstantsUtil.SP_WIFI_PROTECT_DAY, 0L)
            outTimeHint.text="下次登录时间：${inner7Day(dayTime)}前"+"\n"+"为验证你是WIFI主人，若超过7天未登录将取消保护"

            protectContainer.also {
                it.layoutManager= LinearLayoutManager(this@WifiProtectInfoViewActivity)
                it.adapter=mProtectInfoAdapter
                mProtectInfoAdapter.setList(DataProvider.protectInfoList)
            }

        }


        WifiStateLiveData.observe(this, {
            when(it){
                WifiState.DISABLED->{
                    finish()
                    showToast("WiFi已关闭")
                }
            }
        })



        startAnimation()

    }



    override fun initEvent() {





        binding.apply {

            mProtectInfoToolbar.toolbarEvent(this@WifiProtectInfoViewActivity) {}


            cancelProtect.setOnClickListener {
                sp.apply {
                    putBoolean (ConstantsUtil.SP_WIFI_PROTECT_OPEN,false)
                    putString (ConstantsUtil.SP_WIFI_PROTECT_NAME,"")
                }
                finish()
            }

        }
    }


    private  fun startAnimation(){
        mScope.launch (Dispatchers.Main){
            for (i in 0 until  5){
                mProtectInfoAdapter.setStepState(HardwareTweaksActivity.state[i])
                delay(1000)
            }
        }
    }

}