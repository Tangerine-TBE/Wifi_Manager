package com.example.wifi_manager.repository
import com.example.wifi_manager.repository.api.ApiService
import com.example.wifi_manager.utils.RetrofitClient

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.repository
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/13 13:45:11
 * @class describe
 */
object NetSpeedTestRepository {

      fun getNetSpeed() =  RetrofitClient.createNetSpeed().downFile()


}