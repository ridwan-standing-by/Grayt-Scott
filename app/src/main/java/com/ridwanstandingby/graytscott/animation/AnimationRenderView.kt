package com.ridwanstandingby.graytscott.animation

import android.annotation.SuppressLint
import android.content.Context
import android.view.SurfaceView

@SuppressLint("ViewConstructor")
class AnimationRenderView(
    context: Context,
    private val animationRule: AnimationRule<*, *, *, *>
) : SurfaceView(context) {

    private lateinit var animationRunner: AnimationRunner

    fun resume() {
        animationRunner = AnimationRunner(this, animationRule.create())
        animationRunner.start()
    }

    fun pause() {
        animationRunner.stop()
    }
}
