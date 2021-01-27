package com.feisukj.cleaning.bean

import android.app.usage.StorageStats
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.storage.StorageManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.module_base.cleanbase.BaseConstant
import java.io.File
import java.lang.ref.SoftReference
import java.util.*
import kotlin.collections.ArrayList

class AppBean(file:File):FileBean(file) {
    companion object{
        fun getAppBean(file:File):AppBean{
            val appBean= AppBean(file)
            appBean.isApp=false
            try {
                val pm= BaseConstant.application.packageManager
                val packInfo=pm.getPackageArchiveInfo(file.absolutePath,0)
                if (packInfo != null) {
                    val appInfo = packInfo.applicationInfo?:return appBean
                    appInfo.sourceDir = file.absolutePath
                    appInfo.publicSourceDir = file.absolutePath

                    appBean.label=appInfo.loadLabel(pm).toString()
                    appBean.packageName=appInfo.packageName
                    appBean.versionName=packInfo.versionName
                    appBean.icon= SoftReference(appInfo.loadIcon(pm))
                }
                try {
                    appBean.packageName?.let { pm?.getPackageInfo(it, 0) }
                    appBean.isInstall=true
                }catch (e: Exception){
                    appBean.isInstall=false
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
            return appBean
        }

        fun getAppBean(context: Context, packageName: String, isContainSysApp:Boolean=false):AppBean?{
            val pm=context.packageManager
            val appBean:AppBean
            try {
                pm.getPackageInfo(packageName,0).let { info ->
                    if (info.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM!=0&&!isContainSysApp){
                        return null
                    }
                    val icon=info.applicationInfo.loadIcon(pm)
                    val label=info.applicationInfo.loadLabel(pm)
                    val versionName=info.versionName
                    val sourceApk=info.applicationInfo.sourceDir
                    appBean=AppBean(File(sourceApk)).also {
                        it.packageName=packageName
                        it.label=label.toString()
                        it.versionName=versionName
                        it.icon= SoftReference(icon)
                    }

                    try {
                        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                            var appBytes=0L
                            var cacheBytes=0L
                            var dataBytes=0L
                            AppBean.getAppCache(context,info.applicationInfo.uid).forEach {
                                appBytes+=it.appBytes
                                cacheBytes+=it.cacheBytes
                                dataBytes+=it.dataBytes
                            }
                            appBean.appBytes=appBytes
                            appBean.cacheBytes=cacheBytes
                            appBean.dataBytes=dataBytes
                        }
                    }catch (e:SecurityException){
                        e.printStackTrace()
                    }
                }
            }catch (e:Exception){
                return null
            }
            return appBean
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getAppCache(context: Context, uid:Int):List<StorageStats>{
            val storageStatsManager = context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
            val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            val storageVolumes = storageManager.storageVolumes
            val stats=ArrayList<StorageStats>()
            storageVolumes.forEach {
                val uuidStr = it.uuid
                val uuid = if (uuidStr==null){
                    StorageManager.UUID_DEFAULT
                }else{
                    try {
                        UUID.fromString(uuidStr)
                    }catch (e:Exception){
                        e.printStackTrace()
                        null
                    }
                }
                val storageStats=storageStatsManager.queryStatsForUid(uuid?:return emptyList(), uid)
                stats.add(storageStats)
            }
            return stats
        }
    }

    var label=fileName
    var packageName:String?=null
    var versionName:String?="未知"
    var icon: SoftReference<Drawable?>?=null
        get() {
            if (field==null){
                getApkIcon().let {
                    field = if (it!=null){
                        SoftReference(it)
                    }else{
                        SoftReference(null)
                    }
                }
            }else{
                field?.get().let {
                    if (it==null){
                        getApkIcon()?.apply {
                            field= SoftReference(this)
                        }
                    }
                }
            }
            return field
        }
    var isInstall:Boolean=false
    var isApp:Boolean?=null
    var lastUseTimeInterval:Long=0

    var appBytes=0L
    var cacheBytes=0L
    var dataBytes=0L

    val totalSize by lazy { appBytes+cacheBytes+dataBytes+fileSize }

    private fun getApkIcon(): Drawable? {
        val pm: PackageManager = BaseConstant.application.packageManager
        val info = pm.getPackageArchiveInfo(absolutePath,
                0)//PackageManager.GET_ACTIVITIES
        if (info != null) {
            val appInfo = info.applicationInfo
            appInfo.sourceDir = absolutePath
            appInfo.publicSourceDir = absolutePath
            try {
                return appInfo.loadIcon(pm)
            } catch (e: OutOfMemoryError) {
                Log.e("ApkIconLoader", e.toString())
            }
        }
        return null
    }

    override fun hashCode(): Int {
        return absolutePath.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is AppBean) return false
        return absolutePath==other.absolutePath
    }
}