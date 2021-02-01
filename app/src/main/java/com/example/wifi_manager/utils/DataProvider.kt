package com.example.wifi_manager.utils

import android.Manifest
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


    val permissionList = arrayListOf(
            ItemBean(icon = R.mipmap.icon_ps_store, title = "储存", hint = "为您记录蹭网的设备信息"),
            ItemBean(icon = R.mipmap.icon_ps_location, title = "位置", hint = "为您提供附近更多的WiFi信息"),
    )


    val askStoragePermissionLis = arrayListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )




    val askAllPermissionLis = arrayListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    val askCameraPermissionLis = arrayListOf(Manifest.permission.CAMERA)

    val askLocationPermissionLis = arrayListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    val homeTopList = arrayListOf(
            ItemBean(icon = R.mipmap.icon_home_one, title = "一键加速"),
            ItemBean(icon = R.mipmap.icon_home_safety, title = "安全检测"),
            ItemBean(icon = R.mipmap.icon_home_speed_up, title = "网络测速"),
            ItemBean(icon = R.mipmap.icon_home_protect, title = "WiFi保镖"),
            ItemBean(icon = R.mipmap.icon_home_signal_up, title = "信号增强")
    )


    val myTopList = arrayListOf(
            ItemBean(icon = R.mipmap.icon_my_scan, title = "扫一扫"),
            ItemBean(icon = R.mipmap.icon_my_protect, title = "WiFi保镖"),
            ItemBean(icon = R.mipmap.icon_my_speed_test, title = "网络测速"),
            ItemBean(icon = R.mipmap.icon_my_distance, title = "测距仪"),
            ItemBean(icon = R.mipmap.icon_my_optimize, title = "硬件优化")
    )

    val myBottomList = arrayListOf(
            ItemBean(title = "取消WiFi分享"),
            ItemBean(title = "帮助与反馈"),
            ItemBean(title = "用户协议"),
            ItemBean(title = "隐私政策"),
            ItemBean(title = "关于我们"),
            ItemBean(title = "检测更新"),
            ItemBean(title = "账号注销")
    )

    val shareApplyList = arrayListOf(
            ItemBean(title = "WiFi名称", hint = "输入WiFi名称"),
            ItemBean(title = "Mac地址", hint = "输入Mac地址"),
            ItemBean(title = "WiFi密码", hint = "输入密码以已证身份")
    )


    val wifiProtectList = arrayListOf(
            ItemBean(icon = R.mipmap.icon_protect_one, title = "防恶意蹭网    "),
            ItemBean(icon = R.mipmap.icon_protect_two, title = "保障不被分享"),
            ItemBean(icon = R.mipmap.icon_protect_three, title = "实时入侵警示"),
            ItemBean(icon = R.mipmap.icon_protect_four, title = "防破解监听    ")
    )

    val hardwareList = arrayListOf(
            ItemBean(icon = R.mipmap.icon_hardware_loading, title = "优化无线模块内核，提高速度"),
            ItemBean(icon = R.mipmap.icon_hardware_loading, title = "低信号下保持连接，不断线"),
            ItemBean(icon = R.mipmap.icon_hardware_loading, title = "减少延迟，提升网络稳定"),
            ItemBean(icon = R.mipmap.icon_hardware_loading, title = "优化WiFi连接引擎，智能网络加速"),
    )

    val signalWifiList = arrayListOf(
            ItemBean(icon = R.mipmap.icon_signal_select, title = "优化无线模块内核，提高速度"),
            ItemBean(icon = R.mipmap.icon_signal_select, title = "优化无线网络多线程"),
            ItemBean(icon = R.mipmap.icon_signal_select, title = "过滤钓鱼WiFi"),
            ItemBean(icon = R.mipmap.icon_signal_select, title = "优化WiFi内存，减少网络丢包")
    )

    val signalNetList = arrayListOf(
            ItemBean(icon = R.mipmap.icon_signal_select, title = "优化WiFi网络选择"),
            ItemBean(icon = R.mipmap.icon_signal_select, title = "智能调整WAN模式"),
            ItemBean(icon = R.mipmap.icon_signal_select, title = "优化Host/DNS域服务器")
    )

    val safetyList = arrayListOf(
            ItemBean(hint = "信号增强器", title = "", actionHint = "增强信号"),
            ItemBean(hint = "防蹭网扫描", title = "", actionHint = "查看设备"),
            ItemBean(hint = "WiFi保镖", title = "", actionHint = "开启保护"),
            ItemBean(hint = "WiFi测速", title = "", actionHint = "重新测速"),
            ItemBean(hint = "安全检测", title = "", actionHint = "查看详情")
    )

    val protectInfoList = arrayListOf(
            ItemBean(icon = R.mipmap.icon_protect_finish, title = "WIFI平台防破解监听"),
            ItemBean(icon = R.mipmap.icon_protect_finish, title = "密码未被分享"),
            ItemBean(icon = R.mipmap.icon_protect_finish, title = "历史成功拦截破解次数"),
            ItemBean(icon = R.mipmap.icon_protect_finish, title = "每7天需要登录一次")
    )


    val checkList = arrayListOf(
            ItemBean(title = "检查网络是否被监听", hint = "正常"),
            ItemBean(title = "检查是否遭到DNS劫持", hint = "正常"),
            ItemBean(title = "检查是否为钓鱼WiFi", hint = "正常"),
            ItemBean(title = "检查是否加密", hint = "正常"),
            ItemBean(title = "检查Arp是否异常", hint = "正常"),
            ItemBean(title = "检查是否存在SSL Strip攻击", hint = "正常"),
    )


}