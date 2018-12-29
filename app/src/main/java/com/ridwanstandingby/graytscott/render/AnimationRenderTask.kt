package com.ridwanstandingby.graytscott.render

import android.util.Log

class AnimationRenderTask(private val animationRenderView: AnimationRenderView, private val animation: Animation<*>): RenderTask {

    @Volatile private var t = System.currentTimeMillis() // DEBUG

    override var canDraw = false

    private val bitmapGenerator = BitmapGenerator(animation)

    override fun run() {
        while (canDraw) {
            if (!animationRenderView.holder.surface.isValid) {
                continue
            }

            val newT = System.currentTimeMillis()
            Log.d("###", "${newT - t}")

            animation.update((newT - t).toDouble()/1000.0)
            t = newT
            updateBitmapAndDraw()
        }
    }

    private fun updateBitmapAndDraw() {
        val canvas = animationRenderView.holder.lockCanvas()
        bitmapGenerator.updateBitmap()
        canvas.drawBitmap(bitmapGenerator.bitmap, 0f, 0f, null)
        animationRenderView.holder.unlockCanvasAndPost(canvas)
    }
}
