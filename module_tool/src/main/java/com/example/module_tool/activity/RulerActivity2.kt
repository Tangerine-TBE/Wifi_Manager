package com.example.module_tool.activity

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.module_tool.R
import com.example.module_tool.base.BaseAppCompatActivity
import com.example.module_tool.base.BaseConstant
import com.example.module_tool.widget.MyRulerView2
import com.example.module_tool.widget.PopupWindowView2
import com.gyf.barlibrary.ImmersionBar
import kotlinx.android.synthetic.main.activity_ruler2_cjy.*
import kotlin.math.abs

class RulerActivity2: BaseAppCompatActivity() {
    companion object{
        val proofreadResultKey="proofread_key"
    }
    private lateinit var immersionBar: ImmersionBar


    private enum class ProofreadActivity(val clazz: Class<out FragmentActivity>,val requestCode: Int,val title:String){
        ruler(RulerProofreadActivity::class.java,1, BaseConstant.application.getString(R.string.rulerJiaoDui)),
        bankCard(BankProofreadActivity::class.java,2,BaseConstant.application.getString(R.string.bankCardJiaoDui)),
        coin(CoinProofreadActivity::class.java,3,BaseConstant.application.getString(R.string.coinJiaoDui))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruler2_cjy)
        immersionBar=ImmersionBar.with(this)
        immersionBar.statusBarColor(R.color.them_cjy)
                .init()

        initClick2()
    }

    private fun initClick2(){
        proofread.setOnClickListener {
            val contentView=LayoutInflater.from(this).inflate(R.layout.window_proofread_cjy,null)
            val proofreadView= PopupWindowView2(contentView)
            proofreadView.isFocusable(true)
            proofreadView.findViewById<View>(R.id.rulerProofread).setOnClickListener {
                startActivityForResult(Intent(this,RulerProofreadActivity::class.java),1)
                proofreadView.dismiss()
            }
            proofreadView.findViewById<View>(R.id.bankProofread).setOnClickListener {
                startActivityForResult(Intent(this,BankProofreadActivity::class.java),2)
                proofreadView.dismiss()
            }
            proofreadView.findViewById<View>(R.id.coinProofread).setOnClickListener {
                startActivityForResult(Intent(this,CoinProofreadActivity::class.java),3)
                proofreadView.dismiss()
            }
            proofreadView.findViewById<View>(R.id.defJiaodui).setOnClickListener {
                MyRulerView2.LengthUnit.proofread(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,10f,resources.displayMetrics))
                rulerView2.invalidate()
                proofreadView.dismiss()
            }
            proofreadView.showAsDropDown(it)
        }

        unitSelect.setOnClickListener {
            val contentView=LayoutInflater.from(this).inflate(R.layout.window_choose_unit_cjy,null)
            val unitView= PopupWindowView2(contentView)
            unitView.isFocusable(true)
            unitView.findViewById<View>(R.id.unitMM).setOnClickListener {
                rulerView2.unit=MyRulerView2.LengthUnit.mm
                rulerView2.invalidate()
                unitView.dismiss()
            }
            unitView.findViewById<View>(R.id.unitCM).setOnClickListener {
                rulerView2.unit=MyRulerView2.LengthUnit.cm
                rulerView2.invalidate()
                unitView.dismiss()
            }
            unitView.findViewById<View>(R.id.unitIN).setOnClickListener {
                rulerView2.unit=MyRulerView2.LengthUnit.inch
                rulerView2.invalidate()
                unitView.dismiss()
            }
            unitView.findViewById<View>(R.id.unitIN_P).setOnClickListener {
                rulerView2.unit= MyRulerView2.LengthUnit.inchPer
                rulerView2.invalidate()
                unitView.dismiss()
            }
            unitView.showAsDropDown(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val isProofread=ProofreadActivity.values().map {
            it.requestCode
        }.contains(requestCode)
        if (isProofread){
            val cmInPx=data?.getFloatExtra(proofreadResultKey,0f)?:return
            if (cmInPx!=0f){
                MyRulerView2.LengthUnit.proofread(cmInPx)
                rulerView2.invalidate()
            }
        }
    }

    private var moveDistance=0f
    private var lastX=0f
    private var lastY=0f
    private val titleBarHeight by lazy { TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,70f,resources.displayMetrics)+0.5f }
    private var currentTitleBarHeight=0
    private val heightShowAnimator by lazy {
        ValueAnimator.ofInt(0,titleBarHeight.toInt()).apply {
            repeatCount=0
            repeatMode=ValueAnimator.RESTART
            duration=300
            addUpdateListener {
                val value=it.animatedValue
                if (value is Int) {
                    guidelineTitle.setGuidelineBegin(value)
                    currentTitleBarHeight=value
                }
            }
        }
    }
    private val heightHideAnimator by lazy {
        ValueAnimator.ofInt(titleBarHeight.toInt(),0).apply {
            repeatCount=0
            duration=300
            addUpdateListener {
                val value=it.animatedValue
                if (value is Int) {
                    guidelineTitle.setGuidelineBegin(value)
                    currentTitleBarHeight=value
                }
            }
        }
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?:return super.onTouchEvent(event)
        val x=event.x
        val y=event.y
        when (event.action){
            MotionEvent.ACTION_DOWN->{
                moveDistance=0f
                lastX=x
                lastY=y
            }
            MotionEvent.ACTION_MOVE->{
                moveDistance+= abs(x-lastX)
                moveDistance+= abs(y-lastY)

                lastX=x
                lastY=y
            }
            MotionEvent.ACTION_UP->{
                if (moveDistance<10){
                    if (currentTitleBarHeight==0) {
                        heightShowAnimator.start()
                    }else if(currentTitleBarHeight==titleBarHeight.toInt()){
                        heightHideAnimator.start()
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }
}