package com.feisukj.cleaning.file

import com.feisukj.cleaning.db.SQLiteDbManager
import com.feisukj.cleaning.utils.Constant

class ApplicationPath{
    var data:Data?=null
}

class Data{
    var ShortVideoPath:List<ShortVideoPath>?=null
    var ApplicationGarbage:List<ApplicationGarbage>?=null
    var ADGarbage:List<ADGarbage>?=null
    var WeAndQQClean:List<WeAndQQClean>?=null
}

data class ShortVideoPath(val packageName:String?){//增加短视频
constructor(packageName: String?,path:String,appName:String?=null):this(packageName){
    this.path=path
    this.appName=appName
}
    var path=""
    var appName:String?=null
}
val shortVideoPath=HashSet<ShortVideoPath>().apply {
    try {
        this.addAll(SQLiteDbManager.getShortVideoPathInfo())
    }catch (e:Exception){
        e.printStackTrace()
    }
}

/**
 * 垃圾文件，缓存表情，朋友圈缓存，其他缓存//微信
 * 垃圾文件，临时文件，头像缓存，空间缓存//QQ
 */
data class WeAndQQClean(val id:Int){
    constructor(id: Int,flag:String,des: String,paths: List<String>):this(id){
        this.des=des
        this.paths=paths
        this.flag=flag
    }
    var flag:String="qq"
    var des:String=""
    var paths:List<String>?=null
}
const val QQ_GARBAGE_ID=0
const val QQ_CACHE_ID=1
const val QQ_HEAD_ID=2
const val QQ_QZONE_ID=3
const val WECHAT_GARBAGE_ID=10//微信垃圾文件key
const val WECHAT_CACHE_ID=11//微信缓存表情key
const val WECHAT_FRIEND_ID=12//微信朋友圈缓存key
const val WECHAT_OTHER_ID=13//微信其他缓存key
val weAndQQPaths=HashSet<WeAndQQClean>().apply {
    add(WeAndQQClean(QQ_GARBAGE_ID, "qq", "垃圾文件", listOf(
            ("/storage/emulated/0/tencent/Midas")
            , ("${Constant.QQ_PATH_MOBILEQQ}/appicon")
            , ("${Constant.QQ_PATH_MOBILEQQ}/ar_model")
            , ("${Constant.QQ_PATH_MOBILEQQ}/qqconnect")
            , ("${Constant.QQ_PATH_MOBILEQQ}/.gift")
            , ("${Constant.QQ_PATH_MOBILEQQ}/pendant")
            ,("/storage/emulated/0/tencent/TMAssistantSDK/Download/com.tencent.mobileqq")//QQ-下载的应用安装包
            ,("/storage/emulated/0/tencent/QWallet")//qq  缓存图片
            ,("/storage/emulated/0/tencent/tassistant/mediaCache")//qq  缓存文件
            ,("/storage/emulated/0/Android/data/com.tencent.mobileqq/qzone/avatar")//qq  装扮缓存
    )))
    add(WeAndQQClean(QQ_CACHE_ID, "qq", "临时文件", listOf(
            ("${Constant.QQ_PATH_MOBILEQQ}/babyQIconRes")
            , ("${Constant.QQ_PATH_MOBILEQQ}/DoutuRes")
            , ("${Constant.QQ_PATH_MOBILEQQ}/portrait")
            , ("${Constant.QQ_PATH_MOBILEQQ}/status_ic")
            , ("${Constant.QQ_PATH_MOBILEQQ}/thumb")
            , ("${Constant.QQ_PATH_MOBILEQQ}/.apollo")
            , ("${Constant.QQ_PATH_MOBILEQQ}/.emojiSticker_v2.1")
    )))
    add(WeAndQQClean(QQ_HEAD_ID, "qq", "头像缓存", listOf<String>(
            ("${Constant.QQ_PATH_MOBILEQQ}/head")
    )))
    add(WeAndQQClean(QQ_QZONE_ID, "qq", "图片缓存", listOf(
            ("${Constant.QQ_PATH_MOBILEQQ}/emoji"),
            "${Constant.QQ_PATH_MOBILEQQ}/diskcache"
    )))
}

data class ApplicationGarbage(val packageName: String,val appName: String,val des:String="",val path:String)

