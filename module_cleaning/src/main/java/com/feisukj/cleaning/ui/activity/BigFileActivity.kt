package com.feisukj.cleaning.ui.activity

import android.app.Dialog
import android.graphics.Color
import android.os.Environment
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.cleanbase.BaseActivity2
import com.example.module_base.cleanbase.SureCancelDialog
import com.example.module_base.widget.LoadingDialog


import com.feisukj.cleaning.BuildConfig
import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.SortListAdapter
import com.feisukj.cleaning.bean.AllFileBean
import com.feisukj.cleaning.bean.AppBean
import com.feisukj.cleaning.bean.FileBean
import com.feisukj.cleaning.bean.ImageBean
import com.feisukj.cleaning.file.FileContainer
import com.feisukj.cleaning.file.FileManager
import com.feisukj.cleaning.file.FileType
import com.feisukj.cleaning.file.NextFileCallback
import com.feisukj.cleaning.utils.Constant
import com.feisukj.cleaning.utils.getSizeString
import com.feisukj.cleaning.utils.toAppOpenFile
import kotlinx.android.synthetic.main.act_bigfile_clean.*
import kotlinx.android.synthetic.main.act_bigfile_clean.barTitle
import kotlinx.android.synthetic.main.act_bigfile_clean.goBack
import kotlinx.android.synthetic.main.act_bigfile_clean.noData
import kotlinx.android.synthetic.main.act_bigfile_clean.recyclerView
import kotlinx.android.synthetic.main.act_music_clean.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.HashSet

class BigFileActivity : BaseActivity2(),NextFileCallback{
    companion object {
        const val BIG=423
    }
    private val deleteStack=HashSet<FileBean>()

    private var adapter: SortListAdapter<FileBean>?=null
    override fun getLayoutId()= R.layout.act_bigfile_clean

    override fun isActionBar(): Boolean = false

