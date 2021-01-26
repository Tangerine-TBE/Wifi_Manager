package com.example.wifi_manager.livedata

import com.example.wifi_manager.domain.ValueScore
import com.example.wifi_manager.utils.ConstantsUtil

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.livedata
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/26 11:38:21
 * @class describe
 */
object ScoreLiveData : BaseLiveData<ValueScore>() {


    fun getScore(){
        sp.apply {
            val openState= getBoolean(ConstantsUtil.SP_WIFI_PROTECT_OPEN)
            val signalState = getBoolean(ConstantsUtil.SP_SIGNAL_SATE )
            value=when{
                openState and signalState->ValueScore(100,"")
                openState and !signalState->ValueScore(90,"建议优化项1个")
                !openState and  signalState->ValueScore(80,"建议优化项2个")
                else-> ValueScore(70,"建议优化项3个")
            }
        }
    }



}