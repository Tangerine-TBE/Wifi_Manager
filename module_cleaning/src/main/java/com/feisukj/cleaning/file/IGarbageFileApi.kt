package com.feisukj.cleaning.file

import retrofit2.http.GET
import io.reactivex.Observable

interface IGarbageFileApi {
    @GET("/ytkapplicaton/angetClearCatalog")
    fun getAppGarbagePath():Observable<ApplicationPath>
}