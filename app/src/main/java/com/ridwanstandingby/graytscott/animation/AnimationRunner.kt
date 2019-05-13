package com.ridwanstandingby.graytscott.animation

class AnimationRunner(private val animationRenderView: AnimationRenderView, private val animation: Animation<*, *, *>) {

    @Volatile
    private var t = System.nanoTime()
    private var canDraw = false

    private var renderTaskThread: Thread? = null

    private val animationRunnable = Runnable {
        while (canDraw) {
            if (animationRenderView.holder.surface.isValid) {
                update()
                render()
            }
        }
    }

    fun start() {
        canDraw = true
        renderTaskThread = Thread(animationRunnable, "Animation-Runner-Thread").also { it.start() }
    }

    fun stop() {
        canDraw = false
        while (renderTaskThread != null) {
            try {
                renderTaskThread?.join()
                renderTaskThread = null
                break
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun update() {
        val newT = System.nanoTime()
        animation.update((newT - t).toDouble() / 1000000000.0)
        t = newT
    }

    private fun render() {
        val canvas = animationRenderView.holder.lockCanvas()
        animation.renderer.updateCanvas(canvas)
        animationRenderView.holder.unlockCanvasAndPost(canvas)
    }
}
