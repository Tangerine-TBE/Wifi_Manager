package com.feisukj.cleaning.setting

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface SettingLinkService {
    @GET("/ytkapplicaton/angetTuiJianList")
    fun getSettingConfig(@Query("name") packName:String,
                         @Query("channel") channel:String,
                         @Query("version") version:String): Observable<SettingLinkConfig>
}