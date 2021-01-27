package com.feisukj.cleaning.service

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.feisukj.cleaning.ui.activity.NotificationCleanActivity

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class MyNotificationListenerService: NotificationListenerService() {
    companion object{
        val notificationListenerServices:ArrayList<StartCommand> by lazy { ArrayList<StartCommand>() }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationListenerServices.forEach {
            try {
                it.startCommand(this)
            }catch (e: Exception){

            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val intent=Intent(NotificationCleanActivity.NOTIFICATION_BROADCAST_ACTION)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        val intent=Intent(NotificationCleanActivity.NOTIFICATION_BROADCAST_ACTION)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
    override fun onListenerDisconnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requestRebind(ComponentName(this, MyNotificationListenerService::class.java))
        }
    }

    interface StartCommand{
        /**
         * 当回调了一次后，该回调接口就会被移除；不会再回调了
         */
        fun startCommand(notificationListenerService: MyNotificationListenerService){
            notificationListenerServices.remove(this)
        }
    }
}
/**
 * 确保NotificationListenerService在后台运行，所以通过判断服务是否在运行中的服务中来进行触发系统rebind操作
 */
fun ensureServiceIsRunning(context: Context) {
    val serviceComponent = ComponentName(context, MyNotificationListenerService::class.java)
    Log.d("tagtag", "确保服务NotificationListenerExampleService正在运行")
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    var isRunning = false
    val runningServiceInfos = manager.getRunningServices(Integer.MAX_VALUE)
    if (runningServiceInfos == null) {
        Log.w("tagtag", "运行中的服务为空")
        return
    }

    for (serviceInfo in runningServiceInfos) {
        if (serviceInfo.service == serviceComponent) {
            if (serviceInfo.pid == Process.myPid()) {
                isRunning = true
            }
        }
    }

    if (isRunning) {
        Log.d("tagtag", "ensureServiceIsRunning: 监听服务正在运行")
        return
    }

    Log.d("tagtag", "ensureServiceIsRunning: 服务没有运行，重启中...")
    toggleNotificationListenerService(context)
}

/**
 * 不调用下面的函数，第一次安装使用app能够正常读取通知栏的通知。但是把app进城杀掉重启发现不能拦截到通知栏消息，
 * 这是因为监听器服务没有开启，更深层的原因是没有bindService。解决方法是把NotificationListenerService的实
 * 现类disable后再enable，这样可以触发系统的rebind操作。
 *
 * 下面的做法会有点小延迟，大约在10s钟左右。
 */
private fun toggleNotificationListenerService(context: Context) {
    val pm = context.packageManager
    pm.setComponentEnabledSetting(ComponentName(context, MyNotificationListenerService::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
    pm.setComponentEnabledSetting(ComponentName(context, MyNotificationListenerService::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
}