val applicationGarbagePath=HashSet<ApplicationGarbage>().apply {
//    add(ApplicationGarbage("com.tencent.qqpimsecure", "腾讯应用管家", "监控日志", "/storage/emulated/0/Android/data/com.tencent.qqpimsecure"))
//    add(ApplicationGarbage("com.tencent.qqpimsecure", "腾讯应用管家", "下载的插件", "/storage/emulated/0/QQSecureDownload"))
//    add(ApplicationGarbage("com.tencent.qqpimsecure", "腾讯应用管家", "数据缓存", "/storage/emulated/0/QQSecureDownload/reminder"))
//    add(ApplicationGarbage("com.tencent.qqpimsecure", "腾讯应用管家", "缓存", "/storage/emulated/0/.tmfs"))
//
//    add(ApplicationGarbage("com.qti.qualcomm.deviceinfo", "软件商店", "缓存", "/storage/emulated/0/android/data/com.oppo.market/files/cache"))
//    add(ApplicationGarbage("com.qti.qualcomm.deviceinfo", "软件商店", "缓存", "/storage/emulated/0/ColorOS/Market/.dog"))
//    add(ApplicationGarbage("com.qti.qualcomm.deviceinfo", "软件商店", "缓存", "/storage/emulated/0/Android/data/com.oppo.market/files/cache/cache_gslb"))
//    add(ApplicationGarbage("com.qti.qualcomm.deviceinfo", "软件商店", "数据缓存", "/storage/emulated/0/ColorOS/Market/database"))
//
//    add(ApplicationGarbage("com.alibaba.android.rimet", "钉钉", "日志文件", "/storage/emulated/0/Android/data/com.alibaba.android.rimet/files/logs"))
//    add(ApplicationGarbage("com.alibaba.android.rimet", "钉钉", "临时文件", "/storage/emulated/0/Android/data/com.alibaba.android.rimet/cache/REQTTMPCACHE"))
//    add(ApplicationGarbage("com.alibaba.android.rimet","钉钉","日志文件","/storage/emulated/0/android/data/com.alibaba.android.rimet/files/logs"))
//    add(ApplicationGarbage("com.alibaba.android.rimet","钉钉","缓存文件","/storage/emulated/0/Android/data/com.alibaba.android.rimet/cache"))
//    add(ApplicationGarbage("com.alibaba.android.rimet","钉钉","缓存文件","/storage/emulated/0/Android/data/com.alibaba.android.rimet/cache/REQDEFCACHE"))
//    add(ApplicationGarbage("com.alibaba.android.rimet","钉钉","临时缓存","/storage/emulated/0/Android/data/com.alibaba.android.rimet/cache/REQTTMPCACHE"))
//    add(ApplicationGarbage("com.alibaba.android.rimet","钉钉","缓存文件","/storage/emulated/0/Android/data/com.alibaba.android.rimet/cache/data/user/0/com.alibaba.android.rimet/cache"))
//    add(ApplicationGarbage("com.alibaba.android.rimet","钉钉","缓存","/storage/emulated/0/android/data/com.alibaba.android.rimet/files/awcn_strategy"))
//
//    add(ApplicationGarbage("com.wuba.zhuanzhuan","转转","缓存文件","/storage/emulated/0/Android/data/com.wuba.zhuanzhuan/cache"))
//    add(ApplicationGarbage("com.wuba.zhuanzhuan","转转","图片缓存","/storage/emulated/0/Android/data/com.wuba.zhuanzhuan/cache/fresco_images"))
//    add(ApplicationGarbage("com.wuba.zhuanzhuan","转转","图片缓存","/storage/emulated/0/Android/data/com.wuba.zhuanzhuan/files/zzface"))
//
//    add(ApplicationGarbage("com.tencent.tim", "TIM", "缩略图缓存", "/storage/emulated/0/Tencent/TIMfile_recv/.thumbnails"))
//    add(ApplicationGarbage("com.tencent.tim", "TIM", "网页缓存", "/storage/emulated/0/tencent/Tim/qbiz/html5"))
//    add(ApplicationGarbage("com.tencent.tim", "TIM", "腾讯TSF日志", "/storage/emulated/0/tencent/msflogs"))
//    add(ApplicationGarbage("com.tencent.tim", "TIM", "图片缓存", "/storage/emulated/0/tencent/Tim/diskcache"))
//    add(ApplicationGarbage("com.tencent.tim", "TIM", "头像浏览缓存", "/storage/emulated/0/Tencent/Tim/portrait/HDAvatar"))
//    add(ApplicationGarbage("com.tencent.tim", "TIM", "登录日志", "/storage/emulated/0/tencent/wtlogin"))
//    add(ApplicationGarbage("com.tencent.tim", "TIM", "发送过的图片", "/storage/emulated/0/tencent/Tim/photo"))
//    add(ApplicationGarbage("com.tencent.tim", "TIM", "发送过的缩略图", "/storage/emulated/0/tencent/Tim/thumb"))
//    add(ApplicationGarbage("com.tencent.tim", "TIM", "信息缓存", "/storage/emulated/0/Android/data/com.tencent.tim/files/.info"))
//
//    add(ApplicationGarbage("com.tencent.tim", "TIM", "临时缓存", "/storage/emulated/0/tencent/Tim/WebViewCheck"))
//    add(ApplicationGarbage("com.tencent.tim", "TIM", "日志文件", "/storage/emulated/0/tencent/Tim/log"))
//    add(ApplicationGarbage("com.tencent.tim", "TIM", "日志文件", "/storage/emulated/0/tencent/Tim/Qqmail/log"))
//
//    add(ApplicationGarbage("com.coloros.gallery3d", "相册", "缓存", "/storage/emulated/0/android/data/com.coloros.gallery3d/cache"))
//
//    add(ApplicationGarbage("com.bbk.appstore", "vivo应用商店", "临时文件", "/storage/emulated/0/.BBKAppStore/cache"))
//
//    add(ApplicationGarbage("com.tencent.android.qqdownloader", "应用宝", "缓存", "/storage/emulated/0/tencent/tassistant/mediacache"))
//    add(ApplicationGarbage("com.tencent.android.qqdownloader", "应用宝", "数据文件", "/storage/emulated/0/tencent/tassistant/mediacache"))
//    add(ApplicationGarbage("com.tencent.android.qqdownloader", "应用宝", "下载的安装包", "/storage/emulated/0/tencent/tassistant/apk"))
//    add(ApplicationGarbage("com.tencent.android.qqdownloader", "应用宝", "媒体缓存", "/storage/emulated/0/tencent/tassistant/mediaCache"))
//    add(ApplicationGarbage("com.tencent.android.qqdownloader", "应用宝", "缓存", "/storage/emulated/0/tencent/tassistant/korok"))
//    add(ApplicationGarbage("com.tencent.android.qqdownloader", "应用宝", "日志文件", "/storage/emulated/0/tencent/tassistant/log"))
//    add(ApplicationGarbage("com.tencent.android.qqdownloader", "应用宝", "资源文件", "/storage/emulated/0/tencent/tassistant/file"))
//    add(ApplicationGarbage("com.tencent.android.qqdownloader", "应用宝", "缓存", "/storage/emulated/0/Android/data/com.tencent.android.qqdownloader/files"))
//    add(ApplicationGarbage("com.tencent.android.qqdownloader", "应用宝", "日志文件", "/storage/emulated/0/android/data/com.tencent.android.qqdownloader/files/MiPushLog"))
//
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","数据缓存","/storage/emulated/0/Android/data/com.taobao.taobao/files"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存","/storage/emulated/0/android/data/com.taobao.taobao/files/AVFSCache"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存文件","/storage/emulated/0/Android/data/com.taobao.taobao/files/AVFSCache/phximgs_top1"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存文件","/storage/emulated/0/Android/data/com.taobao.taobao/files/AVFSCache/phximgs_top3"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存","/storage/emulated/0/android/data/com.taobao.taobao/cache"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","广告图片缓存","/storage/emulated/0/android/data/com.taobao.taobao/cache"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存文件","/storage/emulated/0/Android/data/com.taobao.taobao/files/AVFSCache/networksdk.httpcache/files"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存文件","/storage/emulated/0/Android/data/com.taobao.taobao/files/AVFSCache/networksdk.httpcache"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","日志文件","/storage/emulated/0/android/data/com.taobao.taobao/files/logs"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","临时缓存","/storage/emulated/0/Android/data/com.taobao.taobao/files/acds"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存","/storage/emulated/0/android/data/com.taobao.taobao/files/amapcn/data_v6/mapcache"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存文件","/storage/emulated/0/Android/data/com.taobao.taobao/files/AVFSCache/MyTaobao"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存文件","/storage/emulated/0/Android/data/com.taobao.taobao/files/AVFSCache/TBStorageAdapterImpl"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存文件","/storage/emulated/0/Android/data/com.taobao.taobao/files/AVFSCache/phximgs_top5"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存文件","/storage/emulated/0/Android/data/com.taobao.taobao/files/AVFSCache/bootimage"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存文件","/storage/emulated/0/Android/data/com.taobao.taobao/files/AVFSCache/mtop_apicache_carts2/files"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存文件","/storage/emulated/0/Android/data/com.taobao.taobao/files/AVFSCache/mtop_apicache_carts2"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存","/storage/emulated/0/Android/data/com.taobao.taobao/files/persistent_store"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","缓存文件","/storage/emulated/0/Android/data/com.taobao.taobao/files/AVFSCache/favorite_sdk"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","阿里广告","/storage/emulated/0/.UTSystemConfig"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","阿里网页广告","/storage/emulated/0/.DataStorage"))
//    add(ApplicationGarbage("com.taobao.taobao","淘宝","阿里网页广告","/storage/emulated/0/backups/.SystemConfig"))
//
//    add(ApplicationGarbage("com.xiaomi.market","小米应用商店","下载的安装包","/storage/emulated/0/MiMarket"))
//    add(ApplicationGarbage("com.xiaomi.market","小米应用商店","日志缓存","/storage/emulated/0/MIUI/debug_log"))
//    add(ApplicationGarbage("com.xiaomi.market","小米应用商店","日志文件","/storage/emulated/0/Android/data/com.xiaomi.market/files/MiPushLog"))
//
//    add(ApplicationGarbage("com.master.hypnosis", "深睡大师", "缓存", "/storage/emulated/0/android/data/com.master.hypnosis/files/MusicCache"))
//    add(ApplicationGarbage("cn.missfresh.application","每日优鲜","缓存","/storage/emulated/0/android/data/cn.missfresh.application/files/Pictures/MissFresh/cache"))
//    add(ApplicationGarbage("cn.missfresh.application","每日优鲜","日志文件","/storage/emulated/0/android/data/cn.missfresh.application/files/tbslog"))
//    add(ApplicationGarbage("cn.missfresh.application","每日优鲜","日志文件","/storage/emulated/0/android/data/cn.missfresh.application/files/tencent/tbs_live_log"))
//    add(ApplicationGarbage("cn.missfresh.application","每日优鲜","日志文件","/storage/emulated/0/android/data/cn.missfresh.application/files/MiPushLog"))
//    add(ApplicationGarbage("cn.missfresh.application","每日优鲜","缓存","/storage/emulated/0/Android/data/cn.missfresh.application/cache"))
//    add(ApplicationGarbage("com.pb","招商银行","缓存","/storage/emulated/0/cmb/data"))
//    add(ApplicationGarbage("com.pb","招商银行","日志文件","/storage/emulated/0/cmb.pb/log"))
//    add(ApplicationGarbage("com.pb","招商银行","插件缓存","/storage/emulated/0/android/data/cmb.pb/files/Movies/baidu"))
//    add(ApplicationGarbage("com.pb","招商银行","日志文件","/storage/emulated/0/android/data/cmb.pb/files/MiPushLog"))
//
//    add(ApplicationGarbage("com.zhihu.android","知乎","安装包缓存","/storage/emulated/0/android/data/com.zhihu.android/cache/apk"))
//    add(ApplicationGarbage("com.zhihu.android","知乎","图片和网页缓存","/storage/emulated/0/android/data/com.zhihu.android/cache"))
//    add(ApplicationGarbage("com.zhihu.android","知乎","缓存","/storage/emulated/0/Android/data/com.zhihu.android/files"))
//    add(ApplicationGarbage("com.zhihu.android","知乎","缓存","/storage/emulated/0/android/data/com.zhihu.android/files/Movies/VideoCache"))
//
//    add(ApplicationGarbage("com.sina.weibo", "微博", "缓存", "/storage/emulated/0/sina/weibo/cache"))
//    add(ApplicationGarbage("com.sina.weibo", "微博", "广告缓存", "/storage/emulated/0/sina/weibo/.wbadcache"))
//    add(ApplicationGarbage("com.sina.weibo", "微博", "微博广告", "/storage/emulated/0/sina/weibo/.weibo_ad_universal_cache/"))
//    add(ApplicationGarbage("com.sina.weibo", "微博", "微博视频缓存", "/storage/emulated/0/sina/weibo/.weibo_video_cache_new/"))
//    add(ApplicationGarbage("com.sina.weibo", "微博","浏览缓存","/storage/emulated/0/sina/weibo/storage/video_play_cache"))
//    add(ApplicationGarbage("com.sina.weibo", "微博","页面缓存","/storage/emulated/0/sina/weibo/storage/newsfeed"))
//    add(ApplicationGarbage("com.sina.weibo", "微博","图片缓存","/storage/emulated/0/sina/weibo/storage/photoalbum_pic/.prenew"))
//    add(ApplicationGarbage("com.sina.weibo", "微博","预览图片","/storage/emulated/0/sina/weibo/storage/photoalbum_pic/.weibo_pic_new"))
//    add(ApplicationGarbage("com.sina.weibo", "微博","头像图片","/storage/emulated/0/sina/weibo/storage/photoalbum_pic/.portraitnew"))
//    add(ApplicationGarbage("com.sina.weibo", "微博","日志缓存","/storage/emulated/0/sina/weibo/weibolog"))
//    add(ApplicationGarbage("com.sina.weibo", "微博","日志文件","/storage/emulated/0/Android/data/.log/com.sina.weibo"))
//    add(ApplicationGarbage("com.sina.weibo", "微博","软件信息缓存","/storage/emulated/0/sina/weibo/.appinfos"))
//    add(ApplicationGarbage("com.sina.weibo", "微博","消息缓存","/storage/emulated/0/sina/weibo/msgcatch"))
//    add(ApplicationGarbage("com.sina.weibo", "微博","监控日志","/storage/emulated/0/sina/weibo/.log"))
//    add(ApplicationGarbage("com.sina.weibo", "微博","微友列表","/storage/emulated/0/sina/weibo/weiyoumenulist"))
//    add(ApplicationGarbage("com.sina.weibo", "微博","图片缓存","/storage/emulated/0/sina/weibo/storage/pagecard_no_auto_clear"))
//    add(ApplicationGarbage("com.sina.weibo", "微博","缓存","/storage/emulated/0/sina/weibo/traffic"))
//
//    add(ApplicationGarbage("com.eg.android.AlipayGphone", "支付宝", "聊天语音及大图", "/storage/emulated/0/alipay/multimedia"))
//    add(ApplicationGarbage("com.eg.android.AlipayGphone","支付宝","缓存","/storage/emulated/0/Amap/data/mapcache"))
//    add(ApplicationGarbage("com.eg.android.AlipayGphone","支付宝","压缩包缓存","/storage/emulated/0/Android/data/com.eg.android.AlipayGphone/files/emotion/magic"))
//    add(ApplicationGarbage("com.eg.android.AlipayGphone","支付宝","缓存文件","/storage/emulated/0/Android/data/com.eg.android.AlipayGphone/cache"))
//    add(ApplicationGarbage("com.eg.android.AlipayGphone","支付宝","缓存","/storage/emulated/0/android/data/com.eg.android.AlipayGphone/cache"))
//    add(ApplicationGarbage("com.eg.android.AlipayGphone","支付宝","广告文件","/storage/emulated/0/android/data/com.eg.android.AlipayGphone/files/multimedia/516581a2ce920b9bc16a9c5004a023b8/ad"))
//    add(ApplicationGarbage("com.eg.android.AlipayGphone","支付宝","数据缓存","/storage/emulated/0/alipay/com.eg.android.AlipayGphone/lottie"))
//    add(ApplicationGarbage("com.eg.android.AlipayGphone","支付宝","缓存","/storage/emulated/0/.com.taobao.dp"))
//
//    add(ApplicationGarbage("com.sankuai.meituan.takeoutnew","美团外卖","升级缓存","/storage/emulated/0/Android/data/com.sankuai.meituan.takeoutnew/files/upgrade"))
//    add(ApplicationGarbage("com.sankuai.meituan.takeoutnew","美团外卖","缓存","/storage/emulated/0/android/data/com.sankuai.meituan.takeoutnew/cache"))
//    add(ApplicationGarbage("com.sankuai.meituan.takeoutnew","美团外卖","缓存","/storage/emulated/0/Android/data/com.sankuai.meituan.takeoutnew/cache"))
//    add(ApplicationGarbage("com.sankuai.meituan.takeoutnew","美团外卖","缓存","/storage/emulated/0/Android/data/com.sankuai.meituan.takeoutnew/files/elephent/im"))
//    add(ApplicationGarbage("com.sankuai.meituan.takeoutnew","美团外卖","更新缓存","/storage/emulated/0/android/data/com.sankuai.meituan.takeoutnew/files/cips/common/mrn_update"))
//
//    add(ApplicationGarbage("com.autonavi.minimap","高德地图","地图缓存","/storage/emulated/0/Amap/GridMapV3"))
//    add(ApplicationGarbage("com.autonavi.minimap","高德地图","缓存文件","/storage/emulated/0/Android/data/com.autonavi.minimap/files/autonavi/httpcache"))
//    add(ApplicationGarbage("com.autonavi.minimap","高德地图","缓存垃圾","/storage/emulated/0/autonavi/alc"))
//    add(ApplicationGarbage("com.autonavi.minimap","高德地图","闪屏图片","/storage/emulated/0/autonavi/afpSplash"))
//    add(ApplicationGarbage("com.autonavi.minimap","高德地图","动态图标","/storage/emulated/0/autonavi/cache/gif"))
//    add(ApplicationGarbage("com.autonavi.minimap","高德地图","定位缓存","/storage/emulated/0/Android/data/com.autonavi.minimap/files/amaplocation"))
//    add(ApplicationGarbage("com.autonavi.minimap","高德地图","缓存","/storage/emulated/0/android/data/com.autonavi.minimap/files/autonavi/LocalCache"))
//    add(ApplicationGarbage("com.autonavi.minimap","高德地图","缓存","/storage/emulated/0/autonavi/qmt"))
//    add(ApplicationGarbage("com.autonavi.minimap","高德地图","缓存","/storage/emulated/0/autonavi/data/crosscache"))
//    add(ApplicationGarbage("com.autonavi.minimap","高德地图","定位缓存","/storage/emulated/0/autonavi/location"))
//
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","图片缓存","/storage/emulated/0/kugou/.glide"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","数据缓存","/storage/emulated/0/kugou/.backupsv905"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","数据缓存","/storage/emulated/0/kugou/.backupsv901/databases"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","日志文件","/storage/emulated/0/kugou/log"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","飞屏图片","/storage/emulated/0/kugou/.splash"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","歌手头像","/storage/emulated/0/kugou/.singerres"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","日志文件","/storage/emulated/0/kugou/v8skin/log"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","图片缓存","/storage/emulated/0/kugou/.images"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","广告缓存","/storage/emulated/0/kugou/.ads"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","直播缓存","/storage/emulated/0/kugou/fanxing/.live"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","缓存","/storage/emulated/0/android/data/com.kugou.android/cache"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","频道缓存","/storage/emulated/0/kugou/.httpclient"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","歌词缓存","/storage/emulated/0/kugou/lyrics/hitlayer"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","配置文件","/storage/emulated/0/kugou/config"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","聊天拍摄图片","/storage/emulated/0/kugou/msgchat"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","电台缓存","/storage/emulated/0/kugou/fm"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","图标缓存","/storage/emulated/0/kugou/.userinfo/friendicon"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","聊天拍摄照片","/storage/emulated/0/kugou/msgchat/camera"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","样本记录","/storage/emulated/0/kugou/.recordsample"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","临时歌词缓存","/storage/emulated/0/kugou/temp_lyrics"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","缓存","/storage/emulated/0/kugou/down_c/radio"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","图标缓存","/storage/emulated/0/kugou/.userinfo/friendicon"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","反馈日志","/storage/emulated/0/kugou/.feedback"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","下载的安装包","/storage/emulated/0/aliunion_apk"))
//    add(ApplicationGarbage("com.kugou.android","酷狗音乐","列表缓存","/storage/emulated/0/kugou/list"))
//
//    add(ApplicationGarbage("com.jingdong.app.mall","京东","缓存","/storage/emulated/0/Android/data/com.jingdong.app.mall/files"))
//    add(ApplicationGarbage("com.jingdong.app.mall","京东","图片缓存","/storage/emulated/0/Android/data/com.jingdong.app.mall/files/image"))
//    add(ApplicationGarbage("com.jingdong.app.mall","京东","日志文件","/storage/emulated/0/Android/data/com.jingdong.app.mall/files/tbslog"))
//    add(ApplicationGarbage("com.jingdong.app.mall","京东","日志文件","/storage/emulated/0/android/data/com.jingdong.app.mall/files/MiPushLog"))
//    add(ApplicationGarbage("com.jingdong.app.mall","京东","缓存","/storage/emulated/0/Android/data/com.jingdong.app.mall/cache"))
//
//    add(ApplicationGarbage("com.baidu.mbaby","宝宝知道","图片缓存","/storage/emulated/0/android/data/com.baidu.mbaby/files/image"))
//    add(ApplicationGarbage("com.baidu.mbaby","宝宝知道","缓存","/storage/emulated/0/android/data/com.baidu.mbaby/files/entity"))
//    add(ApplicationGarbage("com.baidu.mbaby","宝宝知道","广告文件","/storage/emulated/0/android/data/com.baidu.mbaby/files/image/ad"))
//
//    add(ApplicationGarbage("com.chinamworld.main","中国建设银行","缓存","/storage/emulated/0/Ccb/Pic"))
//    add(ApplicationGarbage("com.chinamworld.main","中国建设银行","临时缓存","/storage/emulated/0/Android/data/com.chinamworld.main/files/baidu/tempdata"))
//    add(ApplicationGarbage("com.chinamworld.main","中国建设银行","插件缓存","/storage/emulated/0/android/data/com.chinamworld.main/files/baidu"))
//    add(ApplicationGarbage("com.chinamworld.main","中国建设银行","缓存","/storage/emulated/0/Android/data/com.chinamworld.main/files"))
//
//    add(ApplicationGarbage("com.hpbr.bosszhipin","Boss直聘","日志文件","/storage/emulated/0/Android/data/com.hpbr.bosszhipin/files/tbslog"))
//    add(ApplicationGarbage("com.hpbr.bosszhipin","Boss直聘","日志文件","/storage/emulated/0/android/data/com.hpbr.bosszhipin/files/tencent/tbs_live_log"))
//    add(ApplicationGarbage("com.MobileTicket","铁路12306","闪屏图片","/storage/emulated/0/Android/data/com.MobileTicket/cache/bontai/splash/img"))
//
//    add(ApplicationGarbage("com.MobileTicket","铁路12306","闪屏图片","/storage/emulated/0/Android/data/com.MobileTicket/cache/bontai/splash/img"))
//    add(ApplicationGarbage("com.ss.android.article.news","今日头条","缓存","/storage/emulated/0/android/data/com.ss.android.article.news/cache"))
//    add(ApplicationGarbage("com.ss.android.article.news","今日头条","更新的安装包","/storage/emulated/0/android/data/com.ss.android.article.news/files"))
//    add(ApplicationGarbage("com.ss.android.article.news","今日头条","日志文件","/storage/emulated/0/android/data/com.ss.android.article.news/files/logs"))
//    add(ApplicationGarbage("com.ss.android.article.news","今日头条","日志文件","/storage/emulated/0/android/data/com.ss.android.article.news/files/MiPushLog"))
//    add(ApplicationGarbage("com.ss.android.article.news","今日头条","临时文件","/storage/emulated/0/android/data/com.ss.android.article.news/weboffline/tt_user_auth_web/template"))
//    add(ApplicationGarbage("com.ss.android.article.news","今日头条","临时文件","/storage/emulated/0/android/data/com.ss.android.article.news/weboffline/search/template"))
//    add(ApplicationGarbage("com.ss.android.article.news","今日头条","缓存","/storage/emulated/0/android/data/com.snssdk.api"))
//
//    add(ApplicationGarbage("cn.goapk.market","安智市场","下载的安装包","/storage/emulated/0/anzhi/download"))
//    add(ApplicationGarbage("cn.goapk.market","安智市场","广告图片","/storage/emulated/0/anzhi/.screen"))
//    add(ApplicationGarbage("cn.goapk.market","安智市场","图标缓存","/storage/emulated/0/anzhi/.manage"))
//    add(ApplicationGarbage("cn.goapk.market","安智市场","监控日志","/storage/emulated/0/anzhi/.log"))
//
//    add(ApplicationGarbage("com.tencent.qqmusic","QQ音乐","动图图标","/storage/emulated/0/qqmusic/gift_anim_zip"))
//    add(ApplicationGarbage("com.tencent.qqmusic","QQ音乐","缓存","/storage/emulated/0/android/data/com.tencent.qqmusic/cache"))
//    add(ApplicationGarbage("com.tencent.qqmusic","QQ音乐","识别的音乐","/storage/emulated/0/qqmusic/recognize"))
//    add(ApplicationGarbage("com.tencent.qqmusic","QQ音乐","网页缓存","/storage/emulated/0/qqmusic/network"))
//    add(ApplicationGarbage("com.tencent.qqmusic","QQ音乐","闪屏图片缓存","/storage/emulated/0/qqmusic/splash"))
//    add(ApplicationGarbage("com.tencent.qqmusic","QQ音乐","日志文件","/storage/emulated/0/android/data/com.tencent.qqmusic/files/tbslog"))
//    add(ApplicationGarbage("com.tencent.qqmusic","QQ音乐","缓存","/storage/emulated/0/qqmusic/thirdapicaches"))
//    add(ApplicationGarbage("com.tencent.qqmusic","QQ音乐","配置文件","/storage/emulated/0/qqmusic/config"))
//    add(ApplicationGarbage("com.tencent.qqmusic","QQ音乐","用户信息缓存","/storage/emulated/0/android/data/com.tencent.qqmusic/files/tencent"))
//
//    add(ApplicationGarbage("com.oppo.market","OPPO软件商店","下载的安装包","/storage/emulated/0/ColorOS/Market/app"))
//
//    add(ApplicationGarbage("com.qihoo.appstore","360手机助手","360日志","/storage/emulated/0/360log"))
//    add(ApplicationGarbage("com.qihoo.appstore","360手机助手","缓存","/storage/emulated/0/android/data/com.qihoo.appstore/cache"))
//    add(ApplicationGarbage("com.qihoo.appstore","360手机助手","缓存文件","/storage/emulated/0/android/data/com.qihoo.appstore/cache/image/image_cache/v2.ols100.1"))
//    add(ApplicationGarbage("com.qihoo.appstore","360手机助手","图标缓存","/storage/emulated/0/360/newssdk/com.qihoo.appstore/.c/img"))
//    add(ApplicationGarbage("com.qihoo.appstore","360手机助手","文件缓存","/storage/emulated/0/android/data/com.qihoo.appstore/files"))
//
//    add(ApplicationGarbage("com.cubic.autohome","汽车之家","图片缓存","/storage/emulated/0/autohomemain/img"))
//
//    add(ApplicationGarbage("com.baidu.BaiduMap","百度地图","导航文件缓存","/storage/emulated/0/baidumap/bnav/navi/pub"))
//    add(ApplicationGarbage("com.baidu.BaiduMap","百度地图","导航产生的缓存","/storage/emulated/0/baidumap/bnav/cache"))
//    add(ApplicationGarbage("com.baidu.BaiduMap","百度地图","导航日志","/storage/emulated/0/baidumap/bnav/navienginelog"))
//
//    add(ApplicationGarbage("com.tencent.androidqqmail","QQ邮箱","图片缓存","/storage/emulated/0/tencent/qqmail/imagecache"))
//    add(ApplicationGarbage("com.tencent.androidqqmail","QQ邮箱","网页缓存","/storage/emulated/0/tencent/qqmail/webviewext"))
//
//    add(ApplicationGarbage("com.youku.phone","优酷视频","广告文件","/storage/emulated/0/android/data/com.youku.phone/files/ad"))
//    add(ApplicationGarbage("com.youku.phone","优酷视频","弹幕缓存","/storage/emulated/0/android/data/com.youku.phone/files/danmaku_online"))
//
//    add(ApplicationGarbage("com.youdao.note","有道云笔记","用户头像","/storage/emulated/0/.youdaonote/userinfo"))
//
//    add(ApplicationGarbage("com.ucmobile","UC浏览器","压缩包缓存","/storage/emulated/0/backucup/com.ucmobile"))
//    add(ApplicationGarbage("com.ucmobile","UC浏览器","用户头像","/storage/emulated/0/android/data/com.ucmobile/cache/uil-images"))
//
//    add(ApplicationGarbage("cn.wps.moffice_eng","WPS Office","缓存文件","/storage/emulated/0/android/data/cn.wps.moffice_eng/.cache/kingsoftoffice"))
//    add(ApplicationGarbage("cn.wps.moffice_eng","WPS Office","下载的字体","/storage/emulated/0/android/data/cn.wps.moffice_eng/.cache/kingsoftoffice"))
//    add(ApplicationGarbage("cn.wps.moffice_eng","WPS Office","临时缓存","/storage/emulated/0/android/data/cn.wps.moffice_eng/.cache/kingsoftoffice/.temp"))
//    add(ApplicationGarbage("com.qzone","QQ空间","图片缓存","/storage/emulated/0/Android/data/com.qzone/cache/imageV2"))
//    add(ApplicationGarbage("com.qzone","QQ空间","缓存","/storage/emulated/0/Android/data/com.qzone/cache/facade"))
//    add(ApplicationGarbage("com.qzone","QQ空间","缓存","/storage/emulated/0/qmt"))
//    add(ApplicationGarbage("com.changba","唱吧","压缩包缓存","/storage/emulated/0/.ktv/pitch_correction"))
//    add(ApplicationGarbage("com.changba","唱吧","图片缓存","/storage/emulated/0/.ktv/images"))
//    add(ApplicationGarbage("com.changba","唱吧","播放缓存","/storage/emulated/0/.ktv/song/_play_zrce"))
//    add(ApplicationGarbage("com.sohu.inputmethod.sogou","搜狗输入法","表情图片","/storage/emulated/0/sogou/.hotexp"))
//    add(ApplicationGarbage("com.sohu.inputmethod.sogou","搜狗输入法","日志文件","/storage/emulated/0/sogou/corelog"))
//    add(ApplicationGarbage("com.sohu.inputmethod.sogou","搜狗输入法","皮肤预览图","/storage/emulated/0/sogou/sga/.theme_net"))
//    add(ApplicationGarbage("com.xiangha","香哈菜谱","图片缓存","/storage/emulated/0/xiangha/cache"))
//    add(ApplicationGarbage("com.xingjiabi.shengsheng","他趣","临时缓存","/storage/emulated/0/system/tmp/local"))
//    add(ApplicationGarbage("com.baidu.searchbox","手机百度","广告垃圾","/storage/emulated/0/.BD_SAPI_CACHE"))
//    add(ApplicationGarbage("com.baidu.searchbox","手机百度","百度临时文件","/storage/emulated/0/baidu/tempdata"))
//    add(ApplicationGarbage("com.baidu.BaiduMap","百度地图","缓存","/storage/emulated/0/autonavi/support"))
//    add(ApplicationGarbage("com.tencent.mtt","QQ浏览器","日志文件","/storage/emulated/0/tencent/com.tencent.mtt"))
//    add(ApplicationGarbage("com.tencent.mtt","QQ浏览器","数据缓存","/storage/emulated/0/tencent/beacon"))
//    add(ApplicationGarbage("com.ss.android.article.news","今日头条","缓存","/storage/emulated/0/Android/data/com.snssdk.api/cache"))
//    add(ApplicationGarbage("com.tencent.now","腾讯NOW直播","数据文件","/storage/emulated/0/tencent/now/file"))
//    add(ApplicationGarbage("com.qihoo360.mobilesafe","360手机卫士","下载的安装包","/storage/emulated/0/360download"))
//    add(ApplicationGarbage("com.jingyao.easybike","哈啰单车","数据","/storage/emulated/0/easybike"))
//    add(ApplicationGarbage("com.wandoujia.phoenix2","豌豆荚","下载的安装包","/storage/emulated/0/easybike"))
//    add(ApplicationGarbage("com.wandoujia.phoenix2","豌豆荚","日志文件","/storage/emulated/0/android/data/com.wandoujia.phoenix2/files/tnetlogs"))
//    add(ApplicationGarbage("com.wandoujia.phoenix2","豌豆荚","缓存","/storage/emulated/0/android/data/com.wandoujia.phoenix2/files/awcn_strategy"))

