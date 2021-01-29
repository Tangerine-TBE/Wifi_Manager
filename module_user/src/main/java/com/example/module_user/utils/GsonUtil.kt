package com.example.module_user.utils

import com.example.module_user.domain.GeneralMsg
import com.google.gson.Gson
import org.json.JSONObject
import java.net.HttpURLConnection

/**
 * @author: 铭少
 * @date: 2021/1/24 0024
 * @description：
 */
object GsonUtil {

   inline fun <reified T>setUserResult(resultStr:String,success:(T)->Unit,fail:(failMsg:GeneralMsg)->Unit){
       try {
           val ret = JSONObject(resultStr).getInt("ret")
           if (ret == 200) {
               success(Gson().fromJson(resultStr, T::class.java))
           } else {
               fail(Gson().fromJson(resultStr, GeneralMsg::class.java))
           }
       }catch (e:Exception){

       }
    }


    inline fun <reified T> strResolve(str:String):T?{
        return if (str != "") {
           Gson().fromJson(str, T::class.java)
        } else {
            null
        }
    }
}