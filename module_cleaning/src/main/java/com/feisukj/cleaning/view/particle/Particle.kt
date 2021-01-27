package com.feisukj.cleaning.view.particle

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint

class Particle (private val bitmap: Bitmap, initX:Int, initY:Int){
    private val paint= Paint()
    private var matrix= Matrix()
    private val bitmapCenterX=bitmap.width.toFloat()/2
    private val bitmapCenterY=bitmap.height.toFloat()/2

    var initX:Int=initX
        private set
    var initY:Int=initY
        private set

    var rotation = 0f
    var scale=1f
    var dx=initX.toFloat()
    var dy=initY.toFloat()
    var alpha=255

    fun draw(c: Canvas){
        matrix.reset()
        matrix.postRotate(rotation, bitmapCenterX, bitmapCenterY)
        matrix.postScale(scale, scale, bitmapCenterX, bitmapCenterX)
        matrix.postTranslate(dx, dy)
        paint.alpha = alpha
        c.drawBitmap(bitmap,matrix,paint)
    }

}
