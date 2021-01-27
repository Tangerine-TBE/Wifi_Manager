package com.feisu.module_battery.ui.activity

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_base.cleanbase.BaseActivity2
import com.example.module_base.cleanbase.RecyclerViewHolder
import com.feisu.module_battery.R

import kotlinx.android.synthetic.main.activity_optimize.*
import java.util.*

class OptimizeActivity: BaseActivity2() {
    private var timer:Timer?=null

    override fun getLayoutId()= R.layout.activity_optimize

    override fun initView() {
        setContentText("耗电优化")
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
                    OptimizeData(label.toString(), icon)
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
                        startActivity(Intent(this@OptimizeActivity,SaveElectricityActivity::class.java))
//                        if (CompleteAdActivity.adViews.any { it!=null }){
//                            SPUtil.getInstance().getString(ADConstants.SAVE_ELECTRICITY_FINISHED)?.also {
//                                val t= GsonUtils.parseObject(it, TypeBean::class.java)
//                                val sf=t?.self_insert_screen
//                                if (sf?.status==true){
//                                    val intent=Intent(this@OptimizeActivity, CompleteAdActivity::class.java)
//                                    intent.putExtra(CompleteAdActivity.AD_STATUS_BEAN, GsonUtils.GsonString(sf))
//                                    startActivity(intent)
//                                }
//                            }
//                        }
                        this@OptimizeActivity.finish()
                    }
                }
            }
        },800,500)

        //提前获得广告view
//        SPUtil.getInstance().getString(ADConstants.SAVE_ELECTRICITY_FINISHED)?.also {
//            val t= GsonUtils.parseObject(it, TypeBean::class.java)
//            val sf=t?.self_insert_screen
//            if (sf?.status==true){
//                val str= GsonUtils.GsonString(sf)
//                for (i in 0 until 2){
//                    CompleteAdActivity.initAdView(this,str,object : AdViewCall {
//                        override fun onAdView(view: View) {
//                            if (CompleteAdActivity.adViews[0]==null){
//                                CompleteAdActivity.adViews[0]=view
//                            }else{
//                                CompleteAdActivity.adViews[1]=view
//                            }
//                        }
//                    })
//                }
//            }
//        }
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