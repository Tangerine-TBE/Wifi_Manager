package com.example.module_base.utils

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.ForwardToSettingsCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.permissionx.guolindev.request.ExplainScope
import com.permissionx.guolindev.request.ForwardScope

object PermissionUtils {
    fun askPermission(fragmentActivity: FragmentActivity,msg:String,permission: MutableList<String>, granted: () -> Unit, denied: () -> Unit) {
        if (permission.indexOf(Manifest.permission.READ_EXTERNAL_STORAGE) != -1&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            permission.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }
        PermissionX.init(fragmentActivity)
            .permissions(permission)
            .explainReasonBeforeRequest()
            .onExplainRequestReason(ExplainReasonCallback { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, msg, "去授权", "拒绝")
            })
            .onForwardToSettings(ForwardToSettingsCallback { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList,"您需要手动开启该权限","去开启","取消")
            })
            .request(RequestCallback { allGranted, grantedList, deniedList ->
                if (allGranted){
                    granted.invoke()
                }else{
                    denied.invoke()
                }
            })
    }
    fun askPermission(fragment: Fragment,msg:String,permission: MutableList<String>, granted: () -> Unit, denied: () -> Unit) {
        if (permission.indexOf(Manifest.permission.READ_EXTERNAL_STORAGE) != -1&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            permission.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }
        PermissionX.init(fragment)
            .permissions(permission)
            .explainReasonBeforeRequest()
            .onExplainRequestReason(ExplainReasonCallback { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, msg, "去授权", "拒绝")
            })
            .onForwardToSettings(ForwardToSettingsCallback { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList,"您需要手动开启该权限","去开启","取消")
            })
            .request(RequestCallback { allGranted, grantedList, deniedList ->
                if (allGranted){
                    granted.invoke()
                }else{
                    denied.invoke()
                }
            })
    }

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