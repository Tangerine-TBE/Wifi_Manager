package com.example.wifi_manager.base

import android.app.Activity
import android.os.Bundle
import com.example.module_ad.utils.BaseBackstage
import com.example.module_base.base.BaseApplication

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.base
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 15:57:57
 * @class describe
 */
class MainApplication:BaseApplication() {

    override fun initData() {

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            private var activityStartCount = 0

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {
                activityStartCount++;
                //数值从0 变到 1 说明是从后台切到前台
                if (activityStartCount == 1) {
                    //从后台切到前台
                        BaseBackstage.setBackstage(this@MainApplication)

                }
            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {
                activityStartCount--;
                //数值从1到0说明是从前台切到后台
                if (activityStartCount == 0) {
                    //从前台切到后台
                        BaseBackstage.setStop(this@MainApplication)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        })
    }






}