//    add(ApplicationGarbage("com.tencent.mm", "微信", "朋友圈广告", "/storage/emulated/0/tencent/MicroMsg/sns_ad_landingpages"))
//    add(ApplicationGarbage("com.tencent.mm", "微信", "缓存文件", "/storage/emulated/0/tencent/MicroMsg/diskcache"))
//    add(ApplicationGarbage("com.tencent.mm", "微信", "临时文件", "/storage/emulated/0/tencent/MicroMsg/CDNTemp"))
//    add(ApplicationGarbage("com.tencent.mm", "微信", "缓存图片", "/storage/emulated/0/tencent/MicroMsg/wallet"))
//    add(ApplicationGarbage("com.tencent.mm","微信","日志文件","/storage/emulated/0/tencent/MicroMsg/xlog"))
//    add(ApplicationGarbage("com.tencent.mm","微信","日志文件","/storage/emulated/0/Tencent/MicroMsg/wxanewfiles"))
//    add(ApplicationGarbage("com.tencent.mm","微信","小程序图标缓存","/storage/emulated/0/tencent/MicroMsg/wxacache"))
//    add(ApplicationGarbage("com.tencent.mm","微信","缓存","/storage/emulated/0/android/data/com.tencent.mm/cache"))
//    add(ApplicationGarbage("com.tencent.mm","微信","小程序图片缓存","/storage/emulated/0/tencent/MicroMsg/Download/appbrand"))
//    add(ApplicationGarbage("com.tencent.mm","微信","检查更新缓存","/storage/emulated/0/Tencent/MicroMsg/CheckResUpdate"))
//    add(ApplicationGarbage("com.tencent.mm","微信","缓存","/storage/emulated/0/Android/data/com.tencent.mm/files"))
//    add(ApplicationGarbage("com.tencent.mm","微信","日志文件","/storage/emulated/0/android/data/com.tencent.mm/files/tbslog"))
//    add(ApplicationGarbage("com.tencent.mm","微信","临时缓存","/storage/emulated/0/tencent/MicroMsg/.tmp"))
//    add(ApplicationGarbage("com.tencent.mm","微信","图标缓存","/storage/emulated/0/Android/data/com.tencent.mm/MicroMsg/wallet"))
//    add(ApplicationGarbage("com.tencent.mm","微信","钱包图标","/storage/emulated/0/tencent/MicroMsg/wallet_images"))
//    add(ApplicationGarbage("com.tencent.mm","微信","游戏下载缓存","/storage/emulated/0/tencent/MicroMsg/game"))
//    add(ApplicationGarbage("com.tencent.mm","微信","崩溃日志","/storage/emulated/0/tencent/MicroMsg/crash"))
//    add(ApplicationGarbage("com.tencent.mm","微信","星标用户图标","/storage/emulated/0/tencent/MicroMsg/vusericon"))
//    add(ApplicationGarbage("com.tencent.mm","微信","日志文件","/storage/emulated/0/tencent/MicroMsg/handler"))
//    add(ApplicationGarbage("com.tencent.mm","微信","跟踪文件","/storage/emulated/0/tencent/MicroMsg/sqltrace"))
//    add(ApplicationGarbage("com.tencent.mm","微信","缓存","/storage/emulated/0/tencent/MicroMsg/cache"))
//    add(ApplicationGarbage("com.tencent.mm","微信","错误日志","/storage/emulated/0/Tencent/MicroMsg/FailMsgFileCache"))
//    add(ApplicationGarbage("com.tencent.mm","微信","版本信息","/storage/emulated/0/tencent/MicroMsg/recovery"))

