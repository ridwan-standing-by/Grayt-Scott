package com.ridwanstandingby.graytscott.render

import android.annotation.SuppressLint
import android.content.Context
import android.view.SurfaceView

@SuppressLint("ViewConstructor")
class AnimationRenderView(
    context: Context,
    private val animationRule: AnimationRule<*, *>
) : SurfaceView(context), RenderView {

    private lateinit var animationRenderTask: AnimationRenderTask
    private var renderTaskThread: Thread? = null

    override fun resume() {
        animationRenderTask = AnimationRenderTask(this, animationRule.create())
        animationRenderTask.canDraw = true

        renderTaskThread = Thread(animationRenderTask, "Animation-Render-Task-Thread").also { it.start() }
    }

    override fun pause() {
        animationRenderTask.canDraw = false
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
}
