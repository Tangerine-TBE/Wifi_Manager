package com.feisukj.cleaning.ui.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.module_base.utils.SPUtil

import com.feisukj.cleaning.R
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_accelerate_clean.*

class AccelerateActivity: FragmentActivity() {
    companion object{
        private const val ACCELERATE_INTERVAL_TIME=3
        private const val ACCELERATE_INTERVAL_TIME_KEY="_Accelerate_time"
        fun getIntent(context: Context):Intent{
            val preTime= SPUtil.getInstance().getLong(ACCELERATE_INTERVAL_TIME_KEY,0L)
            val intent=if ((System.currentTimeMillis()-preTime)/1000/60>=ACCELERATE_INTERVAL_TIME){
                SPUtil.getInstance().putLong(ACCELERATE_INTERVAL_TIME_KEY,System.currentTimeMillis())
                Intent(context,AccelerateActivity::class.java)
            }else{
                Intent(context,SavePowerActivity::class.java)
            }
            return intent
        }
    }

    private val animatorSet=AnimatorSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accelerate_clean)
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .init()

        val bitmaps=getInstallAppIcon().take(25).map {
            if (it is BitmapDrawable){
                it.bitmap
            }else{
                val bitmap=Bitmap.createBitmap(it.intrinsicWidth,it.intrinsicHeight,Bitmap.Config.ARGB_8888)
                val c=Canvas(bitmap)
                it.setBounds(0,0,bitmap.width,bitmap.height)
                it.draw(c)
                bitmap
            }
        }

        scanAnimator.listBitmap=bitmaps
        animatorSet.playTogether(getScanAppAnimator())
        animatorSet.duration=3000
        animatorSet.start()
        animatorSet.addListener(object :Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
                
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (!isFinishing) {
                    progressTips.text="一键省电己完成"
                    startActivity(Intent(this@AccelerateActivity, SavePowerActivity::class.java))

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
        if (animatorSet.isRunning){
            animatorSet.cancel()
        }
    }

    private fun getScanAppAnimator(): Animator {
        val valueAnimator= ValueAnimator.ofFloat(0f,1f)
        valueAnimator.interpolator= LinearInterpolator()
        valueAnimator.addUpdateListener {
            (it.animatedValue as? Float)?.apply {
                scanAnimator.progress=this
                progressText.text=(this*100+0.5).toInt().toString()+"%"
                scanAnimator.invalidate()
            }
        }
        return valueAnimator
    }

    private fun getInstallAppIcon():List<Drawable>{
        return packageManager.getInstalledPackages(0)
                .asSequence()
                .filter {
                    (it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM)==0
                }
                .map {
                    it.applicationInfo.loadIcon(packageManager)
                }
                .toList()

    }
}