//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","缓存","/storage/emulated/0/Android/data/com.tencent.mobileqq/cache"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","日迹短视频缩略图","/storage/emulated/0/Tencent/MobileQQ/shortvideo/thumbs"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","图片缩略图","/storage/emulated/0/tencent/Qqfile_recv/.thumbnails"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","日志文件","/storage/emulated/0/android/data/com.tencent.mobileqq/files/Tencent/tbs_live_log"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","日志文件","/storage/emulated/0/android/data/com.tencent.mobileqq/files/tbslog"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","缩略图缓存","/storage/emulated/0/Tencent/QQfile_recv/.trooptmp"))
//    add(ApplicationGarbage("com.tencent.mobileqq", "QQ", "装扮图片", "/storage/emulated/0/Android/data/com.tencent.mobileqq/qzone/avatar"))
//    add(ApplicationGarbage("com.tencent.mobileqq", "QQ", "下载的应用安装包", "/storage/emulated/0/tencent/TMAssistantSDK/Download/com.tencent.mobileqq"))
//    add(ApplicationGarbage("com.tencent.mobileqq", "QQ", "缓存图片", "/storage/emulated/0/tencent/QWallet"))
//    add(ApplicationGarbage("com.tencent.mobileqq", "QQ", "缓存文件", "/storage/emulated/0/tencent/tassistant/mediaCache"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","图片缓存","/storage/emulated/0/Android/data/com.qzone/cache"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","腾讯地图缓存数据","/storage/emulated/0/tencentmapsdk"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","视频缓存","/storage/emulated/0/Android/data/com.tencent.mobileqq/qzone/video_cache"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","装扮图片","/storage/emulated/0/Tencent/MobileQQ/.apollo"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","缓存","/storage/emulated/0/tencent/blob/mqq"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","图片缓存","/storage/emulated/0/Tencent/tassistant/pic"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","AR模型缓存","/storage/emulated/0/Tencent/MobileQQ/ar_model"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","登录日志","/storage/emulated/0/tencent/wtlogin/com.tencent.mobileqq"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","下载的应用安装包","/storage/emulated/0/tencent/TMAssistantSDK/Download/com.tencent.mobileqq"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","图标图片","/storage/emulated/0/Android/data/com.tencent.mobileqq/qzone/surprise"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","短视频缓存","/storage/emulated/0/Android/data/com.tencent.mobileqq/qq/video/Source"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","好友头像","/storage/emulated/0/Tencent/MobileQQ/head/_SSOhd"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","信息缓存","/storage/emulated/0/Android/data/com.tencent.mobileqq/files/.info"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","无效的数据缓存","/storage/emulated/0/libs"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","语音通话缓存","/storage/emulated/0/tencent/audio"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","可清理腾讯文件","/storage/emulated/0/tencent/mta"))
//    add(ApplicationGarbage("com.tencent.mobileqq","QQ","缓存","/storage/emulated/0/com.tencent.mobileqq"))
//    add(ApplicationGarbage("com.sohu.inputmethod.sogou.xiaomi","搜狗拼音输入法小米版","缓存","/storage/emulated/0/android/data/com.sohu.inputmethod.sogou.xiaomi/cache"))
//    add(ApplicationGarbage("com.tencent.eim","企业QQ","临时缓存","/storage/emulated/0/Tencent/QQfile_recv/.tmp"))


    add(ApplicationGarbage("com.tencent.mobileqq", "QQ", "动画及缩略图", "${Constant.QQ_PATH_MOBILEQQ}/pddata/app/offline"))
    add(ApplicationGarbage("com.tencent.mobileqq", "QQ", "gif缓存", "${Constant.QQ_PATH_MOBILEQQ}/pe/res/"))
    add(ApplicationGarbage("com.tencent.mobileqq", "QQ", "装扮图片", "${Constant.QQ_PATH_MOBILEQQ}/.apollo"))
    add(ApplicationGarbage("com.tencent.mobileqq", "QQ", "广告文件", "${Constant.QQ_PATH_MOBILEQQ}/.apollo"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","用户头像（七天前）","${Constant.QQ_PATH_MOBILEQQ}/head/_hd"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","缩略图缓存","${Constant.QQ_PATH_MOBILEQQ}/thumb"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","空间缓存","${Constant.QQ_PATH_MOBILEQQ}/webso/offline"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","头像挂件","${Constant.QQ_PATH_MOBILEQQ}/.pendant"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","标签图片缓存","${Constant.QQ_PATH_MOBILEQQ}/pe/res"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","钱包缓存","${Constant.QQ_PATH_MOBILEQQ}/QWallet/.tmp"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","新年祝福缓存","${Constant.QQ_PATH_MOBILEQQ}/bless"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","相册缓存","${Constant.QQ_PATH_MOBILEQQ}/portrait"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","图标缓存","${Constant.QQ_PATH_MOBILEQQ}/appicon"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","陌生人头像缓存","${Constant.QQ_PATH_MOBILEQQ}/head/_stranger"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","AR特征缓存","${Constant.QQ_PATH_MOBILEQQ}/ar_feature"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","轻游戏引导图","${Constant.QQ_PATH_MOBILEQQ}/.apollo/image_cache"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","图标压缩包","${Constant.QQ_PATH_MOBILEQQ}/babyqiconres"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","个性签名图标（三天前）","${Constant.QQ_PATH_MOBILEQQ}/status_ic"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","访问服务器地址缓存","${Constant.QQ_PATH_MOBILEQQ}/data"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","联系人缓存","${Constant.QQ_PATH_MOBILEQQ}/qqconnect"))
    add(ApplicationGarbage("com.tencent.eim","企业QQ","消息图片缓存","${Constant.QQ_PATH_MOBILEQQ}/diskcache"))
    add(ApplicationGarbage("com.tencent.eim","企业QQ","下载的安装包","${Constant.QQ_PATH_MOBILEQQ}/arkapp"))
    add(ApplicationGarbage("com.tencent.eim","企业QQ","贴图商店缓存","${Constant.QQ_PATH_MOBILEQQ}/emoji"))
    add(ApplicationGarbage("com.tencent.eim","企业QQ","图标缓存","${Constant.QQ_PATH_MOBILEQQ}/avatarpendanticons"))
    add(ApplicationGarbage("com.tencent.eim","企业QQ","日志","${Constant.QQ_PATH_MOBILEQQ}/log"))
    SQLiteDbManager.getAppGarbageFile().let {
        this.addAll(it)
    }
    //20200617
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","图片缓存","${Constant.QQ_PATH_MOBILEQQ}/chatpic"))
    Constant.weChatUserPath_Data_Cache.forEach {
        add(ApplicationGarbage("com.tencent.mm","微信","朋友圈缓存", "$it/sns"))
        add(ApplicationGarbage("com.tencent.mm","微信","缓存", "$it/bizimg"))
        add(ApplicationGarbage("com.tencent.mm","微信","视频缓存", "$it/finder/video"))
        add(ApplicationGarbage("com.tencent.mm","微信","图片缓存", "$it/finder/image"))
        add(ApplicationGarbage("com.tencent.mm","微信","缓存", "$it/finder/tmp"))
    }
    Constant.weChatUserPath.forEach {
        add(ApplicationGarbage("com.tencent.mm","微信","视频缓存", "$it/finder/video"))
        add(ApplicationGarbage("com.tencent.mm","微信","图片缓存", "$it/finder/image"))
        add(ApplicationGarbage("com.tencent.mm","微信","缓存", "$it/finder/tmp"))
    }
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","缓存","${Constant.QQ_PATH_MOBILEQQ}/cache"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","缓存","${Constant.QQ_PATH}/files/ae/sv_config_resources"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","缓存","/storage/emulated/0/download/.com.tencent.mobileqq"))

    add(ApplicationGarbage("com.tencent.mm","微信","缓存", "/storage/emulated/0/tencent/MicroMsg/videocache/appbrand0"))
    add(ApplicationGarbage("com.tencent.mm","微信","缓存", "/storage/emulated/0/tencent/MicroMsg/videocache/appbrand1"))
    add(ApplicationGarbage("com.tencent.mm","微信","缓存", "/storage/emulated/0/tencent/MicroMsg/videocache/appbrand2"))
    add(ApplicationGarbage("com.tencent.mm","微信","缓存", "/storage/emulated/0/tencent/MicroMsg/videocache/appbrand3"))
    add(ApplicationGarbage("com.tencent.mm","微信","缓存", "/storage/emulated/0/tencent/MicroMsg/videocache/appbrand4"))

    add(ApplicationGarbage("com.tencent.mm","微信","缓存", "/storage/emulated/0/tencent/apdl"))
    add(ApplicationGarbage("com.tencent.mm","微信","缓存", "/storage/emulated/0/tencent/MicroMsg/webnetfile"))
    add(ApplicationGarbage("com.tencent.mm","微信","缓存", "/storage/emulated/0//Android/data/com.tencent.mm/cache/imgcache"))

    add(ApplicationGarbage("com.eg.android.AlipayGphone", "支付宝", "缓存", "/storage/emulated/0/Alipay/com.eg.android.AlipayGphone/nebuladownload"))
    add(ApplicationGarbage("com.eg.android.AlipayGphone", "支付宝", "缓存", "/storage/emulated/0/Alipay/com.eg.android.AlipayGphone/applogic"))
    add(ApplicationGarbage("com.eg.android.AlipayGphone", "支付宝", "缓存", "/storage/emulated/0/Alipay/com.eg.android.AlipayGphone/arplatform"))
    add(ApplicationGarbage("com.eg.android.AlipayGphone", "支付宝", "缓存", "/storage/emulated/0/Alipay/com.eg.android.AlipayGphone/openplatform"))

    //20200618
    add(ApplicationGarbage("com.tencent.qqmusic","QQ音乐","音乐缓存","/storage/emulated/0/qqmusic/gift_anim_zip/v_anim"))
    add(ApplicationGarbage("com.tencent.qqmusic","QQ音乐","音乐缓存","/storage/emulated/0/qqmusic/cache"))
    add(ApplicationGarbage("com.sdu.didi.psnger","滴滴出行","腾讯地图缓存数据","/storage/emulated/0/tencentmapsdk"))
    add(ApplicationGarbage("com.tencent.qqlive","腾讯视频","缓存","/storage/emulated/0/Android/data/com.tencent.qqlive/files/backup"))
    add(ApplicationGarbage("com.tencent.qqlive","腾讯视频","图片缓存","/storage/emulated/0/Android/data/com.tencent.qqlive/files/p_image"))
    add(ApplicationGarbage("com.tencent.qqlive","腾讯视频","直播图标缓存","/storage/emulated/0/Android/data/com.tencent.qqlive/files/.tab/icon/dirs"))
    add(ApplicationGarbage("com.tencent.qqpimsecure","腾讯手机管家","下载的插件","/storage/emulated/0/QQSecureDownload"))
    add(ApplicationGarbage("com.tencent.qqpimsecure","腾讯手机管家","缓存","/storage/emulated/0/QQSecureDownload/mfcache"))
    add(ApplicationGarbage("com.tencent.qqpimsecure","腾讯手机管家","临时文件","/storage/emulated/0/QQSecureDownload/gallerycache"))
    add(ApplicationGarbage("com.tencent.qqpimsecure","腾讯手机管家","缓存","/storage/emulated/0/qmt"))
    add(ApplicationGarbage("com.cubic.autohome","汽车之家","图片缓存","/storage/emulated/0/autohomemain/img"))
    add(ApplicationGarbage("com.cubic.autohome","汽车之家","缓存","/storage/emulated/0/autohomemain/article_page_new"))
    add(ApplicationGarbage("com.cubic.autohome","汽车之家","缓存","/storage/emulated/0/Android/data/com.cubic.autohome/files"))
    add(ApplicationGarbage("com.baidu.BaiduMap","百度地图","缓存","/storage/emulated/0/BaiduMap/cache"))
    add(ApplicationGarbage("com.baidu.BaiduMap","百度地图","导航文件缓存","/storage/emulated/0/BaiduMap/bnav/navi/pub"))
    add(ApplicationGarbage("com.baidu.BaiduMap","百度地图","导航缓存","/storage/emulated/0/BaiduMap/bnav/cache"))
    add(ApplicationGarbage("com.baidu.BaiduMap","百度地图","导航缓存","/storage/emulated/0/BaiduMap/bnav/trajectory"))
    add(ApplicationGarbage("cn.goapk.market","安智市场","缓存","/storage/emulated/0/anzhi/.cache"))
    add(ApplicationGarbage("cn.goapk.market","安智市场","图标缓存","/storage/emulated/0/anzhi/.manage"))
    add(ApplicationGarbage("cn.goapk.market","安智市场","广告图片","/storage/emulated/0/anzhi/.screen"))
    add(ApplicationGarbage("cn.goapk.market","安智市场","监控日志","/storage/emulated/0/anzhi/.log"))
    add(ApplicationGarbage("com.ss.android.article.news","今日头条","更新安装包","/storage/emulated/0/Android/data/com.ss.android.article.news/files"))
