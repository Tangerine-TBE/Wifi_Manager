package com.example.module_base.extensions

import android.app.Activity
import android.app.Dialog
import androidx.lifecycle.liveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.extensions
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/13 16:56:29
 * @class describe
 */

//网络请求
fun <T> Call<T> .exAwait(fail:(t: Throwable)->Unit, success:(response: Response<T>)->Unit) {
    enqueue(object :Callback<T>{
        override fun onFailure(call: Call<T>, t: Throwable) {
            fail(t)
        }
        override fun onResponse(call: Call<T>, response: Response<T>) {
            success(response)
        }
    })
}


suspend fun <T> Call<T>.await():T{
    return suspendCoroutine { continuation->
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (body!=null)
                    continuation.resume(body)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
    }
}


//Dialog
fun Dialog.noFinishShow(activity: Activity?){
    activity?.let {
        if (!it.isFinishing) {
            show()
        }
    }

}

