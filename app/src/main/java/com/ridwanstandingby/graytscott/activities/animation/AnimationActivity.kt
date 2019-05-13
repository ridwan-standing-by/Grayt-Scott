package com.ridwanstandingby.graytscott.activities.animation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ridwanstandingby.graytscott.domain.GrayScottAnimation
import com.ridwanstandingby.graytscott.domain.GrayScottAnimationParameters
import com.ridwanstandingby.graytscott.domain.GrayScottAnimationRenderer
import com.ridwanstandingby.graytscott.animation.AnimationRenderView
import com.ridwanstandingby.graytscott.animation.AnimationRule
import com.ridwanstandingby.graytscott.domain.GrayScottAnimationInput

class AnimationActivity : AppCompatActivity() {

    private lateinit var animationRenderView: AnimationRenderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        animationRenderView = AnimationRenderView(
            this, AnimationRule(
                ::GrayScottAnimation,
                GrayScottAnimationParameters(),
                GrayScottAnimationRenderer(1080, 1920),
                GrayScottAnimationInput()
            )
        )
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