//    add(ApplicationGarbage("com.ss.android.article.news","今日头条","缓存","/storage/emulated/0/Android/data/com.ss.android.article.news/weboffline/adblock"))
    add(ApplicationGarbage("com.ss.android.article.news","今日头条","缓存","/storage/emulated/0/Android/data/com.ss.android.article.news/weboffline"))
    add(ApplicationGarbage("com.ss.android.article.news","今日头条","缓存","/storage/emulated/0/Android/data/com.snssdk.api"))
    add(ApplicationGarbage("com.cheetah.cmcooler","手机降温精灵","图标缓存","/storage/emulated/0/Android/data/com.cheetah.cmcooler/files/iconcache"))
    add(ApplicationGarbage("com.cheetah.cmcooler","手机降温精灵","日志文件","/storage/emulated/0/Android/data/com.cheetah.cmcooler/files/logs"))
    add(ApplicationGarbage("com.huawei.hwid","华为移动服务","日志文件","/storage/emulated/0/Android/data/com.huawei.hwid/files/log"))
    add(ApplicationGarbage("com.ijinshan.kbatterydoctor","金山电池医生","缓存","/storage/emulated/0/Android/data/com.ijinshan.kbatterydoctor/files"))
    add(ApplicationGarbage("com.ijinshan.kbatterydoctor","金山电池医生","图片缓存（1天前）","/storage/emulated/0/news_sdk/.image"))
    add(ApplicationGarbage("com.ijinshan.kbatterydoctor","金山电池医生","缓存","/storage/emulated/0/kbatterydoctor"))
    add(ApplicationGarbage("cmb.pb","招商银行","头像缓存","/storage/emulated/0/cmb.pb/avatar"))
    add(ApplicationGarbage("cmb.pb","招商银行","缓存","/storage/emulated/0/cmb.pb/image"))
    add(ApplicationGarbage("cmb.pb","招商银行","数据缓存","/storage/emulated/0/cmb/data"))
    //20200715
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","缓存","/storage/emulated/0/tencent/TMAssistantSDK/download/com.tencent.mm"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","网页缓存","/storage/emulated/0/Android/data/com.tencent.mobileqq/tencent/mobileqq/pddata/app/offline/html5"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","图片缓存","/storage/emulated/0/Android/data/com.tencent.mobileqq/tencent/mobileqq/chatpic"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","图标缓存","/storage/emulated/0/Android/data/com.tencent.mobileqq/qzone/head_drop_operaion"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","缓存","/storage/emulated/0/Android/data/com.tencent.mobileqq/files/decoder_probe_res"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","图片缓存","/storage/emulated/0/Android/data/com.tencent.mobileqq/tencent/mobileqq/diskcache"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","图片缓存","/storage/emulated/0/Android/data/com.tencent.mobileqq/qzone/imagev2"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","图标缓存","/storage/emulated/0/Android/data/com.tencent.mobileqq/qzone/zip_cache"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","缓存","/storage/emulated/0/Android/data/com.tencent.mobileqq/tencent/mobileqq/.vaspoke"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","图标缓存","/storage/emulated/0/Android/data/com.tencent.mobileqq/qzone/surprise"))
    add(ApplicationGarbage("com.tencent.mobileqq","QQ","缓存","/storage/emulated/0/Android/data/com.tencent.mobileqq/files/video_dec_probe"))
    add(ApplicationGarbage("com.eg.android.AlipayGphone","支付宝","缓存","/storage/emulated/0/alipay/com.eg.android.AlipayGphone/applogic"))
    add(ApplicationGarbage("com.eg.android.AlipayGphone","支付宝","地图缓存","/storage/emulated/0/amap/data_v6"))
    add(ApplicationGarbage("com.eg.android.AlipayGphone","支付宝","缓存","/storage/emulated/0/alipay/com.eg.android.AlipayGphone/trafficlogic"))
    add(ApplicationGarbage("com.baidu.BaiduMap","百度地图","缓存","/storage/emulated/0/BaiduMap/cache"))
    add(ApplicationGarbage("com.baidu.BaiduMap","百度地图","导航文件","/storage/emulated/0/BaiduMap/bnav/navi/pub"))
    add(ApplicationGarbage("com.baidu.BaiduMap","百度地图","导航缓存","/storage/emulated/0/BaiduMap/bnav/cache"))
    add(ApplicationGarbage("com.alibaba.android.rimet","钉钉","日志文件","/storage/emulated/0/Android/data/com.alibaba.android.rimet/files/logs"))
    add(ApplicationGarbage("com.alibaba.android.rimet","钉钉","缓存","/storage/emulated/0/Android/data/com.alibaba.android.rimet/files/mozirtc"))
    add(ApplicationGarbage("com.alibaba.android.rimet","钉钉","缓存","/storage/emulated/0/Android/data/com.alibaba.android.rimet/files/awcn_strategy"))
    add(ApplicationGarbage("com.alibaba.android.rimet","钉钉","缓存","/storage/emulated/0/Android/data/com.alibaba.android.rimet/files/perf"))
    add(ApplicationGarbage("com.tencent.androidqqmail","QQ邮箱","日志文件","/storage/emulated/0/tencent/QQmail/log"))
    add(ApplicationGarbage("com.tencent.androidqqmail","QQ邮箱","图片缓存","/storage/emulated/0/tencent/QQmail/imagecache"))
    add(ApplicationGarbage("com.cubic.autohome","汽车之家","图片缓存","/storage/emulated/0/autohomemain/img"))
    add(ApplicationGarbage("com.cubic.autohome","汽车之家","缓存","/storage/emulated/0/autohomemain/article_page_new"))
    add(ApplicationGarbage("com.tencent.qqlive","腾讯视频","缓存","/storage/emulated/0/Android/data/com.tencent.qqlive/files/vnapp"))
    add(ApplicationGarbage("com.xunmeng.pinduoduo","拼多多","缓存","/storage/emulated/0/Android/data/com.xunmeng.pinduoduo/files/iris"))
    //20200716
    add(ApplicationGarbage("com.ss.android.ugc.aweme","抖音","缓存","/storage/emulated/0/android/data/com.ss.android.ugc.aweme/cache"))
    add(ApplicationGarbage("com.ss.android.ugc.aweme","抖音","缓存","/storage/emulated/0/android/data/com.ss.android.ugc.aweme/cache/ttcjpaywebdata/cashdesk_offline/assets"))
    add(ApplicationGarbage("com.ss.android.ugc.aweme","抖音","缓存","/storage/emulated/0/android/data/com.ss.android.ugc.aweme/cache/prefs"))
    add(ApplicationGarbage("com.ss.android.ugc.aweme","抖音","缓存","/storage/emulated/0/android/data/com.ss.android.ugc.aweme/bytedance"))
    add(ApplicationGarbage("com.tencent.news","腾讯新闻","日志","/storage/emulated/0/android/data/com.tencent.news/files/netlog"))
}.toList()

