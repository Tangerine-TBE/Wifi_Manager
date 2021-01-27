package com.feisukj.cleaning.view.particle

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class ParticlesField @JvmOverloads constructor(context: Context, attrs: AttributeSet?=null, defSty:Int=0):View(context,attrs,defSty){
    val particles=ArrayList<Particle>()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?:return
        synchronized(particles){
            particles.forEach {
                it.draw(canvas)
            }
        }
    }
}
