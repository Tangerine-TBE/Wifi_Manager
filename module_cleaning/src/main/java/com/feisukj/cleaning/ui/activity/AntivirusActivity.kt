package com.feisukj.cleaning.ui.activity

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.feisukj.cleaning.R
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_antivirus_clean.*

class AntivirusActivity:FragmentActivity() {
    private var valueAnimation:ValueAnimator?=null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_antivirus_clean)
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .init()

        val drawable=resources.getDrawable(R.mipmap.ic_antivirus_circle)
        var bitmap=if (drawable is BitmapDrawable){
            drawable.bitmap
        }else{
            val bitmap= Bitmap.createBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
            val c=Canvas(bitmap)
            drawable.setBounds(0,0,c.width,c.height)
            drawable.draw(c)
            bitmap
        }
        val w=resources.displayMetrics.density*20
        val matrix=Matrix()
        val s=w/bitmap.width
        matrix.setScale(s,s)
        bitmap=Bitmap.createBitmap(bitmap,0,0, bitmap.width, bitmap.height,matrix,false)

        antivirusProgress.progressEndBitmap=bitmap

        val text=listOf(
                "手机IP泄露检测中...",
                "通讯录泄露检测中...",
                "信息被偷窥检测中...",
                "支付环境安全检测中...",
                "摄像头防窥视检测中...",
                "麦克风防窃听检测中...",
                "相册安全保密检测中...",
                "聊天信息加密检测中...",

                "SSL安全检测中...",
                "ARP攻击检测中...",
                "WIFI加密检测中...",
                "DNS安全检测中...",

                "QoS质量检测中...",
                "防火墙服务检测中...",
                "IP保护检测中...",
                "网络防拦截检测中...",
                "已完成"
        )

        valueAnimation=ValueAnimator.ofFloat(0f,1f).also { valueAnimator ->
            valueAnimator.interpolator=LinearInterpolator()
            valueAnimator.duration=5000
            valueAnimator.addUpdateListener { animator ->
                (animator.animatedValue as? Float)?.let {
                    antivirusProgress.progress=it
                    antivirusProgress.invalidate()
                    progressText.text="${(it*100+0.5).toInt()}%"
                    currentItem.text=text[(it*(text.size-1)+0.5f).toInt()]
                }
            }
            valueAnimator.start()
        }
        valueAnimation?.addListener(object :Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                if (!isFinishing) {
                    startActivity(Intent(this@AntivirusActivity, AntivirusCompleteActivity::class.java))
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
        if (valueAnimation?.isPaused==true){
            valueAnimation?.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (valueAnimation?.isRunning==true){
            valueAnimation?.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        valueAnimation?.cancel()
    }
}