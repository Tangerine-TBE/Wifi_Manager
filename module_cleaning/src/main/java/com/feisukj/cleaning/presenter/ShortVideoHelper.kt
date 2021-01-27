package com.feisukj.cleaning.presenter


import com.example.module_base.cleanbase.BaseConstant
import com.feisukj.cleaning.file.ShortVideoPath
import com.feisukj.cleaning.file.shortVideoPath
import java.io.File

class ShortVideoHelper private constructor(){
//    enum class ShortVideoType(val path: String,val packageName:String){
//        douyin(Constant.douyinPath,"com.ss.android.ugc.aweme"),//抖音
//        weishi(Constant.weishiPath,"com.tencent.weishi"), //微视
//        uc(Constant.ucPath,"com.UCMobile"),//uc
//        allPeopleVideo(Constant.allShortVideoPath,"com.baidu.minivideo"),//全民小视频
//        huosan(Constant.huosanPath,"com.ss.android.ugc.live"),//火山
//        kuaishou(Constant.kuaishouPath,"com.smile.gifmaker"),//火山极速版
//        huosanExtreme(Constant.huosanExtremePath,"com.ss.android.ugc.livelite"),//快手
//        faceu(Constant.faceuPath,"com.lemon.faceu"),
//        qiuqiu(Constant.qiuqiuPath,"com.qukandian.video"),
//        tencenNow(Constant.tencentNowPath,"com.tencent.now"),
//        meipai(Constant.meipaiPath,"com.meitu.meipaimv"),
//        haokan(Constant.haokanPath,"com.baidu.haokan"),
//        youku(Constant.youkuPath,"com.youku.phone"),
//        bobo(Constant.boboPath,"com.ttmv.bobo_client"),
//        baidu(Constant.baiduPath,"com.baidu.video"),
//        qiyi(Constant.qiyiPath,"com.qiyi.video"),
//        kuaishougif(Constant.kuaishougifPath,"com.kandian.shortgaoxiao"),
//        miaopai(Constant.miaopaiPath,"com.yixia.videoeditor"),
//        taobao(Constant.taobaoPath,"com.taobao.taobao"),
//        xianyu(Constant.xianyuPath,"com.taobao.idlefish"),
//        tengxun(Constant.tengxunPath,"com.tencent.qqlive")
//    }
    companion object{
        val instance by lazy { ShortVideoHelper() }
    }

    fun requestData(shortVideoCallBack: ShortVideoCallBack){
        Thread{
            shortVideoPath.toList().forEach {
                val list=getPathFile(it.path)
                BaseConstant.mainHandler.post {
                    shortVideoCallBack.onShortVideo(list,it)
                }
            }
            BaseConstant.mainHandler.post {
                shortVideoCallBack.onShortVideoComplete()
            }
        }.start()
    }

    interface ShortVideoCallBack{
        fun onShortVideo(files:List<File>,shortVideoData: ShortVideoPath)

        fun onShortVideoComplete()
    }

    private fun getPathFile(path: String):List<File>{
        val list=ArrayList<File>()
        scanFiles(path,list)
        return list
    }
    
    private fun scanFiles(path:String,list:ArrayList<File>){
        val file= File(path)
        if (!file.exists()){
            return
        }
        val files=file.listFiles()
        if (files.isNullOrEmpty()){
            return
        }
        files.forEach {
            if (it.isDirectory){
                scanFiles(it.absolutePath,list)
            }else{
                list.add(it)
            }
        }
    }
}