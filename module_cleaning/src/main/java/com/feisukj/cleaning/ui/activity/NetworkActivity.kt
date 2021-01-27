package com.feisukj.cleaning.ui.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.module_base.utils.SPUtil


import com.feisukj.cleaning.R
import com.feisukj.cleaning.view.particle.Particle
import com.feisukj.cleaning.view.particle.ParticlesField
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_network_clean.*
import java.text.NumberFormat
import java.util.*

class NetworkActivity :FragmentActivity(){
    companion object{
        private const val NETWORK_INTERVAL_TIME=3
        private const val NETWORK_INTERVAL_TIME_KEY="NETWORK_time"
        fun getIntent(context: Context):Intent{
            val preTime= SPUtil.getInstance().getLong(NETWORK_INTERVAL_TIME_KEY,0L)
            val intent=if ((System.currentTimeMillis()-preTime)/1000/60>=NETWORK_INTERVAL_TIME){
                SPUtil.getInstance().putLong(NETWORK_INTERVAL_TIME_KEY,System.currentTimeMillis())
                Intent(context,NetworkActivity::class.java)
            }else{
                NetworkCompleteActivity.getIntent(context,"刚刚加速过了。")
            }
            return intent
        }
    }

    private val animatorSet:AnimatorSet=AnimatorSet()
    private val listText= listOf(
            "正在分析网络带宽",
            "正在优化CDN加速",
            "正在优化QoS能力",
            "正在提升上网速度",
            "恭喜您,加速成功!"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_clean)

        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .init()

        val duration=3000L

        val random=Random()
        val numberFormat=NumberFormat.getInstance().also {
            it.maximumFractionDigits=1
        }
        val firstSpeed=random.nextFloat()+4
        val firstPlusSpeed=random.nextFloat()*200f+200

        val secondSpeed=firstSpeed+firstPlusSpeed/1000f
        val secondPlusSpeed=random.nextFloat()*200f+100

        val thirdSpeed=secondSpeed+secondPlusSpeed/1000f
        val thirdPlusSpeed=random.nextFloat()*200f+200

        val fourthSpeed=thirdSpeed+thirdPlusSpeed/1000f

        currentSpeed.text=numberFormat.format(firstSpeed)
        plusSpeed.text=numberFormat.format(firstPlusSpeed)
        currentSpeed.postDelayed({
            currentSpeed.text=numberFormat.format(secondSpeed)
            plusSpeed.text=numberFormat.format(secondPlusSpeed)
        },duration/3)
        currentSpeed.postDelayed({
            currentSpeed.text=numberFormat.format(thirdSpeed)
            plusSpeed.text=numberFormat.format(thirdPlusSpeed)
        },duration/2)


        val drawable=resources.getDrawable(R.mipmap.ic_particles)
        val bitmap= if (drawable is BitmapDrawable){
            drawable.bitmap
        }else{
            val bitmap=Bitmap.createBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
            val c= Canvas(bitmap)
            drawable.setBounds(0,0,bitmap.width,bitmap.height)
            drawable.draw(c)
            bitmap
        }
        val animator=getNetworkAnimator(bitmap,20)
        val textAnimator=getTextAnimator()
        animatorSet.playTogether(animator,textAnimator)
        animatorSet.duration=duration
        animatorSet.start()

        val networkDrawable=networkAnimation.drawable as? AnimationDrawable
        networkDrawable?.start()

        animatorSet.addListener(object :Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                networkDrawable?.stop()
                currentSpeed.text=numberFormat.format(fourthSpeed)
                if (!isFinishing){
                    startActivity(Intent(this@NetworkActivity,NetworkCompleteActivity::class.java))


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

    private fun getNetworkAnimator(bitmap: Bitmap, particleCount: Int): Animator {
        val particlesField= ParticlesField(this)
        findViewById<ViewGroup>(android.R.id.content).addView(particlesField)

        val particles=particlesField.particles
        val random= Random()
        for (i in 0 until particleCount){
            val particle= Particle(bitmap,random.nextInt(resources.displayMetrics.widthPixels),resources.displayMetrics.heightPixels+random.nextInt(resources.displayMetrics.heightPixels))
//            particle.scale=random.nextFloat()%3f
            particles.add(particle)
        }

        val animation= ValueAnimator.ofInt(0,-resources.displayMetrics.heightPixels*2)
        animation.interpolator= LinearInterpolator()
        animation.addUpdateListener { animator ->
            val v=animator.animatedValue
            if (v is Int){
                particles.forEach {
                    val dy=it.initY+v.toFloat()
                    it.dy=if (dy>0f) dy else resources.displayMetrics.heightPixels+dy%resources.displayMetrics.heightPixels
                }
                particlesField.invalidate()
            }
        }
        return animation
    }

    private fun getTextAnimator():Animator{
        val textAnimator=ValueAnimator.ofFloat(0f,1f)
        textAnimator.addUpdateListener {
            (it.animatedValue as? Float)?.apply {
                val text=listText[(this*(listText.size-1)).toInt()]
                if (text!=networkTips.text){
                    networkTips.text=text
                    Log.i("text","1111111111")
                }
            }
        }
        return textAnimator
    }
}