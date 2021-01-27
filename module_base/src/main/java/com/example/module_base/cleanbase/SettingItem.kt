package com.example.module_base.cleanbase

import android.content.Context
import android.content.Intent


class SettingItem{
    var status=false
    get() {
        return false
    }
    var type="miniprogram"
    var userName="gh_d1978695dca9"
    var appId="wx143822f474eb9e71"
    var url=""
    var desc=""
    var name=""
    var imageUrl=""

    fun getLinkType(): LinkType {
        return when(type){
            "miniprogram"->{
                LinkType.miniprogram
            }
            "BMW,h5"->{
                LinkType.BMW
            }
            ""->{
                LinkType.APK
            }
            else->{
                LinkType.unkown
            }
        }
    }

    fun openMiniProgram(){
   /*     val api = WXAPIFactory.createWXAPI(BaseConstant.application, appId)
        val req = WXLaunchMiniProgram.Req()
        req.userName = userName // 填小程序原始id
//            req.path = path                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE// 可选打开 开发版，体验版和正式版
        api.sendReq(req)*/
    }

    fun openBmw(context: Context){
       /* val intent= Intent(context, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.URL_KEY,url)
        context.startActivity(intent)*/
    }
}