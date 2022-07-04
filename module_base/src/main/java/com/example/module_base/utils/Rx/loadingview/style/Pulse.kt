package com.feisukj.base.widget.Rx.loadingview.style

import android.animation.ValueAnimator
import com.feisukj.base.widget.Rx.loadingview.animation.SpriteAnimatorBuilder
import com.feisukj.base.widget.Rx.loadingview.sprite.CircleSprite

/**
 * @author tamsiree
 */
class Pulse : CircleSprite() {
    override fun onCreateAnimation(): ValueAnimator? {
        val fractions = floatArrayOf(0f, 1f)
        return SpriteAnimatorBuilder(this).scale(fractions, 0f, 1f).alpha(fractions, 255, 0).duration(1000).easeInOut(*fractions)
                .build()
    }

    init {
        setScale(0f)
    }
}