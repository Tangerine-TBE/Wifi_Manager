package com.example.module_ad.repository

import com.example.module_ad.request.RetrofitUtil
import com.example.module_ad.utils.Contents
import com.example.module_base.base.BaseApplication
import com.example.module_base.base.BaseApplication.Companion.application
import com.example.module_base.utils.PackageUtil

/**
 * @name Wifi_Manager
 * @class name：com.example.module_ad.repository
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/28 14:40:09
 * @class describe
 */
object AdRepository {

    fun getAdMsg()=
        RetrofitUtil.getInstance().adService.getAdMessage( mapOf(
            Contents.AD_NAME to BaseApplication.mPackName,
            Contents.AD_VERSION to PackageUtil.packageCode2(application),
            Contents.AD_CHANNEL to PackageUtil.getAppMetaData(application,Contents.PLATFORM_KEY)
        ))

}