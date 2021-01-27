package com.feisukj.cleaning.ui.activity

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.module_base.utils.SPUtil



import com.feisukj.cleaning.R
import com.feisukj.cleaning.view.particle.Particle
import com.feisukj.cleaning.view.particle.ParticlesField
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_clean_animator_clean.*
import java.util.*

class CleanAnimatorActivity:FragmentActivity() {
    private var totalGarbageSize=0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clean_animator_clean)
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .init()

        totalGarbageSize=intent.getLongExtra(CompleteActivity.SIZE_KEY,0L)

        val drawer=resources.getDrawable(R.mipmap.ic_particles)
        val bitmap=if (drawer is BitmapDrawable){
            drawer.bitmap
        }else{
            val bitmap= Bitmap.createBitmap(drawer.intrinsicWidth,drawer.intrinsicHeight,Bitmap.Config.ARGB_8888)
            val c= Canvas(bitmap)
            drawer.setBounds(0, 0, c.width, c.height)
            drawer.draw(c)
            bitmap
        }
        startParticlesAnimator(bitmap, 20)

        val translateAnimation=TranslateAnimation(0f,0f,0f,-resources.displayMetrics.heightPixels.toFloat()/2)
        translateAnimation.duration=1000
        translateAnimation.interpolator=AccelerateInterpolator()
        translateAnimation.startOffset=2000
        rocketView.animation=translateAnimation
        translateAnimation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
                
            }

            override fun onAnimationEnd(animation: Animation?) {
                rocketView.visibility= View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {
                
            }

        })


    }

    private fun startParticlesAnimator(bitmap: Bitmap, particleCount: Int){
        val particlesField=ParticlesField(this)
        findViewById<ViewGroup>(android.R.id.content).addView(particlesField)

        val particles=particlesField.particles
        val random= Random()
        for (i in 0 until particleCount){
            val particle=Particle(bitmap,random.nextInt(resources.displayMetrics.widthPixels),random.nextInt(resources.displayMetrics.heightPixels))
            particles.add(particle)
        }

        val animation= ValueAnimator.ofInt(0,-resources.displayMetrics.heightPixels)
        animation.repeatCount=3
        animation.duration=1000
        animation.interpolator=LinearInterpolator()
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
        animation.start()
        animation.addListener(object :Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
                
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (!isFinishing){
                    val intent=Intent(this@CleanAnimatorActivity,CompleteActivity::class.java)
                    intent.putExtra(CompleteActivity.SIZE_KEY,totalGarbageSize)
                    startActivity(intent)

                    finish()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
                
            }

            override fun onAnimationStart(animation: Animator?) {
                
            }

        })
    }
}