package com.example.wifi_manager.utils

import com.example.wifi_manager.R
import com.example.wifi_manager.domain.ItemBean

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.utils
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 14:29:40
 * @class describe
 */
object DataProvider {


    val homeTopList= arrayListOf(
        ItemBean(icon = R.mipmap.ic_launcher,title = "一键加速"),
        ItemBean(icon = R.mipmap.ic_launcher,title = "安全检测"),
        ItemBean(icon = R.mipmap.ic_launcher,title = "网络加速"),
        ItemBean(icon = R.mipmap.ic_launcher,title = "WiFi保镖"),
        ItemBean(icon = R.mipmap.ic_launcher,title = "信号增强")
    )


    val myTopList= arrayListOf(
            ItemBean(icon = R.mipmap.icon_my_scan,title = "扫一扫"),
            ItemBean(icon = R.mipmap.icon_my_protect,title = "WiFi保镖"),
            ItemBean(icon = R.mipmap.icon_my_speed_test,title = "网络测速"),
            ItemBean(icon = R.mipmap.icon_my_distance,title = "测距仪"),
            ItemBean(icon = R.mipmap.icon_my_optimize,title = "硬件优化")
    )

   val myBottomList= arrayListOf(
           ItemBean(title = "应用设置"),
           ItemBean(title = "取消WiFi分享"),
           ItemBean(title = "帮助与反馈"),
           ItemBean(title = "用户协议"),
           ItemBean(title = "隐私政策"),
           ItemBean(title = "关于我们"),
           ItemBean(title = "检测更新"),
           ItemBean(title = "账号注销")
   )

    val shareApplyList= arrayListOf(
        ItemBean(title = "WiFi名称",hint = "输入WiFi名称"),
        ItemBean(title = "Mac地址",hint = "输入Mac地址"),
        ItemBean(title = "WiFi密码",hint = "输入密码以已证身份")
    )


    val wifiProtectList= arrayListOf(
        ItemBean(icon = R.mipmap.ic_launcher,title = "防恶意蹭网    "),
        ItemBean(icon = R.mipmap.ic_launcher,title = "保障不被分享"),
        ItemBean(icon = R.mipmap.ic_launcher,title = "实时入侵警示"),
        ItemBean(icon = R.mipmap.ic_launcher,title = "防破解监听    ")
    )


}