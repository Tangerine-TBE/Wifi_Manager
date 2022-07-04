package com.example.module_tool.activity

import com.example.module_tool.R
import com.example.module_tool.base.BaseActivity
import com.example.module_tool.utils.GsonUtils
import com.example.module_base.utils.ToastUtil
import com.feisukj.bean.RangingData
import com.example.module_tool.dialog.RangingErrorDialog
import com.example.module_tool.dialog.RangingHelpDialog
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_ranging_result.*
import java.text.NumberFormat
import kotlin.math.tan

class RangingResultActivity : BaseActivity() {

    companion object{
        const val RESULT_KEY = "result_key"
    }
    private lateinit var bean: RangingData
    private var distance = 0f
    private var height = 0f

    override fun getLayoutId(): Int = R.layout.activity_ranging_result

    override fun initView() {
        ImmersionBar.with(this).statusBarColor("#FFFFFF").statusBarDarkFont(true).init()
        val str = intent.getStringExtra(RESULT_KEY)
        if (str.isNullOrEmpty()){
            ToastUtil.showShortToast("数据出错了")
            finish()
            return
        }
        bean = GsonUtils.parseObject(str,RangingData::class.java)
        if (bean == null){
            ToastUtil.showShortToast("数据出错了")
            finish()
            return
        }
        if (bean.isOnFloor)
            onFloor()
        else
            notOnFloor()

        back.setOnClickListener {
            finish()
        }
        btn.setOnClickListener {
            finish()
        }
        errorBtn.setOnClickListener {
            RangingErrorDialog(this, helpUnit = {
                RangingHelpDialog(this).show()
            }, againUnit = {
                finish()
            }).show()
        }
    }

    /**
     * 计算物体在地面的情况
     * */
    private fun onFloor(){
        val angle = 90f - bean.angle1
        distance = (bean.cameraHeight/ tan(Math.toRadians(angle.toDouble()))).toFloat()
        val angle2 = bean.angle2-90f
        val distance2 = bean.cameraHeight/ tan(Math.toRadians(angle2.toDouble()))
        height = (bean.cameraHeight/(distance2/(distance+distance2))).toFloat()
        show()
    }

    /**
     * 计算物体不在地面的情况
     * */
    private fun notOnFloor(){
        val angle = 90f - bean.angle1
        distance = (bean.cameraHeight/ tan(Math.toRadians(angle.toDouble()))).toFloat()
        val angle2 = bean.angle2-90f
        val distance2 = bean.cameraHeight/ tan(Math.toRadians(angle2.toDouble()))
        //底部高度
        val bottomHeight = (bean.cameraHeight/(distance2/(distance+distance2))).toFloat()
        val angle3 = bean.angle3-90f
        val distance3 = bean.cameraHeight/ tan(Math.toRadians(angle3.toDouble()))
        //总的高度
        val allHeight = (bean.cameraHeight/(distance3/(distance+distance3))).toFloat()
        height = allHeight - bottomHeight
        show()
    }

    private fun show(){
        val upAngle = if (bean.isOnFloor) bean.angle2 else bean.angle3
        val downAngle = if (bean.isOnFloor) bean.angle1 else bean.angle2
        val downStr = if (bean.isOnFloor) "俯视" else "俯视"
        tv_down.text = downStr

        result_height.text = getDistance(height)
        result_distance.text = getDistance(distance)
        result_tall.text = "${bean.cameraHeight} cm"
        result_up.text = "${numFormat(upAngle,1,10,1,2)} °"
        result_down.text = "${numFormat(downAngle,1,10,1,2)} °"

    }
    private fun numFormat(d: Float, minInt: Int, maxInt: Int, minF: Int, maxF: Int): String{
        val nf = NumberFormat.getNumberInstance()
        nf.maximumIntegerDigits = maxInt
        nf.minimumIntegerDigits = minInt
        nf.maximumFractionDigits = maxF
        nf.minimumFractionDigits = minF
        return nf.format(d)
    }

    private fun getDistance(d: Float): String{
        return if (d <= 100)
            numFormat(d,1,10,1,2)+"cm"
        else if(d >100 && d <= 100*1000)
            numFormat(d/100,1,10,1,2)+"m"
        else
            numFormat(d/100/1000,1,10,1,2)+"km"
    }
}