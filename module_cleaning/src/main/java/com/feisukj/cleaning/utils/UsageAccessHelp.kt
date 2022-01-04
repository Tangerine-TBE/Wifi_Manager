package com.feisukj.cleaning.utils

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import android.util.Log
import com.example.module_base.cleanbase.BaseConstant

import java.util.*

//https://blog.csdn.net/dabaoonline/article/details/51832163
class UsageAccessHelp private constructor(){
    companion object{
        val instance by lazy { UsageAccessHelp() }
        private inline fun getUsagestats():String{
            return if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP_MR1) Context.USAGE_STATS_SERVICE else "usagestats"
        }
    }
    //    首先检测设备是否有“有权查看使用情况的应用”
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun isNoOption(): Boolean {
        val packageManager = BaseConstant.application
                .packageManager
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        val list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY)
        return list.size > 0
    }
    //    如果有“有权查看使用情况的应用”判断是否开启
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun isNoSwitch(): Boolean {
        val ts = System.currentTimeMillis()
        val usageStatsManager = BaseConstant.application
                .getSystemService(getUsagestats()) as UsageStatsManager
        val queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, ts-10000000, ts)
        return !(queryUsageStats == null || queryUsageStats.isEmpty())
    }
//    代码实现查看正在运行的应用包名
    fun getTopAppName(context: Context): String {
//        val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as UserActivityManager
        var strName = ""
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                strName = getLollipopFGAppPackageName(context)
            } else {
//                strName = mActivityManager!!.getRunningTasks(1).get(0).topActivity
//                mActivityManager.getRecentTasks(1,UserActivityManager.RECENT_IGNORE_UNAVAILABLE).first()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return strName
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getLollipopFGAppPackageName(ctx: Context): String {
        try {
            val usageStatsManager = ctx.getSystemService(getUsagestats()) as UsageStatsManager
            val milliSecs = (60 * 1000).toLong()
            val date = Date()
            val queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, date.getTime()-milliSecs*20, date.getTime())
            if (queryUsageStats.size > 0) {
                Log.i("LPU", "queryUsageStats size: " + queryUsageStats.size)
            }
            var recentPkg =queryUsageStats.maxByOrNull {
                it.lastTimeStamp
            }?.packageName?:""
            if (Constant.SYSTEM_SERVICE_PACKAGENAME.contains(recentPkg)){
                val sortUsageStats=queryUsageStats.sortedBy { -it.lastTimeStamp }
                var i=1
                do {
                    if (i<sortUsageStats.size){
                        recentPkg=sortUsageStats[i].packageName
                        Log.e(this.javaClass.simpleName,"使用记录的倒数第${i+1}个包名:${recentPkg}")
                    }
                    i++
                }while (Constant.SYSTEM_SERVICE_PACKAGENAME.contains(recentPkg)&&i<sortUsageStats.size)
            }
            return recentPkg
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}