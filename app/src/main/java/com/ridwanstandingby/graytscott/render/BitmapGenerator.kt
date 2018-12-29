package com.ridwanstandingby.graytscott.render

import android.graphics.Bitmap

class BitmapGenerator(private val animation: Animation<*>) {

    val bitmap: Bitmap

    init {
        val config = Bitmap.Config.ARGB_8888
        bitmap = Bitmap.createBitmap(animation.worldX, animation.worldY, config)
    }

    fun updateBitmap() {
        bitmap.setPixels(animation.pixelArray, 0, animation.worldX, 0, 0, animation.worldX, animation.worldY)
    }
}
