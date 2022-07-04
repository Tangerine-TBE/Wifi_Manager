package com.feisukj.cleaning.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Environment
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.module_base.cleanbase.BaseActivity

import com.feisukj.cleaning.R
import com.feisukj.cleaning.adapter.FileAdapter
import com.feisukj.cleaning.adapter.ItemOnClick
import com.feisukj.cleaning.bean.AllFileBean
import com.feisukj.cleaning.filevisit.FileR
import com.feisukj.cleaning.utils.*
import com.feisukj.cleaning.view.spinner.MySpinner
import kotlinx.android.synthetic.main.act_allfile_clean.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


/**
 * 该activity作为浏览文件和复制文件时使用
 */
class AllFileActivity : BaseActivity(),ItemOnClick<AllFileBean>{
    companion object {
        private var copyOrDeleteStack=HashSet<String>()
        const val TO_PATH="to_path"
    }
    private val COPY_FILE="copy_file"//通过intent传了该值来，表示是复制文件
    private var isCopyAct:Boolean?=null
    private val stackPath=Stack<String>()
    private val pathTip=StringBuilder()
    private var files: FileR?=null
    private val listData=ArrayList<AllFileBean>()
    private var adapter:FileAdapter?=null
    override fun getLayoutId()= R.layout.act_allfile_clean
    override fun initView() {
        val path_=intent.getStringExtra(TO_PATH)
        var path=Environment.getExternalStorageDirectory().absolutePath
        if (intent.getStringExtra(COPY_FILE)==COPY_FILE){
            isCopyAct=true
            barTitle.setText(R.string.copyTo)
            copyActButton.visibility=View.VISIBLE
        }else{
            if(path_!=null){
                path=path_
            }
            barTitle.setText(R.string.allFile)
        }
        barBack.setImageResource(R.drawable.icon_base_back)
        stackPath.push(path)
        pathTip.append(resources.getString(R.string.storageD))
        initClick()
    }
    private var spinner: MySpinner?=null
    private val spinnerData:List<String> by lazy { listOf(R.string.dateUnOrder,R.string.dateOrder).map { resources.getString(it) }}
    private fun initClick(){
        order.setOnClickListener { view ->
            if (spinner==null){
                if (view is TextView) {
                    spinner = MySpinner(this, view, spinnerData)
                    spinner?.setOnMyClickItem(MySpinner.OnMyClickItem {
                        if (it<spinnerData.size&&it>=0){
                            order(it)
                            adapter?.notifyDataSetChanged()
                        }
                    })
                }
            }
            spinner?.showPopupWindow()
        }
        barBack.setOnClickListener {
            finish()
        }
        clean.setOnClickListener {
            //执行删除任务
            val thread=Thread{
                kotlin.run {
                    var count=0
                    var fail=0
                    while (!copyOrDeleteStack.isNullOrEmpty()){
                        count++
                        val str=copyOrDeleteStack.first()
                        if(!deleteDirectory(copyOrDeleteStack.first()))
                            fail++
                        copyOrDeleteStack.remove(str)
                    }
                    runOnUiThread{
                        loadingDialog?.dismiss()
                        listData.forEach {
                            it.isCheck=false
                        }
                        upData()
                        upText()
                        val s=String.format(resources.getString(R.string.cleanResult),(count-fail).toString(),fail.toString())
                        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
                    }
                }
            }
            val dialog=AlertDialog.Builder(this)
            dialog.setTitle(R.string.deleteFile)
            dialog.setMessage(String.format(resources.getString(R.string.askDelete),copyOrDeleteStack.size.toString()))
            dialog.setNegativeButton(R.string.no) { _dialog, which -> }
            dialog.setPositiveButton(R.string.yes){_dialog,which->
                if(loadingDialog?.isShowing!=true) {
                    thread.start()
                    loadingDialog?.setCancelable(false)
                    loadingDialog?.setTitleText("正在删除...")
                    loadingDialog?.show()
                }
            }
            dialog.show()
        }
        copy.setOnClickListener {
            val intent=Intent(this, AllFileActivity::class.java)
            intent.putExtra(COPY_FILE,COPY_FILE)
            startActivity(intent)
        }

        paste.setOnClickListener {
            val toFilePath=StringBuffer()//复制到的目标路径
            stackPath.forEach {
                toFilePath.append(it)
            }
            val fromFilePath=StringBuffer()
            copyOrDeleteStack.forEach {
                fromFilePath.append(it)
            }
            val fromFile=File(fromFilePath.toString())
            //执行粘贴任务
            val thread=Thread{
                kotlin.run {
                    val toFile= File(toFilePath.toString())
                    toFilePath.setLength(fromFilePath.length)
                    if (fromFile.isDirectory&&fromFilePath.toString()==toFilePath.toString()){
                        runOnUiThread {
                            Toast.makeText(this,R.string.copyFail,Toast.LENGTH_SHORT).show()
                        }
                    }else {
                        runOnUiThread {
                            runCopy()
                        }
                        copyFile(fromFile, toFile)
                        runOnUiThread {
                            loadingDialog?.dismiss()
                            copyComplete()
                        }
                        copyOrDeleteStack.clear()
                    }

                }
            }
            if (fromFile.isFile){
                val newFile=File("$toFilePath/${fromFile.name}")
                if (newFile.exists()){
                    AlertDialog.Builder(this)
                            .setTitle(R.string.cover)
                            .setMessage(R.string.existenceFile)
                            .setNegativeButton(R.string.no){dialog, which ->  }
                            .setPositiveButton(R.string.yes){dialog, which ->  thread.start()}
                            .show()
                }else{
                    if (loadingDialog?.isShowing!=true) {
                        loadingDialog?.setTitleText("正在删除...")
                        loadingDialog?.setCancelable(false)
                        loadingDialog?.show()
                        thread.start()
                    }
                }
            }
        }
        cancel.setOnClickListener {
            finish()
        }
    }

