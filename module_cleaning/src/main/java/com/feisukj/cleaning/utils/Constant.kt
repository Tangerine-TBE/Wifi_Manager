package com.feisukj.cleaning.utils

import android.os.Build
import android.os.Environment
import com.example.module_base.cleanbase.BaseConstant

import java.io.File

object Constant {
    private val rootPath=Environment.getExternalStorageDirectory().absolutePath
    private val qqVersionCode by lazy {
        try {
            val packInfo=
                BaseConstant.application.packageManager.getPackageInfo("com.tencent.mobileqq",0)
            val versionCode=if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.P){
                packInfo.longVersionCode
            }else{
                packInfo.versionCode.toLong()
            }
            versionCode
        }catch (e:Exception){
            -1L
        }
    }
    private val weChatVersionCode by lazy {
        try {
            val packInfo=BaseConstant.application.packageManager.getPackageInfo("com.tencent.mm",0)
            val versionCode=if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.P){
                packInfo.longVersionCode
            }else{
                packInfo.versionCode.toLong()
            }
            versionCode
        }catch (e:Exception){
            -1L
        }
    }

    val TENCENT_PATH= "$rootPath/tencent"//腾讯目录

    val QQ_TENCENT by lazy {
        if (qqVersionCode>=1406){
            "/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent"
        }else{
            TENCENT_PATH
        }
    }
    val QQ_PATH_MOBILEQQ by lazy {
        QQ_TENCENT+"/MobileQQ"
    }
    val QQ_PATH by lazy {
        try {
            val packInfo=BaseConstant.application.packageManager.getPackageInfo("com.tencent.mobileqq",0)
            val versionCode=if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.P){
                packInfo.longVersionCode
            }else{
                packInfo.versionCode.toLong()
            }
            if (versionCode>=1406){//versionName 8.3.6
                "/storage/emulated/0/Android/data/com.tencent.mobileqq"
            }else{
                "$TENCENT_PATH/MobileQQ"
            }
        }catch (e: Exception){
           // MobclickAgent.reportError(BaseConstant.application,e)
            "$TENCENT_PATH/MobileQQ"
        }
    }
    val QQ_USER_PATH by lazy {
        val qqFile1=File("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent"+"/MobileQQ")
        val list1 = if (!qqFile1.exists()) {
            return@lazy emptyList<String>()
        } else{
            qqFile1.listFiles()?.asSequence()?.filter { file ->
                file.name.all {
                    it in '0' .. '9'
                }&&file.name.length in 6..20
            }?.map { it.absolutePath }?.toList()?: emptyList()
        }
        val qqFile2=File("$TENCENT_PATH/MobileQQ")
        val list2 = if (!qqFile1.exists()) {
            return@lazy emptyList<String>()
        } else{
            qqFile2.listFiles()?.asSequence()?.filter { file ->
                file.name.all {
                    it in '0' .. '9'
                }&&file.name.length in 6..20
            }?.map { it.absolutePath }?.toList()?: emptyList()
        }
        val list = list1+list2
        list
    }

    val SYSTEM_SERVICE_PACKAGENAME by lazy {
        setOf(
                "android",
                "com.google.android.gms",
                "com.android.smspush",
                BaseConstant.packageName
        )
    }
    val SYSTEM_DESKTOP_PACKAGENAME by lazy { setOf(
//            "android",
            "com.google.android.googlequicksearchbox",//谷歌系统的桌面
//            "com.google.android.gms",
            "com.miui.home",
            "com.oppo.launcher",
            "com.huawei.android.launcher",
            "com.vivo.weather.provider",//vivo,com.vivo.weather.provider
            "com.sec.android.app.launcher",//三星
            "com.meizu.flyme.launcher",
            "com.gionee.amisystem",//金立
            "com.smartisanos.tracker",//坚果
            "com.zui.launcher",//联想
            "net.oneplus.launcher",//一加
            "com.sonyericsson.home",//索尼
            "com.android.launcher3",//乐视
            "cn.nubia.launcher",//努比亚
            "com.android.launcher3",//中兴
            "com.android.launcher3",//Nokia
            "com.yulong.android.launcher3",//奇酷
            "com.asus.launcher3",//ASUS
            "com.lge.appbox.client",//LG
            "com.lge.launcher3",//LG
            "com.hmct.vision",//海信
            "com.android.launcher3",//摩托罗拉
            "com.tpv.launcher3",//飞利浦
            "com.android.launcher3"//美图
    ) }

    /******************************************/
    val PICTURE_FORMAT= arrayOf(".jpg",".jpeg",".png",".raw",".gif",".svg")//listOf("bmp","jpg","png","tif","gif","pcx","tga","exif","fpx","svg","psd","cdr","pcd","dxf","ufo","eps","ai","raw","WMF","webp")
    val VIDEO_FORMAT= arrayOf(".3gp", ".amv", ".flv", ".mp4",".mpeg",".mpg")
    val MUSIC_FORMAT= arrayOf(".mp3")
    val ZIP_FORMAT= arrayOf(".001" , ".7z" ,".ace" ,".arj" ,".bz" ,".bz2" ,
            ".cab" ,".gz" ,".iso" ,".jar" ,".Iha" ,".I2" ,
            ".Izh" ,".rar" ,".tar" ,".taz" ,".tbz" ,".tbz2" ,
            ".tgz" ,".tlz" ,".Ju" ,".uue" ,".xxe " ,".zip" ,
            ".zipx")
    val DOC_FORMAT= arrayOf(".txt",".doc",".docx",".pdf",".ppt",".xls",".xlsx")
    /*************************************/
    var SCREENSHOTS_PHOTO:String=""//手机截图文件路径
    private set
        get() {
            if (field.isNotBlank())
                return field
            val file=File("${rootPath}/DCIM/Screenshots")
            if (file.exists()){
                field=file.absolutePath
            }else{
                val file2=File("${rootPath}/Pictures/Screenshots")
                if (file2.exists()){
                    field=file2.absolutePath
                }
            }
            return field
        }
    val DCIM=Environment.getExternalStorageDirectory().absolutePath+File.separator+Environment.DIRECTORY_DCIM
    val CAMERA="${rootPath}/DCIM/Camera"//手机相册及手机视频
    val DOWNLOAD="${rootPath}/Download"//

    val garbagePicturePaths= arrayOf(
            "/storage/emulated/0/android/data/com.taobao.taobao/cache/",
            "/storage/emulated/0/Android/data/com.wuba.zhuanzhuan/cache/fresco_images/",
            "/storage/emulated/0/Android/data/com.wuba.zhuanzhuan/files/zzface/",
            "/storage/emulated/0/tencent/MicroMsg/wxacache/",
            "/storage/emulated/0/tencent/MicroMsg/Download/appbrand/",
            "/storage/emulated/0/Tencent/micromsg/sns_ad_landingpages/",
            "/storage/emulated/0/Android/data/com.tencent.mm/MicroMsg/wallet/",
            "/storage/emulated/0/tencent/MicroMsg/wallet_images/",
            "/storage/emulated/0/tencent/MicroMsg/vusericon/",
            "/storage/emulated/0/Android/data/com.qzone/cache/",
            "$QQ_PATH_MOBILEQQ/.apollo/",
            "$QQ_PATH_MOBILEQQ/head/_hd/",
            "$QQ_PATH_MOBILEQQ/thumb/",
            "/storage/emulated/0/Tencent/tassistant/pic/",
            "$QQ_PATH_MOBILEQQ/pe/res/",
            "/storage/emulated/0/Android/data/com.tencent.mobileqq/qzone/surprise/",
            "$QQ_PATH_MOBILEQQ/portrait/",
            "$QQ_PATH_MOBILEQQ/shortvideo/thumbs/",
            "/storage/emulated/0/Tencent/QQfile_recv/.trooptmp/",
            "$QQ_PATH_MOBILEQQ/appicon/",
            "$QQ_PATH_MOBILEQQ/head/_stranger/",
            "$QQ_PATH_MOBILEQQ/head/_SSOhd/",
            "$QQ_PATH_MOBILEQQ/.apollo/image_cache/",
            "$QQ_PATH_MOBILEQQ/status_ic/",
            "/storage/emulated/0/android/data/com.zhihu.android/cache/",
            "/storage/emulated/0/sina/weibo/storage/photoalbum_pic/.prenew/",
            "/storage/emulated/0/sina/weibo/storage/photoalbum_pic/.weibo_pic_new/",
            "/storage/emulated/0/sina/weibo/storage/photoalbum_pic/.portraitnew/",
            "/storage/emulated/0/sina/weibo/storage/pagecard_no_auto_clear/",
            "/storage/emulated/0/alipay/multimedia/",
            "/storage/emulated/0/autonavi/afpSplash/",
            "/storage/emulated/0/autonavi/cache/gif/",
            "/storage/emulated/0/kugou/.glide/",
            "/storage/emulated/0/kugou/.splash/",
            "/storage/emulated/0/kugou/.images/",
            "/storage/emulated/0/kugou/msgchat/",
            "/storage/emulated/0/kugou/.userinfo/friendicon/",
            "/storage/emulated/0/kugou/msgchat/camera/",
            "/storage/emulated/0/kugou/.userinfo/friendicon/",
            "/storage/emulated/0/Android/data/com.jingdong.app.mall/files/image/",
            "/storage/emulated/0/android/data/com.baidu.mbaby/files/image/",
            "/storage/emulated/0/Android/data/com.MobileTicket/cache/bontai/splash/img/",
            "/storage/emulated/0/anzhi/.screen/",
            "/storage/emulated/0/anzhi/.manage/",
            "/storage/emulated/0/qqmusic/gift_anim_zip/",
            "/storage/emulated/0/qqmusic/splash/",
            "/storage/emulated/0/360/newssdk/com.qihoo.appstore/.c/img/",
            "/storage/emulated/0/autohomemain/img/",
            "/storage/emulated/0/tencent/qqmail/imagecache/",
            "/storage/emulated/0/.youdaonote/userinfo/",
            "/storage/emulated/0/android/data/com.ucmobile/cache/uil-images/"
    )

    /**************************微信***************************************/
    val WE_CHAT_ROOT_PATH="$TENCENT_PATH/MicroMsg"//微信目录
