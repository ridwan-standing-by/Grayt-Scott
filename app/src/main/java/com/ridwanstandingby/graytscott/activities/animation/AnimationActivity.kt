package com.ridwanstandingby.graytscott.activities.animation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import com.ridwanstandingby.graytscott.animation.AnimationRenderView
import com.ridwanstandingby.graytscott.animation.AnimationRule
import com.ridwanstandingby.graytscott.domain.CubeAnimation
import com.ridwanstandingby.graytscott.domain.CubeAnimationInput
import com.ridwanstandingby.graytscott.domain.CubeAnimationParameters
import com.ridwanstandingby.graytscott.domain.CubeAnimationRenderer
import com.ridwanstandingby.graytscott.math.IntVector2
import com.ridwanstandingby.graytscott.sensor.LinearAccelerator
import com.ridwanstandingby.graytscott.sensor.RotationDetector

class AnimationActivity : AppCompatActivity() {

    private lateinit var animationRenderView: AnimationRenderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        hideSystemUI()
        val metrics = DisplayMetrics().also { windowManager.defaultDisplay.getRealMetrics(it) }
        val screenSize = IntVector2(metrics.widthPixels, metrics.heightPixels)

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        animationRenderView = AnimationRenderView(
            this, AnimationRule(
                ::CubeAnimation,
                CubeAnimationParameters(),
                CubeAnimationRenderer(screenSize),
                CubeAnimationInput(RotationDetector(sensorManager, rotationSensor))
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

    private fun hideSystemUI() {
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}
