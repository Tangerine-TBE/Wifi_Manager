package com.feisukj.cleaning.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.cleanbase.BaseMvpActivity

import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.ItemOnClick
import com.feisukj.cleaning.adapter.WeAndQQAdapter
import com.feisukj.cleaning.bean.WeChatAndQQItemBean
import com.feisukj.cleaning.file.*
import com.feisukj.cleaning.presenter.ScanP
import com.feisukj.cleaning.utils.formatFileSize
import com.feisukj.cleaning.utils.getSizeString
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.act_wechat_and_qq_clean.*
import java.text.DecimalFormat

class WeChatAndQQCleanActivity : BaseMvpActivity<WeChatAndQQCleanActivity, ScanP>(), ItemOnClick<WeChatAndQQItemBean> {
    companion object {
        const val KEY="key"
        const val WE_CHAT="we_chat"
        const val QQ="qq"
    }

    val weSet= setOf(WECHAT_CACHE_ID,WECHAT_FRIEND_ID,WECHAT_OTHER_ID)

    val qqSet= setOf(QQ_HEAD_ID,QQ_QZONE_ID)
    @SuppressLint("UseSparseArrays")
    val map=HashMap<Int, WeChatAndQQItemBean>()
    var flag= WE_CHAT
    private var adapter:WeAndQQAdapter?=null
    private var totalSize=0L
        set(value) {
            field=value
            if (decimalFormat==null&&value.shr(10)>0){
                decimalFormat=DecimalFormat("#.00")
            }
        }
    private var decimalFormat:DecimalFormat?=null
    private var disposable: Disposable?=null
    private val cleanSetKey=HashSet<Int>()
    private var cleanSize=0L
    private var isComplete=false
    override fun getLayoutId()= R.layout.act_wechat_and_qq_clean

    override fun createPresenter()= ScanP.single

    override fun initView() {
        super.initView()
        mImmersionBar.statusBarColor(android.R.color.transparent).init()
        intent.getStringExtra(KEY)?.let {
            flag=it
        }
        if (flag== WE_CHAT){
            myTitle.setText(R.string.WeChatSP)
            val item1= WeChatAndQQItemBean()
            item1.key=WECHAT_GARBAGE_ID
            item1.title=resources.getString(R.string.garbageFile)
            item1.icon=R.mipmap.icon_ljwj_qq

            val item2= WeChatAndQQItemBean()
            item2.key=WECHAT_CACHE_ID
            item2.title=resources.getString(R.string.cacheEmoji)
            item2.icon=R.mipmap.icon_txhc

            val item3= WeChatAndQQItemBean()
            item3.key=WECHAT_FRIEND_ID
            item3.title=resources.getString(R.string.friendsCache)
            item3.icon=R.mipmap.icon_pyqhc

            val item4= WeChatAndQQItemBean()
            item4.key=WECHAT_OTHER_ID
            item4.title=resources.getString(R.string.otherCache)
            item4.icon=R.mipmap.icon_qthc

            item1.isSelect=true
            item2.isSelect=true
            item3.isSelect=true
            item4.isSelect=true
            map[WECHAT_GARBAGE_ID]=item1
            map[WECHAT_CACHE_ID]=item2
            map[WECHAT_FRIEND_ID]=item3
            map[WECHAT_OTHER_ID]=item4
            adapter=WeAndQQAdapter(this, listOf(item1,item2,item3,item4))
        }else if (flag== QQ){
            myTitle.text=resources.getString(R.string.QQSP)

            val item1= WeChatAndQQItemBean()
            item1.key=QQ_CACHE_ID
            item1.title=resources.getString(R.string.tempFile)
            item1.icon=R.mipmap.icon_lshc

            val item2= WeChatAndQQItemBean()
            item2.key=QQ_HEAD_ID
            item2.title=resources.getString(R.string.headCache)
            item2.icon=R.mipmap.icon_txhc

            val item3= WeChatAndQQItemBean()
            item3.key=QQ_QZONE_ID
            item3.title=resources.getString(R.string.picCache)
            item3.icon=R.mipmap.icon_kjhc

            val item4= WeChatAndQQItemBean()
            item4.key=QQ_GARBAGE_ID
            item4.title=resources.getString(R.string.garbageFile)
            item4.icon=R.mipmap.icon_ljwj_qq

            item1.isSelect=true
            item2.isSelect=true
            item3.isSelect=true
            item4.isSelect=true
            map[QQ_GARBAGE_ID]=item1
            map[QQ_CACHE_ID]=item2
            map[QQ_HEAD_ID]=item3
            map[QQ_QZONE_ID]=item4
            adapter=WeAndQQAdapter(this, listOf(item1,item2,item3,item4))
        }
        val layoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        adapter?.itemOnClick=this
        recyclerView.adapter=adapter
        initClick()
        disposable=mPresenter.requestData()

    }

    private fun initClick(){
        back.setOnClickListener { finish() }
        clean.setOnClickListener {view->
            if (!isComplete){
                Toast.makeText(this,R.string.scanning,Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            cleanSetKey.forEach {key->
                Thread(){
                    kotlin.run {
                        val list=map[key]?.fileList?.toList()
                        list?.forEach {
                            if (it.exists()){
                                it.delete()
                            }
                        }
                    }
                }.start()
            }
            if (cleanSize!=0L) {
                val intent = Intent(this, CompleteActivity::class.java)
                intent.putExtra(CompleteActivity.SIZE_KEY, cleanSize)
                startActivity(intent)
                finish()
            }
        }
        shendu.setOnClickListener {
            val f=if (flag== WE_CHAT){
                QQAndWeFileActivity.WE
            }else{
                QQAndWeFileActivity.QQ
            }
            val intent=Intent(this,QQAndWeFileActivity::class.java)
            intent.putExtra(QQAndWeFileActivity.KEY,f)
            startActivity(intent)
        }
    }

    fun onNext(size:Long){
        if (size!=0L){
            totalSize+=size
            formatFileSize(this,totalSize).let {
                unit.text=it.second
                textCount.text=it.first
            }
            adapter?.notifyDataSetChanged()
        }
    }

    fun onComplete(){
        isComplete=true
        disposable?.dispose()
        map.keys.forEach {
            addAndRemove(true,it)
        }
    }

    override fun onCheckItem(t: WeChatAndQQItemBean, isCheck: Boolean) {
        super.onCheckItem(t, isCheck)
        addAndRemove(isCheck, t.key)
    }

    override fun onMyClick(view: View, t: WeChatAndQQItemBean) {
        super.onMyClick(view, t)
        if (t.key !in qqSet&&t.key !in weSet)
            return
        val intent=Intent(this,WeAndQQManagerActivity::class.java)
        intent.putExtra(WeAndQQManagerActivity.KEY,t.key)
        startActivity(intent)
    }

    private fun addAndRemove(isAdd:Boolean,key:Int){
        val size=map[key]?.fileTotalSize?:0
        if (isAdd){
            cleanSetKey.add(key)
            cleanSize+=size
        }else{
            cleanSize-=size
            cleanSetKey.remove(key)
        }
        val r=resources.getString(R.string.cleanVar)
        clean.text=r.format(getSizeString(cleanSize,2))
    }
}