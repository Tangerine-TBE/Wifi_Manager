package com.feisukj.base.widget.Rx.loadingview.animation.interpolator

import android.view.animation.Interpolator

/**
 * @author tamsiree
 */
object Ease {
    @JvmStatic
    fun inOut(): Interpolator {
        return PathInterpolatorCompat.create(0.42f, 0f, 0.58f, 1f)
    }
}