data class ADGarbage(val packageName: String?,val appName: String,val des: String,val path:String)
val adGarbagePaths=HashSet<ADGarbage>().apply {
    add(ADGarbage(null,"广点通","广点通下载文件","/storage/emulated/0/GDTDOWNLOAD"))
    add(ADGarbage(null,"百度", "百度下载文件","/storage/emulated/0/bddownload"))
    add(ADGarbage("com.tencent.mm","微信","朋友圈广告","/storage/emulated/0/tencent/MicroMsg/sns_ad_landingpages"))
    add(ADGarbage(null,"微博","微博广告","/storage/emulated/0/sina/weibo/.weibo_ad_universal_cache"))
    add(ADGarbage(null,"um","um下载文件","/storage/emulated/0/.um"))
    add(ADGarbage("com.taobao.idlefish","咸鱼","缓存","/storage/emulated/0/Android/data/com.taobao.idlefish/files"))
    add(ADGarbage("com.taobao.idlefish","咸鱼","日志","/storage/emulated/0/android/data/com.taobao.idlefish/files/tnetlogs"))
    add(ADGarbage("com.taobao.idlefish","咸鱼","广告","/storage/emulated/0/android/data/com.taobao.idlefish/files/ad"))
    add(ADGarbage("com.taobao.idlefish","咸鱼","日志","/storage/emulated/0/android/data/com.taobao.idlefish/files/logs"))
    add(ADGarbage("com.taobao.idlefish","咸鱼","日志","/storage/emulated/0/android/data/com.taobao.idlefish/files/MiPushLog"))
    add(ADGarbage("com.miui.systemAdSolution","小米广告的广告","缓存","/storage/emulated/0/android/data/com.miui.systemAdSolution/files/miad/cacheFolderAd"))

    add(ADGarbage("com.miui.systemAdSolution","小米广告的广告","日志","/storage/emulated/0/android/data/com.miui.systemAdSolution/files/MiPushLog"))
    add(ADGarbage(null,"其他广告垃圾","无用缩略图","/storage/emulated/0/dcim/.thumbnails"))
    add(ADGarbage(null,"其他广告垃圾","未知厂商广告","/storage/emulated/0/log/tencent"))
    add(ADGarbage(null,"其他广告垃圾","广告垃圾","/storage/emulated/0/Catfish"))
    add(ADGarbage(null,"其他广告垃圾","未知广告","/storage/emulated/0/setup"))
    add(ADGarbage(null,"其他广告垃圾","友盟广告","/storage/emulated/0/.um"))
    add(ADGarbage("com.xiaomi.jr","小米金融的广告","日志文件","/storage/emulated/0/android/data/com.xiaomi.jr/files/tbslog"))
    add(ADGarbage("com.xiaomi.jr","小米金融的广告","日志文件","/storage/emulated/0/android/data/com.xiaomi.jr/files/MiPushLog"))
    add(ADGarbage("com.xiaomi.jr","小米金融的广告","缓存","/storage/emulated/0/android/data/com.xiaomi.jr/files/mipushlog"))
    add(ADGarbage("com.android.thememanager","主题设置的广告","日志文件","/storage/emulated/0/android/data/com.android.thememanager/files/MiPushLog"))
    add(ADGarbage("com.android.thememanager","主题设置的广告","日志缓存","/storage/emulated/0/Android/data/com.android.thememanager/files/mipushlog"))
    add(ADGarbage("com.android.thememanager","主题设置的广告","个性主题","/storage/emulated/0/miui/theme/.data/rights/theme"))
    add(ADGarbage(null,"腾讯MSF日志","","/storage/emulated/0/tencent/msflogs"))
    add(ADGarbage(null,"小米日志","","/storage/emulated/0/MIUI/debug_log"))
    add(ADGarbage(null,"MI广告","","/storage/emulated/0/miad/cache"))
    add(ADGarbage(null,"MF广告","","/storage/emulated/0/mfcache"))
    add(ADGarbage(null,"百度推广日志","","/storage/emulated/0/.BD_SAPI_CACHE"))
    add(ADGarbage(null,"未知厂商广告","","/storage/emulated/0/log"))
    add(ADGarbage(null,"腾讯日志","","/storage/emulated/0/tencent/imsdklogs"))
    add(ADGarbage(null,"腾讯日志","","/storage/emulated/0/tencent/qalsdklogs"))
    add(ADGarbage(null,"百度临时文件","","/storage/emulated/0/baidu/tempdata"))
    add(ADGarbage(null,"未知厂商","","/storage/emulated/0/QCDownload"))
    add(ADGarbage(null,"广告缓存","","/storage/emulated/0/Catfish"))
    add(ADGarbage(null,"未知广告","","/storage/emulated/0/setup"))
    add(ADGarbage(null,"可清理的腾讯文件","","/storage/emulated/0/tencent/mta"))
    add(ADGarbage(null,"可清理的淘宝文件","","/storage/emulated/0/.UTSystemConfig"))
    add(ADGarbage(null,"阿里网页文件","","/storage/emulated/0/.DataStorage"))
    add(ADGarbage(null,"腾讯日志","","/storage/emulated/0/.tbs"))
    add(ADGarbage(null,"msc广告","","/storage/emulated/0/msc"))
    add(ADGarbage(null,"广告图片","","/storage/emulated/0/imgCache"))

}.toList()