    override fun initView() {
        barTitle.text = resources.getText(R.string.bigFile)
        immersionBar.statusBarDarkFont(true).statusBarColor(android.R.color.transparent).init()
        adapter= SortListAdapter(R.layout.item_file_clean, emptyList(),Comparator<FileBean> { o1, o2 ->
            if (o1==null||o2==null){
                1
            }else{
                if (o1.fileSize>o2.fileSize){
                    -1
                }else{
                    1
                }
            }
        })
        FileManager.addCallBack(this)
        val layoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        recyclerView.adapter=adapter
        adapter?.bindView={vh,item->
            when(item){
                is AllFileBean->{
                    vh.setText(R.id.fileName,item.fileName)
                    vh.setImage(R.id.fileIcon,item.fileIcon)
                }
                is ImageBean ->{
                    vh.setText(R.id.fileName,item.fileName)
                    vh.setImage(R.id.fileIcon,item.absolutePath)
                }
                is AppBean ->{
                    vh.setText(R.id.fileName,item.label)
                    item.icon?.get().let {
                        if (it==null){
                            vh.setImage(R.id.fileIcon,android.R.mipmap.sym_def_app_icon)
                        }else{
                            vh.setImage(R.id.fileIcon,it)
                        }
                    }
                }
            }
            vh.setText(R.id.fileDate,"来自：${item.absolutePath.removePrefix(Environment.getExternalStorageDirectory().absolutePath).removeSuffix(item.fileName)}")
            vh.setImageSelect(R.id.fileCheck,item.isCheck)
            vh.setText(R.id.fileSize, getSizeString(item.fileSize))
            vh.getView<ImageView>(R.id.fileCheck).setOnClickListener { view ->
                view.isSelected=!view.isSelected
                val type=when{
                    Constant.PICTURE_FORMAT.any{item.fileName.endsWith(it,true)}->{
                        "图片文件"
                    }
                    Constant.VIDEO_FORMAT.any{item.fileName.endsWith(it,true)}->{
                        "视频文件"
                    }
                    Constant.MUSIC_FORMAT.any{item.fileName.endsWith(it,true)}->{
                        "音乐文件"
                    }
                    Constant.ZIP_FORMAT.any{item.fileName.endsWith(it,true)}->{
                        "压缩文件"
                    }
                    Constant.DOC_FORMAT.any{item.fileName.endsWith(it,true)}->{
                        if (item.fileName.endsWith("txt",true)){
                            "文本文件"
                        }else{
                            "文档文件"
                        }
                    }
                    item.fileName.endsWith(".apk",true)->{
                        "安装包文件"
                    }
                    item.fileName.endsWith(".log",true)->{
                        "日志文件"
                    }
                    else->{
                        "大文件类型未知文件"
                    }
                }.let {
                    SpannableString(it).apply {
                        setSpan(ForegroundColorSpan(Color.RED),0,length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
                val appName=when{
                    item.absolutePath.startsWith(Constant.WE_CHAT_ROOT_PATH,true)->{
                        "微信"
                    }
                    item.absolutePath.startsWith(Constant.WECHAT_DATA,true)->{
                        "微信"
                    }
                    item.absolutePath.startsWith(Constant.QQ_TENCENT,true)->{
                        "QQ"
                    }
                    item.absolutePath.startsWith(Constant.TENCENT_PATH,true)->{
                        "腾讯"
                    }
                    item.absolutePath.startsWith(Constant.DCIM,true)->{
                        "相册"
                    }
                    else->{
                        null
                    }
                }.let {
                    if (it!=null){
                        SpannableString("来自${it}的").apply {
                            setSpan(ForegroundColorSpan(Color.RED),0,length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                    }else{
                        ""
                    }
                }
                val contentText= SpannableStringBuilder()
                        .append("这个")
                        .append(appName)
                        .append(type)
                        .append("清理后将无法恢复,确定选中?")

//                val contentText="这个${appName}${type}清理后将无法恢复,确定选中?"

                val positiveAction={
                    val isChange=if (view.isSelected){
                        deleteStack.add(item)
                    }else{
                        deleteStack.remove(item)
                    }
                    if (isChange){
                        updateCleanText()
                    }
                }

                if (view.isSelected) {
                    showDialog(contentText, {
                        view.isSelected = false
                        it.dismiss()
                    }, {
                        positiveAction()
                        it.dismiss()
                    })
                }else{
                    positiveAction()
                }
            }
            vh.itemView.setOnClickListener {
                toAppOpenFile(this, File(item.absolutePath))
            }
        }
        if (!FileManager.isComplete&&FileContainer.getBigFileList().isNullOrEmpty()) {
            loadingDialog.show()
        }
    }

    override fun initListener() {
        clean.setOnClickListener {
            if (deleteStack.isEmpty()){
                return@setOnClickListener
            }
            val loadingDelete= LoadingDialog(this)
                    .setCancelable(false)
                    .setTitleText("正在删除...")
            loadingDelete.show()
            GlobalScope.launch {
                for (c in deleteStack) {
                    if (!BuildConfig.DEBUG){
                        File(c.absolutePath).delete()
                    }
                    runOnUiThread {
                        adapter?.removeItem(c)
                    }
                }
                runOnUiThread {
                    FileContainer.removeAllBigFile(deleteStack.toList())
                    deleteStack.clear()
                    loadingDelete.dismiss()
                    updateCleanText()
                }
            }
        }
        goBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FileManager.removeCallBack(this)
    }

    private fun showDialog(content:CharSequence, negativeAction:(Dialog)->Unit, positiveAction:(Dialog)->Unit){
        val d= SureCancelDialog(this)
                .setContent(content)
                .setOnNegativeClick {
                    negativeAction.invoke(it)
                }
                .setOnPositiveClick {
                    positiveAction.invoke(it)
                }
        d.setCancelable(false)
        d.show()
    }

    override fun onNextFile(type: FileType,fileBean: FileBean) {
        if (type!=FileType.bigFile)
            return
        runOnUiThread {
            if (loadingDialog.isShowing){
                loadingDialog.dismiss()
            }
            adapter?.addItem(fileBean)
        }
    }

    override fun onComplete(){
        if (loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
        adapter?.setData(FileContainer.getBigFileList())
        if (adapter?.getData()?.isEmpty()!!){
            noData.visibility = View.VISIBLE
        }else{
            noData.visibility = View.GONE
        }
        val f= FrameLayout(this).apply { this.layoutParams= ViewGroup.LayoutParams((resources.displayMetrics.density*300).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT) }
        adapter?.adView=f

    }

    private fun updateCleanText(){
        if(deleteStack.size>0){
            if (bottomButton.visibility!=View.VISIBLE){
                bottomButton.visibility=View.VISIBLE
            }
            clean.text=String.format(getString(R.string.cleanFielTip),deleteStack.size.toString())
        }else{
            if (bottomButton.visibility!=View.GONE){
                bottomButton.visibility=View.GONE
            }
        }
    }
}