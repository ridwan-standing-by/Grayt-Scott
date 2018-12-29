package com.ridwanstandingby.graytscott.activities.animation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ridwanstandingby.graytscott.domain.MandelbrotAnimation
import com.ridwanstandingby.graytscott.render.AnimationRenderView
import com.ridwanstandingby.graytscott.render.AnimationRule

class AnimationActivity : AppCompatActivity() {

    private lateinit var animationRenderView: AnimationRenderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        animationRenderView = AnimationRenderView(this, AnimationRule(::MandelbrotAnimation, MandelbrotAnimation.MandelbrotAnimationParameters(1000, 600)))
        setContentView(animationRenderView)
    }

    override fun onResume() {
        super.onResume()
        animationRenderView.resume()
    }

    override fun onPause() {
        super.onPause()
        animationRenderView.pause()
    }
}
