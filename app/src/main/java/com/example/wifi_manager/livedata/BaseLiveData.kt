package com.example.wifi_manager.livedata

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.livedata
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/21 11:13:53
 * @class describe
 */
open class BaseLiveData<T>:LiveData<T>() {
    protected val mJob=Job()
    protected var mScope=CoroutineScope(mJob)


}