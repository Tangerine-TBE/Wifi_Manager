package com.example.module_base.utils

import android.content.Context
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions

object PermissionUtils {
    fun askPermission(context: Context, vararg permission: String, method: () -> Unit) {
        XXPermissions.with(context)
            .permission(permission)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all){
                        method.invoke()
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    if (never){
                        XXPermissions.startPermissionActivity(context,permissions)
                    }
                }

            })

    }
}