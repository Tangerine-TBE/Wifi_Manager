package com.feisukj.base.widget.Rx.loadingview.style

import com.feisukj.base.widget.Rx.loadingview.sprite.Sprite
import com.feisukj.base.widget.Rx.loadingview.sprite.SpriteContainer

/**
 * @author tamsiree
 */
class MultiplePulse : SpriteContainer() {
    override fun onCreateChild(): Array<Sprite?>? {
        return arrayOf(
                Pulse(),
                Pulse(),
                Pulse())
    }

    override fun onChildCreated(vararg sprites: Sprite?) {
        for (i in sprites.indices) {
            sprites[i]?.setAnimationDelay(200 * (i + 1))
        }
    }
}