    /**
     * 正在进行复制粘贴任务
     */
    private fun runCopy(){
        copyActButton.visibility=View.GONE
    }

    /**
     * 复制粘贴任务执行完成
     */
    private fun copyComplete(){
        Toast.makeText(this,R.string.copySuccess,Toast.LENGTH_SHORT).show()
        finish()
    }
    override fun initData() {
        super.initData()
        upData()
        pathText.text=pathTip.toString()
        adapter = if (isCopyAct==true)
            FileAdapter(this,listData,0)
        else
            FileAdapter(this,listData)
        adapter?.itemOnClick=this
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.adapter=adapter
    }
    private fun upData(){
        val path=StringBuffer()
        stackPath.forEach {
            path.append(it)
        }
        files= FileR(path.toString())
        listData.clear()
        files?.listFiles()?.forEach { file ->
            if (isCopyAct != true||file.isDirectory) {
                val fileBean = AllFileBean(file)
                if (file.isDirectory) {
                    fileBean.dirCount = file.listFiles()?.size?:0
                }
                listData.add(fileBean)
            }
        }
        order()
        adapter?.notifyDataSetChanged()
    }
    private fun order(order:Int=0){
        if (order==1){
            //顺序
            listData.sortBy {
                if (it.isFile){
                    it.fileLastModified
                }else{
                    Long.MIN_VALUE+it.fileLastModified
                }
            }
        }else{
            //到序
            listData.sortBy {
                if (it.isFile){
                    Long.MAX_VALUE-it.fileLastModified
                }else{
                    -it.fileLastModified
                }
            }
        }
    }
    private fun pushPath(fileName:String){
        if (isCopyAct!=true){
            removeAllTask()
        }
        stackPath.push("/$fileName")
        pathTip.append("/$fileName")
        pathText.text=pathTip.toString()
        upData()
    }
    private fun popPath(){
        if (isCopyAct!=true){
            removeAllTask()
        }
        if (stackPath.size<=1){
            finish()
        }else{
            val path=stackPath.pop()
            pathTip.setLength(pathTip.length-path.length)
            pathText.text=pathTip.toString()
            upData()
        }
    }

    override fun onBackPressed() {
        if (stackPath.size>1){
            popPath()
        }else {
            super.onBackPressed()
        }
    }

    override fun onMyClick(view: View, t: AllFileBean) {
        super.onMyClick(view, t)
        if (isCopyAct!=true&&t.isFile){//启动意图
            toAppOpenFile(this,File(t.absolutePath))
        }else{
            pushPath(t.fileName)
        }
    }

    override fun onCheckItem(t: AllFileBean, isCheck: Boolean) {
        super.onCheckItem(t, isCheck)
        if (isCheck){
            addTask(t.absolutePath!!)
        }else{
            removeTask(t.absolutePath!!)
        }
    }
    private fun addTask(absolutePath:String){
        copyOrDeleteStack.add(absolutePath)
        bottomButton.visibility=View.VISIBLE
        copy.text=String.format(resources.getString(R.string.copyFileTip), copyOrDeleteStack.size.toString())
        clean.text=String.format(resources.getString(R.string.cleanFielTip), copyOrDeleteStack.size.toString())
    }
    private fun removeTask(absolutePath: String){
        copyOrDeleteStack.remove(absolutePath)
        upText()
    }
    private fun upText(){
        if (copyOrDeleteStack.isNullOrEmpty()){
            bottomButton.visibility=View.GONE
        }else{
            copy.text=String.format(resources.getString(R.string.copyFileTip), copyOrDeleteStack.size.toString())
            clean.text=String.format(resources.getString(R.string.cleanFielTip), copyOrDeleteStack.size.toString())
        }
        adapter?.notifyDataSetChanged()
    }
    private fun removeAllTask(){
        copyOrDeleteStack.clear()
        bottomButton.visibility=View.GONE
    }
}