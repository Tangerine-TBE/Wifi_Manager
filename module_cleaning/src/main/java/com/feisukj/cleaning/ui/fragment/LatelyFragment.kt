package com.feisukj.cleaning.ui.fragment

import android.os.Bundle
import android.view.View
import com.feisukj.cleaning.bean.FileBean
import com.feisukj.cleaning.filevisit.FileR
import com.feisukj.cleaning.utils.Constant
import kotlinx.android.synthetic.main.fragment_abs_tab_clean.*
import java.io.File

class LatelyFragment:AbsTabFragment<FileBean>() {

    private var nullAction:(()->Unit)?=null

    enum class LatelyFileType(val label:String){
        All("全部"),Pictures("图片"),
        Media("媒体"),Doc("文档"),
        Zip("压缩包"),Apk("安装包"),
        Other("其他");
        companion object{
            fun getFileType(fileName:String):LatelyFileType{
                return when{
                    Constant.PICTURE_FORMAT.any {
                        fileName.endsWith(it,true)
                    }->{
                        Pictures
                    }
                    Constant.VIDEO_FORMAT.any {
                        fileName.endsWith(it,true)
                    }||Constant.MUSIC_FORMAT.any {
                        fileName.endsWith(it,true)
                    }->{
                        Media
                    }
                    Constant.DOC_FORMAT.any {
                        fileName.endsWith(it,true)
                    } -> {
                        Doc
                    }
                    Constant.ZIP_FORMAT.any {
                        fileName.endsWith(it,true)
                    } -> {
                        Zip
                    }
                    fileName.endsWith("apk",true) -> {
                        Apk
                    }
                    else -> {
                        Other
                    }
                }
            }
        }
    }
    companion object{
        private const val LATELY_TYPE_KEY="lately_type"
        fun getInstance(type:LatelyFileType):LatelyFragment{
            val f=LatelyFragment()
            f.arguments= Bundle().apply {
                this.putSerializable(LATELY_TYPE_KEY,type)
            }
            return f
        }
    }
    private val type by lazy {
        (arguments?.getSerializable(LATELY_TYPE_KEY) as? LatelyFileType)?:LatelyFileType.All
    }
    var onRemoveItem:((LatelyFileType,Collection<FileBean>)->Unit)?=null

    fun addItem(item: FileBean,type: LatelyFileType){
        if (this.type!=LatelyFileType.All&&type!=this.type){
            return
        }else{
            adapterAddItem(item)
        }
    }

    fun removeAllItem(item: Collection<FileBean>){
        if (isAdded){
            adapter.removeAllItem(item)
        }
    }

    override fun onRemoveAllItem(item: Collection<FileBean>) {
        onRemoveItem?.invoke(type,item)
    }

    override fun getPath(): List<String>? {
        return null
    }

    override fun onNextFile(file: FileR): FileBean? {
        return null
    }

    public override fun onComplete() {
        activity?.runOnUiThread {
            nullAction = {
                val lastItem=adapter.getData().lastOrNull()
                if (lastItem!=null){

                }else{
                    if (adapter?.getData()?.isEmpty()){
                        noData.visibility = View.VISIBLE
                    }else{
                        noData.visibility = View.GONE
                    }
                }
            }
            if (isAdded){
                nullAction?.invoke()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nullAction?.invoke()
    }

}