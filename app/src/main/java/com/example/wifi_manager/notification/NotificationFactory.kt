package com.example.wifi_manager.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.module_base.base.BaseApplication
import com.example.wifi_manager.R

import com.example.wifi_manager.ui.activity.MainViewActivity
import com.example.wifi_manager.ui.activity.SafetyCheckActivity
import com.example.wifi_manager.ui.activity.ScanActivity
import com.example.wifi_manager.ui.activity.SpeedTestViewActivity
import com.example.wifi_manager.ui.adapter.recycleview.ProtectWifiCheckAdapter
import com.example.wifi_manager.utils.ConstantsUtil

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.notification
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/2/1 11:19:55
 * @class describe
 */
object NotificationFactory {

    private val  mNotificationManager = BaseApplication.application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(ConstantsUtil.ACTION_WIFI_CONNECT_CANCEL, "WIFI通知", NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(notificationChannel)
        }

    }



    fun noWiFiNotification() {
            val remoteViews = RemoteViews(BaseApplication.mPackName, R.layout.notification_no_wifi_container)
            remoteViews.setTextViewText(R.id.notification_title,"未连接WiFi")
            remoteViews.setTextViewText(R.id.notification_content, "一键连接附近热点")
            remoteViews.setOnClickPendingIntent(R.id.notification_scan, PendingIntent.getActivity(BaseApplication.application, 0, Intent(BaseApplication.application, ScanActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
            remoteViews.setOnClickPendingIntent(R.id.notification_include, PendingIntent.getActivity(BaseApplication.application, 0, Intent(BaseApplication.application, MainViewActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
        val build = NotificationCompat.Builder(BaseApplication.application, ConstantsUtil.ACTION_WIFI_CONNECT_CANCEL)
                .setSmallIcon(R.mipmap.app_logo)
                .setOngoing(true)
                .setCustomContentView(remoteViews)
                .build()
        mNotificationManager.notify(1,build)
    }


    fun wiFiNotification(name:String) {
        val remoteViews = RemoteViews(BaseApplication.mPackName, R.layout.notification_wifi_container)
        remoteViews.setTextViewText(R.id.notification_title,name)
        remoteViews.setTextViewText(R.id.notification_content, "点击检查网络安全")
        remoteViews.setOnClickPendingIntent(R.id.notification_protect,
                PendingIntent.getActivity(BaseApplication.application, 0,  Intent(BaseApplication.application, SafetyCheckActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
        remoteViews.setOnClickPendingIntent(R.id.notification_speed,
                PendingIntent.getActivity(BaseApplication.application, 0, Intent(BaseApplication.application, SpeedTestViewActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
        remoteViews.setOnClickPendingIntent(R.id.notification_include, PendingIntent.getActivity(BaseApplication.application, 0, Intent(BaseApplication.application, SafetyCheckActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
        val build =NotificationCompat.Builder(BaseApplication.application,ConstantsUtil.ACTION_WIFI_CONNECT_CANCEL)
                .setSmallIcon(R.mipmap.app_logo)
                .setOngoing(true)
                .setCustomContentView(remoteViews)
                .build()
        mNotificationManager.notify(1,build)
    }


}