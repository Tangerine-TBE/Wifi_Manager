package com.feisukj.cleaning.ui.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.module_base.utils.SPUtil

import com.feisukj.cleaning.R
import com.feisukj.cleaning.view.particle.Particle
import com.feisukj.cleaning.view.particle.ParticlesField
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_cooling_clean.*
import java.util.*

class CoolingActivity :FragmentActivity(){
    companion object{
        private const val COOLING_INTERVAL_TIME=3
        private const val COOLING_INTERVAL_TIME_KEY="cooling_time"
        fun getIntent(context: Context):Intent{
            val preTime= SPUtil.getInstance().getLong(COOLING_INTERVAL_TIME_KEY,0L)
            val intent=if ((System.currentTimeMillis()-preTime)/1000/60>=COOLING_INTERVAL_TIME){
                SPUtil.getInstance().putLong(COOLING_INTERVAL_TIME_KEY,System.currentTimeMillis())
                Intent(context,CoolingActivity::class.java)
            }else{
                CoolingCompleteActivity.getIntent(context,"刚刚优化过了。")
            }
            return intent
        }
    }

    private val animatorSet:AnimatorSet=AnimatorSet()
    private val progressTextContent= listOf(
            "正在优化系统占用资源",
            "正在优化CPU使用率",
            "正在优化手机屏幕",
            "己对手机进行降温"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cooling_clean)
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .init()

        val drawable=resources.getDrawable(R.mipmap.ic_snow)
        val bitmap= if (drawable is BitmapDrawable){
            drawable.bitmap
        }else{
            val bitmap=Bitmap.createBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
            val c=Canvas(bitmap)
            drawable.setBounds(0,0,bitmap.width,bitmap.height)
            drawable.draw(c)
            bitmap
        }

        animatorSet.duration=3000
        val snow=getSnowAnimator(bitmap, 100)
        val progress=getProgressAnimator()
        animatorSet.playTogether(snow,progress)
        animatorSet.start()
        animatorSet.addListener(object :Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
//                snowContainer.removeAllViews()
//                progressAnimation.visibility= View.GONE
                if (!isFinishing) {
                    startActivity(Intent(this@CoolingActivity, CoolingCompleteActivity::class.java))
                    finish()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        if (animatorSet.isPaused){
            animatorSet.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (animatorSet.isRunning){
            animatorSet.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        animatorSet.cancel()
    }

    private fun getProgressAnimator(): Animator{
        val valueAnimator=ValueAnimator.ofFloat(0f,1f)
        valueAnimator.interpolator=LinearInterpolator()
//        valueAnimator.duration=duration
        valueAnimator.addUpdateListener { animator ->
            (animator.animatedValue as? Float)?.let {
                progressAnimation.progress=it*progressTextContent.size%1f
                progressText.text="${(it*100).toInt()}%"
                currentItem.text=progressTextContent[(it*(progressTextContent.size-1)).toInt()]
                progressAnimation.invalidate()
            }
        }
        return valueAnimator
    }

    private fun getSnowAnimator(bitmap: Bitmap, particleCount: Int):Animator{
        val particlesField= ParticlesField(this)
        snowContainer.addView(particlesField)

        val particles=particlesField.particles
        val random= Random()
        for (i in 0 until particleCount){
            val particle= Particle(bitmap,random.nextInt(resources.displayMetrics.widthPixels),-random.nextInt(resources.displayMetrics.heightPixels))
            particle.scale=random.nextFloat()%3f
            particles.add(particle)
        }

        val animation= ValueAnimator.ofInt(0,resources.displayMetrics.heightPixels*2)
//        animation.duration=duration
        animation.interpolator=LinearInterpolator()
        animation.addUpdateListener { animator ->
            val v=animator.animatedValue
            if (v is Int){
                particles.forEach {
                    val dy=it.initY+v.toFloat()
                    it.dy=if (dy>0f) dy%resources.displayMetrics.heightPixels else dy
                }
                particlesField.invalidate()
            }
        }
        return animation
    }
}