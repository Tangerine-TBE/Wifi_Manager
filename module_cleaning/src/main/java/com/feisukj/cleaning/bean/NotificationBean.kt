package com.feisukj.cleaning.bean

import android.app.PendingIntent
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.Bitmap



class NotificationBean {
    var title:String?=null
    var content:String?=null
    var pendingIntent:PendingIntent?=null
    var time:Long?=null
    var id:Int?=null
    var packageName:String?=null
}