//    val WE_CHAT_ROOT_PATH="/storage/emulated/0/android/data/com.tencent.mm/MicroMsg"//微信目录
    const val WECHAT_DATA="/storage/emulated/0/android/data/com.tencent.mm"
    const val WE_CHAT_ROOT_PATH_DATA_MircroMsg="/storage/emulated/0/android/data/com.tencent.mm/MicroMsg"
    const val WE_CHAT_ROOT_PATH_DATA_Cache="/storage/emulated/0/android/data/com.tencent.mm/cache"
//    val WE_SAVE_PIC="$WE_CHAT_ROOT_PATH/WeiXin"//保存的图片或视频，和通过相机拍摄的
    val WE_SAVE_PIC="/storage/emulated/0/Pictures/WeiXin"//保存的图片或视频，和通过相机拍摄的
    val WE_FILE="$WE_CHAT_ROOT_PATH/Download"//微信文件
    val WE_FILE_DATA="$WE_CHAT_ROOT_PATH_DATA_MircroMsg/Download"//微信文件
    //微信的用户文件夹集合(外部目录)
    val weChatUserPath by lazy {
        val list= ArrayList<String>()
//        val weChatRoot=if (weChatVersionCode>=1720){//weChatVersionCode>=1720
//            File(WE_CHAT_ROOT_PATH_DATA_MircroMsg)
//        }else{
//            File(WE_CHAT_ROOT_PATH)
//        }
//        if (weChatRoot.exists()){
//            val fileList=weChatRoot.listFiles()
//            fileList?.forEach {
//                if (it.isDirectory&&it.name.length==32){
//                    list.add(it.absolutePath)
//                }
//            }
//        }
        val weChatRoot1 = File(WE_CHAT_ROOT_PATH_DATA_MircroMsg)
        val weChatRoot2 = File(WE_CHAT_ROOT_PATH)
        if (weChatRoot1.exists()){
            val fileList1 = weChatRoot1.listFiles()
            fileList1?.forEach {
                if (it.isDirectory&&it.name.length==32){
                    list.add(it.absolutePath)
                }
            }
        }
        if (weChatRoot2.exists()){
            val fileList2 = weChatRoot1.listFiles()
            fileList2?.forEach {
                if (it.isDirectory&&it.name.length==32){
                    list.add(it.absolutePath)
                }
            }
        }
        list.toList()
    }
    val weChatUserPath_Data by lazy {//微信的用户文件夹集合(包名下的目录)
        val list=ArrayList<String>()
        val weChatRoot=File(WE_CHAT_ROOT_PATH_DATA_MircroMsg)
        if (weChatRoot.exists()){
            val fileList=weChatRoot.listFiles()
            fileList?.forEach {
                if (it.isDirectory&&it.name.length==32){
                    list.add(it.absolutePath)
                }
            }
        }
        list.toList()
    }
    val weChatUserPath_Data_Cache by lazy {
        val list=ArrayList<String>()
        val weChatRoot=File(WE_CHAT_ROOT_PATH_DATA_Cache)
        if (weChatRoot.exists()){
            val fileList=weChatRoot.listFiles()
            fileList?.forEach {
                if (it.isDirectory&&it.name.length==32){
                    list.add(it.absolutePath)
                }
            }
        }
        list.toList()
    }
    val WE_CHAT_PIC by lazy {//聊天图片。。。
        weChatUserPath.map { "$it/image2" }
    }
    val WE_FRIEND by lazy {//朋友圈的图片和视频
        if (weChatVersionCode>=1680){
            weChatUserPath_Data_Cache.map { "$it/sns" }
        }else{
            weChatUserPath_Data.map { "$it/sns" }
        }
    }
    val WE_ENOJI by lazy {//表情
        weChatUserPath.map { "$it/emoji" }
    }
    val WE_YUYIN by lazy {//语音
        weChatUserPath.map { "$it/voice2" }
    }
    val WE_VIDEO by lazy {//视频
        weChatUserPath.map { "$it/video" }
    }

    //扫描最近文件用
    val WE_PATH_IMAGE:ArrayList<String> by lazy {
        val temp=ArrayList<String>()
        weChatUserPath.forEach {
            temp.add("${it}/image")
        }
        temp
    }
    val WE_PATH_DOWN="${WE_CHAT_ROOT_PATH}/Download"
    /**************************微信END***************************************/

    /**************************QQ***************************************/
    val QQ_PHOTO="$QQ_PATH_MOBILEQQ/photo"//QQ图片文件
    val QQ_CHAT_PIC="$QQ_PATH_MOBILEQQ/diskcache"
    val CHAT_IMG="$QQ_PATH_MOBILEQQ/chatpic/chatimg"
    val QQ_VIDEO="$QQ_PATH_MOBILEQQ/shortvideo"//QQ视频文件
    val QQ_T_FILE="$QQ_TENCENT/QQfile_recv"//qq文件
    val QQ_YUYIN by lazy {
        QQ_USER_PATH.map { "$it/ptt" }
    }//语音
    val QQ_SAVE_IMAGE="$TENCENT_PATH/QQ_Images"//
    /**************************End*************************************/

    //来自 “其他相册”
