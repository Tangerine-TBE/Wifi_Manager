
package com.example.module_base.cleanbase
import android.app.Activity
import android.app.Application
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.widget.Toast
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
fun Activity.toast(msg: Int) {
    Handler(mainLooper).post {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.toast(msg: String) {
    Handler(BaseApplication.application.mainLooper).post {
        Toast.makeText(BaseApplication.application, msg, Toast.LENGTH_SHORT).show()
    }
}

fun Any.dLog(text:String,tag:String=this.javaClass.simpleName){
    Log.d("myLog,$tag",text)
}

fun Any.iLog(text:String,tag:String=this.javaClass.simpleName){
    Log.i("myLog,$tag",text)
}

fun Any.eLog(text:String,tag:String=this.javaClass.simpleName){
    Log.e("myLog,$tag",text)
}

fun Application.toast(msg: String) {
    Handler(mainLooper).post {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

