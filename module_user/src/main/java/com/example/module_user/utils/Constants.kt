package com.example.module_user.utils

/**
 * @author: 铭少
 * @date: 2021/1/24 0024
 * @description：
 */
object Constants {
    const val PLATFORM_KEY = "CHANNEL" //平台
    const val APP_NAME = "APP_NAME" //app名字

    //注册、登陆参数
    const val SERVICE = "service" //接口名

    const val MOBILE = "mobile" //电话

    const val PACKAGE = "package" //包名

    const val SIGNATURE = "signature" //验证码

    const val NONCE = "nonce" //随机数

    const val TIMESTAMP = "timestamp" //时间戳

    const val TOKEN = "^x389fhfeahykge" //token值

    const val QQ_TYPE = "1" //QQ

    const val WECHAT_TYPE = "0" //微信

    const val OPENID = "openId" //openId

    const val TYPE = "type" //openId

    const val CODE = "code" //验证码

    const val PLATFORM = "platform" //平台

    const val PASSWORD = "password" //密码


    //支付参数
    const val TRADE = "trade" //订单号

    const val SUBJECT = "subject" //标题

    const val PRICE = "price" //价格

    const val BODY = "body" //内容

    //购买信息
    const val VIP13 = "VIP13"
    const val VIP12 = "VIP12"
    const val VIP3 = "VIP3"
    const val VIP1 = "VIP1"

    const val VIP_title_13 = "永久卡"
    const val VIP_title_12 = "一年"
    const val VIP_title_3 = "三个月"
    const val VIP_title_1 = "一个月"


    /*--------------/注册接口-----------*/ //微信
    const val WECHAT_URL = "https://api.weixin.qq.com/sns/oauth2/"

    //域名
    const val USER_URL = "https://www.aisou.club"

    //支付
    const val PAY_WX_URL = "http://www.aisou.club/pay/wxh5/dafa.php?"
    const val PAY_ALI_URL = "http://www.aisou.club/pay/aliv2/wappay/pay.php?"

    ////////////////////SeverName///////////////////
    //注册验证码
    const val GET_CODE = "passport.regcode"

    //找回密码验证码
    const val GET_FIND_PWD_CODE = "passport.findPassword"

    //校验验证码
    const val CHECK_CODE = "passport.checkcode"

    //注册
    const val ADD_USER = "passport.registerByMobile"

    //登录
    const val LOGIN = "passport.loginMobile"

    //找回密码
    const val FIND_PWD = "passport.setPassByFind"

    //注销账号
    const val DELETE_USER = "passport.unregister"

    //ali_pay
    const val ALI_PAY = "ALI"

    //wx_pay
    const val WX_PAY = "WX"

    //验证QQ微信是否注册
    const val CHECK_THIRD = "passport.checkThird"

    //第三方注册
    const val REGISTER_BY_THIRD = "passport.registerByThird"

    //第三方登录
    const val LOGIN_THIRD = "passport.loginThird"

    //获取手机号码
    const val GET_PHONE = "passport.getMobile"


    //测试
    /*    public static final double VIP_price_13 = 0.01;
    public static final double VIP_price_12 = 0.01;
    public static final double VIP_price_3 =0.01;
    public static final double VIP_price_1 = 0.01;*/

    //储存用户信息
    const val LOCAL_TYPE = "2" //本地登陆
    const val USER_INFO = "user_info"
    const val USER_ID = "id"
    const val USER_IS_LOGIN = "isLogin"
    const val USER_ID_TYPE = "id_type"
    const val USER_ACCOUNT = "user_account"
    const val USER_PWD = "user_pwd"
    const val USER_THIRDLY_OPENID = "user_thirdly_openid"
    const val USER_VIP_LEVEL = "vip_level"
    const val USER_VIP_TIME = "vip_time"
    const val USER_LOGIN_TIME = "user_login_time"





    //Intent  ket
    const val USER_ACTION="USER_ACTION"



}