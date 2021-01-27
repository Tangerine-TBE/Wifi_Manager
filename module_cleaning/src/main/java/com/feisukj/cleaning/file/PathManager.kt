package com.feisukj.cleaning.file

import android.os.Build
import com.feisukj.cleaning.utils.getInstallAppPackageName

object PathManager {

    fun getUnloadingResiduePath():List<ApplicationGarbage>{
        val packageNames= getInstallAppPackageName()?:return emptyList()
        val unInstallPath= mutableListOf(*applicationGarbagePath.toTypedArray())
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
            unInstallPath.removeIf {
                packageNames.contains(it.packageName)
            }
        }else{
            val t=applicationGarbagePath.filter {
                packageNames.contains(it.packageName)
            }
            unInstallPath.removeAll(t)
        }
        return unInstallPath
    }
}