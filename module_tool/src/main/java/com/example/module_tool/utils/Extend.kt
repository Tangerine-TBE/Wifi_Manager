
package com.example.module_tool.utils

import android.app.Activity
import android.app.Application
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.module_base.base.BaseApplication


/**
 * Author : Gupingping
 * Date : 2018/7/15
 * Mail : gu12pp@163.com
 */

fun Activity.toast(msg: String) {
    Handler(mainLooper).post {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
fun Activity.toast(msgRes: Int) {
    Handler(mainLooper).post {
        Toast.makeText(this, msgRes, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.toast(msg: String) {
    Handler(BaseApplication.application.mainLooper).post {
        Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
    }
}
fun Fragment.toast(msgRes: Int) {
    Handler(BaseApplication.application.mainLooper).post {
        Toast.makeText(this.context, msgRes, Toast.LENGTH_SHORT).show()
    }
}



fun Application.toast(msg: String) {
    Handler(mainLooper).post {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

