package com.ridwanstandingby.graytscott.render

import android.graphics.Canvas
import com.ridwanstandingby.graytscott.animation.AnimationRenderer

abstract class PixelArrayAnimationRenderer(val worldX: Int, val worldY: Int): AnimationRenderer() {

    val pixelArray: IntArray = IntArray(worldX * worldY)

    private val bitmapGenerator = BitmapGenerator(worldX, worldY)

    init {
        for (i in 0 until worldX * worldY) {
            pixelArray[i] = -0x1000000
        }
    }

    override fun updateCanvas(canvas: Canvas) {
        bitmapGenerator.updateBitmap(pixelArray)
        canvas.drawBitmap(bitmapGenerator.bitmap, 0f, 0f, null)
    }
}
