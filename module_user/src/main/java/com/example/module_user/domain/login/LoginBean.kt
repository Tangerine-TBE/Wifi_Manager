package com.example.module_user.domain.login

/**
 * @author: 铭少
 * @date: 2021/1/24 0024
 * @description：
 */

data class LoginBean(
        val `data`: Data,
        val msg: String,
        val ret: Int
)

data class Data(
    val activedays: Int,
    val activetime: String,
    val addip: String,
    val addplat: Int,
    val addtime: String,
    val adduuid: String,
    val addver: String,
    val callprice: Int,
    val cdrcount: Int,
    val cdrtime: Int,
    val channelid: String,
    val chargecount: Int,
    val chargeold: Int,
    val concern: Int,
    val concerned: Int,
    val credit: Int,
    val forbidreason: String,
    val forbidtime: String,
    val gifts: String,
    val goldcoin: Int,
    val id: Int,
    val invitation: Int,
    val isforbid: Int,
    val lastip: String,
    val lastplat: String,
    val lasttime: String,
    val lastuuid: String,
    val lastver: String,
    val logincount: Int,
    val medals: String,
    val mobile: String,
    val noticeid: Int,
    val now: String,
    val offlinemsg: Int,
    val openid: String,
    val partnerlevel: Int,
    val partnertime: String,
    val password: String,
    val pictype: Int,
    val report: Int,
    val sendgifts: String,
    val star: Int,
    val tvreceivecall: Int,
    val type: Int,
    val unique: String,
    val usertype: Int,
    val uuidfirst: String,
    val vip: Int,
    val vipexpiretime: String,
    val virginid: Int
)
