package com.example.wifi_manager.db

import android.content.ContentValues
import com.example.wifi_manager.domain.SqlitBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litepal.LitePal
import org.litepal.crud.LitePalSupport

/**
 * @author: 铭少
 * @date: 2021/1/23 0023
 * @description：
 */
object SQliteHelper {


    suspend inline fun <reified T : LitePalSupport> saveData(
        condition: String,
        value: String,
        clazz: SqlitBean,
        crossinline block: () -> ContentValues
    ) = withContext(Dispatchers.IO) {
        val find = LitePal.where(condition, value).find(T::class.java)
        if (find.isEmpty()) {
            clazz.save()
        } else {
            LitePal.updateAll(T::class.java, block(), condition, value) > 0
        }
    }


    inline fun <reified T : LitePalSupport> findAllData(): List<T> {
        return LitePal.findAll(T::class.java)
    }


    inline fun <reified T : LitePalSupport> findOneData( condition: String, value: String): List<T> {
     return  LitePal.where(condition,value).find(T::class.java)
    }
}