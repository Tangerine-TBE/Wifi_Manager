package com.feisukj.cleaning.ui.activity

import android.annotation.SuppressLint
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.module_base.cleanbase.BaseActivity
import com.example.module_base.cleanbase.BaseConstant
import com.example.module_base.cleanbase.GuideActivity
import com.example.module_base.cleanbase.toast

import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.NotificationAdapter
import com.feisukj.cleaning.bean.NotificationBean
import com.feisukj.cleaning.service.MyNotificationListenerService
import com.feisukj.cleaning.service.ensureServiceIsRunning

import kotlinx.android.synthetic.main.act_notification_clean.*
import kotlinx.android.synthetic.main.act_notification_s_clean.*
import java.util.*

class NotificationCleanActivity: BaseActivity() {
    companion object{
        const val NOTIFICATION_BROADCAST_ACTION="NOTIFICATION_BROADCAST_ACTION"
    }
    private var notificationService:MyNotificationListenerService?=null
    private var adapter: NotificationAdapter? = null
    private val listData = ArrayList<NotificationBean>()
    override fun getLayoutId()= R.layout.act_notification_clean
    override fun initView() {
        barTitle.setText(R.string.noticeClean)
        mImmersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()
        if (!isNotificationServiceEnabled()) {
            requestRootView.visibility = View.VISIBLE
            requestP.setOnClickListener {
                buildNotificationServiceAlertDialog().show()
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotificationAdapter(this, listData)
        recyclerView.adapter = adapter
        registerBroadcastReceiver()
        upData()
        initClick()
        ensureServiceIsRunning(this)

        MyNotificationListenerService.notificationListenerServices.add(object :MyNotificationListenerService.StartCommand{
            override fun startCommand(notificationListenerService: MyNotificationListenerService) {
                super.startCommand(notificationListenerService)
                notificationService=notificationListenerService
                upData()
            }
        })
        startService(Intent(this,MyNotificationListenerService::class.java))
    }

    override fun onResume() {
        super.onResume()
        if (isNotificationServiceEnabled() && requestRootView != null) {
            requestRootView.visibility = View.GONE
            val v = findViewById<View>(R.id.frame)
            v.visibility = View.VISIBLE
            BaseConstant.mainHandler.postDelayed({ v.visibility = View.GONE }, 500)
            upData()
        }
    }
    private fun initClick(){
        barBack.setOnClickListener { finish() }
        clean.setOnClickListener {
            if (notificationService != null) {
                try {
                    notificationService?.cancelAllNotifications()
                }catch (e: Exception){
                    //MobclickAgent.reportError(this,e)
                    toast(R.string.cleanFail)
                }
                listData.clear()
                count.text = "0"
                adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun buildNotificationServiceAlertDialog(): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.ApplicationAuthority)
        alertDialogBuilder.setMessage(R.string.authorityDes)
        alertDialogBuilder.setPositiveButton(R.string.yes) { dialog, which ->
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
            startActivity(Intent(this, GuideActivity::class.java))
        }
        alertDialogBuilder.setNegativeButton(R.string.no) { dialog, which ->
            Toast.makeText(this, R.string.noDes, Toast.LENGTH_SHORT)
                    .show()
        }
        return alertDialogBuilder.create()
    }

    /**
     * 判断NotificationListenerServices是否被授予Notification Access权限
     * @return
     */
    private fun isNotificationServiceEnabled(): Boolean {
        return NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)
    }


    @SuppressLint("SetTextI18n")
    private fun upData() {
        val service=notificationService?:return
        listData.clear()
        val activeNotifications=service.activeNotifications?:return
        for (statusBarNotification in activeNotifications) {
            if (statusBarNotification.isClearable) {
                val notificationBean = NotificationBean()
                notificationBean.packageName = statusBarNotification.packageName
                notificationBean.id = statusBarNotification.id
                notificationBean.time = statusBarNotification.postTime
                if (Build.VERSION.SDK_INT >= 19) {
                    val extras = statusBarNotification.notification.extras
                    notificationBean.title = extras.getString(Notification.EXTRA_TITLE) //通知title
                    val content = extras.getString(Notification.EXTRA_TEXT) //通知内容
                    notificationBean.content = content
                }
                notificationBean.pendingIntent = statusBarNotification.notification.contentIntent
                listData.add(notificationBean)
            }
        }
        BaseConstant.mainHandler.post {
            count.text = listData.size.toString() + ""
            adapter?.notifyDataSetChanged()
        }
    }

    private fun registerBroadcastReceiver(){
        val broadcastReceiver=NotificationBroadcastReceiver()
        val intentFilter=IntentFilter(NOTIFICATION_BROADCAST_ACTION)
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,intentFilter)
    }
    inner class NotificationBroadcastReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            upData()
        }
    }

}