package com.ridwanstandingby.graytscott.animation

import android.graphics.Canvas

abstract class AnimationRenderer(val worldX: Int, val worldY: Int) {

    abstract fun updateCanvas(canvas: Canvas)
}
