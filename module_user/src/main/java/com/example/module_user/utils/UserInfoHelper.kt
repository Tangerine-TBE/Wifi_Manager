package com.example.module_user.utils

import com.example.module_base.base.BaseApplication
import java.util.*

/**
 * @author: 铭少
 * @date: 2021/1/24 0024
 * @description：
 */
object UserInfoHelper {

    fun doCode(phoneNumber: String):Map<String, Any>{
        //获取随机数
        val random = Random().nextInt()
        //获取时间戳
        val currentTimeMillis = System.currentTimeMillis()
        val map1: MutableMap<String, String> = TreeMap()
        map1[Constants.PACKAGE] = BaseApplication.mPackName
        map1[Constants.MOBILE] = phoneNumber
        val value1 = SortMapUtil.sortMapByValue(map1)
        //Md5值
        val checkCode: String = ApiMapUtil.md5(Constants.TOKEN + currentTimeMillis + random + Constants.GET_CODE + value1)
        return ApiMapUtil.setMapValues(
            Constants.GET_CODE,
            currentTimeMillis,
            random,
            checkCode,
            map1
        )
    }

    fun userEvent(event:String, userInfo:Map<String, String>):Map<String, Any>{
        val currentTimeMillis = System.currentTimeMillis()
        val random = Random().nextInt()
        val sortMapByValue = SortMapUtil.sortMapByValue(userInfo)
        val md5 = ApiMapUtil.md5(Constants.TOKEN + currentTimeMillis + random + event + sortMapByValue)
        return ApiMapUtil.setMapValues(event, currentTimeMillis, random, md5, userInfo)
    }


    fun userLogOut(event:String, userId:String):Map<String, Any>{
        val currentTimeMillis = System.currentTimeMillis()
        val random = Random().nextInt()
        val md5 = ApiMapUtil.md5(Constants.TOKEN + currentTimeMillis + random + event + userId)
        return ApiMapUtil.setStringValues(event, currentTimeMillis, random, md5, userId)
    }



}