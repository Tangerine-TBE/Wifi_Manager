package com.example.wifi_manager.ui.popup

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.module_base.utils.LogUtils
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.PopupConnectStateWindowBinding
import com.example.wifi_manager.utils.StepState


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/19 14:10:46
 * @class describe
 */
class ConnectStatePopup(activity: FragmentActivity?):BasePopup<PopupConnectStateWindowBinding>(activity, R.layout.popup_connect_state_window, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) {
    //icon_home_wifi_normal
    companion object{
        const val WIFI_CONNECT="正在建立连接..."
        const val WIFI_WRITE_PWD="写入密码..."
        const val WIFI_CHECK_PWD= "验证密码..."
        const val WIFI_DISPENSE_IP= "正在分配IP地址..."
    }

    init {
        isFocusable=true
        isOutsideTouchable =false

    }

    fun setConnectName(name:String?){
        mView.connectName.text=name?:""
    }

    private var mState=false
    fun setState(state:Boolean){
        mState= state
    }


    override fun initEvent() {
        setOnDismissListener {
            mOutValueAnimator?.start()
            mView.connectWifiView.setStepState(StepState.ONE)
        }
    }

    fun setConnectState(state: StepState){
        mView.apply {
            LogUtils.i("----setConnectState----$state--------------------")
            connectWifiView.setStepState(state,mState)
            connectProgressView.setProgressState(state)
            connectStep.text=""+when(state){
                StepState.ONE->WIFI_CONNECT
                StepState.TWO-> if (mState) WIFI_CONNECT else WIFI_WRITE_PWD
                StepState.THREE->if (mState) WIFI_CONNECT else WIFI_CHECK_PWD
                StepState.FOUR-> WIFI_DISPENSE_IP
                StepState.FIVE->WIFI_DISPENSE_IP
                else->WIFI_CONNECT
            }
        }
    }

}