//    val videoManagerDownload= listOf(
//            DOWNLOAD,
//            "/storage/emulated/0/alipay/pictures",
//            "/storage/emulated/0/wangmi",
//            "/storage/emulated/0/autohomemain/imgocr",
//            "/storage/emulated/0/Lark/camera/photo",
//            "/storage/emulated/0/LuPingDaShi/Picture",
//            "/storage/emulated/0/runzhong/screen_record/records",
//            "/storage/emulated/0/LuPingDaShi/recommendcache",
//            "/storage/emulated/0/tieba",
//            "/storage/emulated/0/TongChengMiYue/image",
//            "/storage/emulated/0/DCIM/MobileTicket",
//            "/storage/emulated/0/feisu/browser/image",
//            "/storage/emulated/0/YueHuiBa/image",
//            "/storage/emulated/0/tencent/aekit/MaskImages",
//            "/storage/emulated/0/ctcms_amj/splash_ad",
//
//            "/storage/emulated/0/LuPingDaShi/Rec",
//            "/storage/emulated/0/LuPingDaShi/recommendcache",
//            "/storage/emulated/0/icbcVideoAuthen/video",
//            "/storage/emulated/0/hudun/rec/video",
//            "/storage/emulated/0/ddmh/livewallpaper/download",
//            "/storage/emulated/0/runzhong/screen_record/records" ,
//            "/storage/emulated/0/tencent/QQfile_recv",
//            "/storage/emulated/0/qqmusic/landscape",
//            "/storage/emulated/0/VpnCapture/ParseData"
//    )

    //其他相册
    val otherPicture= setOf(
            CAMERA to "相机",
            DOWNLOAD to "下载",
            WE_SAVE_PIC to "微信保存的图片",
            QQ_VIDEO to "QQ视频文件",
            "/storage/emulated/0/Pictures" to "Pictures",
            "/storage/emulated/0/alipay/pictures" to "alipay",
//            "/storage/emulated/0/Pictures/com.uranus.timePlan",
            "/storage/emulated/0/wangmi" to "wangmi",
            "/storage/emulated/0/autohomemain/imgocr" to "imgocr",
            "/storage/emulated/0/Lark/camera/photo" to "Lark",
            "/storage/emulated/0/LuPingDaShi/Picture" to "录屏大师",
            "/storage/emulated/0/runzhong/screen_record/records" to "runzhong",
            "/storage/emulated/0/LuPingDaShi/recommendcache" to "录屏大师",
            "/storage/emulated/0/tieba" to "贴吧",
            "/storage/emulated/0/TongChengMiYue/image" to "同城密约",
            "/storage/emulated/0/DCIM/MobileTicket" to "MobileTicket",
            "/storage/emulated/0/feisu/browser/image" to "飞速浏览器",
            "/storage/emulated/0/YueHuiBa/image" to "YueHuiBa",
            "/storage/emulated/0/tencent/aekit/MaskImages" to "MaskImages",
            "/storage/emulated/0/ctcms_amj/splash_ad" to "开屏图片",
            "$QQ_PATH_MOBILEQQ/photo" to "QQ图片",

            "/storage/emulated/0/LuPingDaShi/Rec" to "录屏大师",
            "/storage/emulated/0/LuPingDaShi/recommendcache" to "录屏大师",
            "/storage/emulated/0/icbcVideoAuthen/video" to "工商银行",
            "/storage/emulated/0/hudun/rec/video" to "hudun",
            "/storage/emulated/0/ddmh/livewallpaper/download" to "livewallpaper",
            "/storage/emulated/0/runzhong/screen_record/records" to "runzhong",
            "/storage/emulated/0/tencent/QQfile_recv" to "QQ文件",
            "/storage/emulated/0/qqmusic/landscape" to "landscape",
            "/storage/emulated/0/VpnCapture/ParseData" to "VpnCapture"
    ).toTypedArray()
    /**************************************专清****************************************/
    /**
     * 微信专清
     */
    val weChatGarbage= ArrayList<String>().apply {
        Constant.weChatUserPath.forEach {
            add("$it/bizmsg")
            add("$it/openapi")
        }
        add("/storage/emulated/0/tencent/MicroMsg/CheckResUpdate")//检查更新文件
        add("/storage/emulated/0/tencent/MicroMsg/diskcache")
        add("/storage/emulated/0/tencent/MicroMsg/xlog")
        add("/storage/emulated/0/tencent/MicroMsg/wxacache")
        add("/storage/emulated/0/tencent/MicroMsg/.tmp")//临时文件
        add("/storage/emulated/0/tencent/MicroMsg/CDNTemp")//临时文件
    }
    /**
     * /storage/emulated/0/tencent/MicroMsg/b06d5b78e9fa64917a409bd60ccd5f4c/emoji/com.tencent.xin.emoticon.person.stiker_1493602656ac35ee2e7b2ce9b7/815f4e30b9d7d91cf0378cba53f00924_panel_enable
     * 仅包含文件名末尾为：_panel_enable
     */
    val weChatCache= ArrayList<String>().apply {
        Constant.weChatUserPath.forEach {
            add("$it/emoji")
        }
        add("/storage/emulated/0/tencent/MicroMsg/diskcache")//缓存文件
        add("/storage/emulated/0/tencent/MicroMsg/wallet")//缓存图片
    }
    val weChatFriend=ArrayList<String>().apply {
        addAll(WE_FRIEND)
        add("/storage/emulated/0/tencent/MicroMsg/sns_ad_landingpages")//朋友圈广告
    }
    val weChatOther=ArrayList<String>().apply {
        Constant.weChatUserPath.forEach {
            add("$it/image")
        }
    }
}