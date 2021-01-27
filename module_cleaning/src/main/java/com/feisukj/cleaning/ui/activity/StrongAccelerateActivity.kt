package com.feisukj.cleaning.ui.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_base.cleanbase.BaseActivity2
import com.example.module_base.cleanbase.RecyclerViewHolder
import com.example.module_base.utils.SPUtil

import com.feisukj.cleaning.R
import kotlinx.android.synthetic.main.activity_strong_accelerate.*
import java.util.*

class StrongAccelerateActivity: BaseActivity2() {
    companion object{
        private const val ACCELERATE_INTERVAL_TIME=3
        private const val ACCELERATE_INTERVAL_TIME_KEY="ACCELERATE_time"
        fun getIntent(context: Context):Intent{
            val preTime= SPUtil.getInstance().getLong(ACCELERATE_INTERVAL_TIME_KEY,0L)
            val intent=if ((System.currentTimeMillis()-preTime)/1000/60>=ACCELERATE_INTERVAL_TIME){
                SPUtil.getInstance().putLong(ACCELERATE_INTERVAL_TIME_KEY,System.currentTimeMillis())
                Intent(context,StrongAccelerateActivity::class.java)
            }else{
                StrongAccelerateCompleteActivity.getIntent(context,"刚刚加速过了。")
            }
            return intent
        }
    }

    private var timer:Timer?=null

    override fun getLayoutId()= R.layout.activity_strong_accelerate

    override fun initView() {
        setContentText("强力加速")

        var n=0
        val data=packageManager.getInstalledPackages(0)
                .filter {
                    if (n>=10){
                        return@filter false
                    }
                    (it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM)==0&&n++<10
                }
                .map {
                    val label=it.applicationInfo.loadLabel(packageManager)
                    val icon=it.applicationInfo.loadIcon(packageManager)
                    OptimizeData(label.toString(),icon)
                }
                .take(15)

        optimizeRecyclerView.layoutManager= LinearLayoutManager(this)
        val adapter=OptimizeAdapter(data)
        optimizeRecyclerView.adapter=adapter
        timer=Timer()
        timer?.schedule(object :TimerTask(){
            override fun run() {
                runOnUiThread {
                    if (!adapter.removeFirst()){
                        this.cancel()
                        startActivity(Intent(this@StrongAccelerateActivity,StrongAccelerateCompleteActivity::class.java))
                        this@StrongAccelerateActivity.finish()
                    }
                }
            }
        },800,500)
    }

    override fun initListener() {
        
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    private class OptimizeData(val label:String,val icon: Drawable){
    }

    private class OptimizeAdapter(listDate:List<OptimizeData>): RecyclerView.Adapter<RecyclerViewHolder>(){
        private val listDate=LinkedList(listDate)

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerViewHolder {
            val itemView=LayoutInflater.from(p0.context).inflate(R.layout.item_optimize,p0,false)
            return RecyclerViewHolder(itemView)
        }

        override fun getItemCount()=listDate.size

        override fun onBindViewHolder(viewHolder: RecyclerViewHolder, position: Int) {
            viewHolder.setText(R.id.optimizeTitle,listDate[position].label)
            viewHolder.getView<ImageView>(R.id.optimizeIcon).setImageDrawable(listDate[position].icon)
        }

        fun removeFirst():Boolean{
            return if (listDate.isEmpty()){
                false
            }else{
                listDate.removeFirst()
                notifyItemRemoved(0)
                true
            }
        }
    }
}