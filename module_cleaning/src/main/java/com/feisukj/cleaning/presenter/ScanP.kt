package com.feisukj.cleaning.presenter



import com.example.module_base.cleanbase.BasePresenter
import com.feisukj.cleaning.file.*
import com.feisukj.cleaning.ui.activity.WeChatAndQQCleanActivity
import com.feisukj.cleaning.utils.Constant
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*

class ScanP private constructor(): BasePresenter<Any>() {
    companion object {
        val single:ScanP by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ScanP() }
    }
    fun requestData(path:List<String>?=null): Disposable?{
        val mView=mMvpView.get()
        val d:Disposable?
        d=when(mView){
            is WeChatAndQQCleanActivity ->{
                weAndQQActP(mView)
            }
            else-> null
        }
        return d
    }

    private fun weAndQQActP(act: WeChatAndQQCleanActivity):Disposable{
        var currentKey=-1
        val d=Observable.create(ObservableOnSubscribe<File>{ emitter ->
            if (act.flag== WeChatAndQQCleanActivity.WE_CHAT){
                val entries=act.map.entries.iterator()
                while (entries.hasNext()){
                    val entry=entries.next()
                    val key=entry.key
                    val value=entry.value
                    currentKey=key
                    if (key== WECHAT_GARBAGE_ID){//垃圾文件
                        Constant.weChatGarbage.forEach {
                            emitterFile(it,emitter,value.fileList)
                        }
                    }else if (key== WECHAT_CACHE_ID){
                        Constant.weChatCache.forEach {
                            emitterFile(it,emitter,value.fileList,fileNameLimit="_panel_enable")
                        }
                    }else if (key== WECHAT_FRIEND_ID){
                        Constant.weChatFriend.forEach {
                            emitterFile(it,emitter,value.fileList)
                        }
                    }else if (key== WECHAT_OTHER_ID){
                        Constant.weChatOther.forEach {
                            emitterFile(it,emitter,value.fileList,formats = listOf("jpg"))
                        }
                    }
                }
            }else if(act.flag== WeChatAndQQCleanActivity.QQ){
                val entries=act.map.entries.iterator()
                while (entries.hasNext()){
                    val entry=entries.next()
                    val key=entry.key
                    val value=entry.value
                    currentKey=key
                    weAndQQPaths.find { it.id==key }?.paths?.forEach {
                        emitterFile(it,emitter,value.fileList)
                    }
                }
            }
            emitter.onComplete()
        })
                .filter {
                    currentKey!=-1&&it.isFile
                }
                .map {
                    val bean=act.map[currentKey]
                    if (bean!=null){
                        bean.fileTotalSize+=it.length()
                    }
                    it.length()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    act.onNext(it)
                },{
                    act.onComplete()
                },{act.onComplete()})
        return d
    }
    /**
     * 发射数据
     */
    private var count=-1
    private fun emitterFile (strPath: String, emitter: ObservableEmitter<File>, list:ArrayList<File>, formats:List<String>?=null, isContainDir:Boolean=true, fileNameLimit:String?=null){
        if (count==0) return
        val file= File(strPath)
        if (!file.exists())
            return
        val files=file.listFiles()
        files?.forEach {
            if (it.isDirectory&&isContainDir){
                emitterFile(it.absolutePath,emitter,list,formats,fileNameLimit = fileNameLimit,isContainDir = isContainDir)

            }else{
                if (count!=-1&&count!=0){
                    val format=it.name.substringAfterLast(".")
                    if (it.isFile&&formats!=null&&formats.contains(format)) {
                        if (fileNameLimit==null||it.name.endsWith(fileNameLimit)) {
                            emitter.onNext(it)
                            count--
                        }
                    }
                }
                if (count==-1) {
                    if (it.isFile) {
                        if (fileNameLimit==null||it.name.endsWith(fileNameLimit)) {
                            list.add(it)
                            emitter.onNext(it)
                        }
                    }
                }
            }
        }
    }
}