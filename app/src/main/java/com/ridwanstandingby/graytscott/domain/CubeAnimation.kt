package com.ridwanstandingby.graytscott.domain

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.ridwanstandingby.graytscott.animation.Animation
import com.ridwanstandingby.graytscott.animation.AnimationInput
import com.ridwanstandingby.graytscott.animation.AnimationParameters
import com.ridwanstandingby.graytscott.animation.AnimationRenderer
import com.ridwanstandingby.graytscott.math.*
import com.ridwanstandingby.graytscott.render.Camera
import kotlin.math.exp
import kotlin.math.log
import kotlin.random.Random

class CubeAnimation(parameters: CubeAnimationParameters, renderer: CubeAnimationRenderer, input: CubeAnimationInput) :
    Animation<CubeAnimationParameters, CubeAnimationRenderer, CubeAnimationInput>(parameters, renderer, input) {

    private val cubes = List(parameters.numberOfCubes) { parameters.generateRandomCube() }

    override fun update(dt: Double) {
        renderer.lines = mutableListOf()
        cubes.forEach {
            it.update(dt, renderer.camera)
            it.prepareRender(renderer)
        }
    }
}

data class Cube(
    var a: Double,
    var position: Vector3,
    var velocity: Vector3,
    var orientation: Vector3,
    var rotation: Double,
    var angularVelocity: Double
) {

    private var vertexMMM: FloatVector2 = FloatVector2(0f, 0f)
    private var vertexMMP: FloatVector2 = FloatVector2(0f, 0f)
    private var vertexMPM: FloatVector2 = FloatVector2(0f, 0f)
    private var vertexMPP: FloatVector2 = FloatVector2(0f, 0f)
    private var vertexPMM: FloatVector2 = FloatVector2(0f, 0f)
    private var vertexPMP: FloatVector2 = FloatVector2(0f, 0f)
    private var vertexPPM: FloatVector2 = FloatVector2(0f, 0f)
    private var vertexPPP: FloatVector2 = FloatVector2(0f, 0f)

    fun update(dt: Double, camera: Camera) {
        position += velocity * dt
        rotation += angularVelocity * dt
        handleBounce(camera)
    }

    private fun handleBounce(camera: Camera) {
        val vertices =
            listOf(vertexMMM, vertexMMP, vertexMPM, vertexMPP, vertexPMM, vertexPMP, vertexPPM, vertexPPP)
        velocity = when {
            vertices.any { it.x < 0 } && velocity dot camera.leftDirection > 0 ->
                velocity.reflect(camera.leftDirection)
            vertices.any { it.y < 0 } && velocity dot camera.upDirection > 0 ->
                velocity.reflect(camera.upDirection)
            vertices.any { it.x > camera.screenDimension.x } && velocity dot camera.rightDirection > 0 ->
                velocity.reflect(camera.rightDirection)
            vertices.any { it.y > camera.screenDimension.y } && velocity dot camera.downDirection > 0 ->
                velocity.reflect(camera.downDirection)
            else -> velocity
        }
    }

    fun prepareRender(renderer: CubeAnimationRenderer) {
        val transform = Quaternion(rotation, orientation)
        vertexMMM = renderer.camera.project(position + Vector3(-a, -a, -a).rotate(transform))
        vertexMMP = renderer.camera.project(position + Vector3(-a, -a, a).rotate(transform))
        vertexMPM = renderer.camera.project(position + Vector3(-a, a, -a).rotate(transform))
        vertexMPP = renderer.camera.project(position + Vector3(-a, a, a).rotate(transform))
        vertexPMM = renderer.camera.project(position + Vector3(a, -a, -a).rotate(transform))
        vertexPMP = renderer.camera.project(position + Vector3(a, -a, a).rotate(transform))
        vertexPPM = renderer.camera.project(position + Vector3(a, a, -a).rotate(transform))
        vertexPPP = renderer.camera.project(position + Vector3(a, a, a).rotate(transform))

        renderer.lines.add(
            floatArrayOf(
                vertexMMM.x, vertexMMM.y, vertexMMP.x, vertexMMP.y, vertexMMP.x, vertexMMP.y, vertexMPP.x, vertexMPP.y,
                vertexMPP.x, vertexMPP.y, vertexMPM.x, vertexMPM.y, vertexMPP.x, vertexMPP.y, vertexPPP.x, vertexPPP.y,
                vertexPPP.x, vertexPPP.y, vertexPMP.x, vertexPMP.y, vertexPMP.x, vertexPMP.y, vertexMMP.x, vertexMMP.y,
                vertexPMP.x, vertexPMP.y, vertexPMM.x, vertexPMM.y, vertexPMM.x, vertexPMM.y, vertexPPM.x, vertexPPM.y,
                vertexPPM.x, vertexPPM.y, vertexPPP.x, vertexPPP.y, vertexPPM.x, vertexPPM.y, vertexMPM.x, vertexMPM.y,
                vertexMPM.x, vertexMPM.y, vertexMMM.x, vertexMMM.y, vertexMMM.x, vertexMMM.y, vertexPMM.x, vertexPMM.y
            )
        )
    }
}

class CubeAnimationParameters(
    val numberOfCubes: Int = 25,
    private val cubeLengthMin: Double = 10.0,
    private val cubeLengthMax: Double = 200.0,
    private val cubeLengthSkew: Double = 0.04,
    private val velocityMin: Double = 10.0,
    private val velocityMax: Double = 400.0,
    private val angularVelocityMin: Double = 0.5,
    private val angularVelocityMax: Double = 5.0
) : AnimationParameters() {

    private fun randomSph2() =
        SphericalVector2(Random.nextDouble(0.0, Math.PI * 2), Random.nextDouble(0.0, Math.PI))

    fun generateRandomCube() = Cube(
        a = -log(
            exp(-cubeLengthMin * cubeLengthSkew) - Random.nextDouble() * (exp(-cubeLengthMin * cubeLengthSkew) - exp(
                -cubeLengthMax * cubeLengthSkew
            )), Math.E
        ) / cubeLengthSkew,
        position = Vector3(0.0, 0.0, 0.0),
        velocity = (randomSph2() * Random.nextDouble(velocityMin, velocityMax)).resolve(),
        orientation = randomSph2().resolve(),
        rotation = Random.nextDouble(0.0, Math.PI * 2),
        angularVelocity = Random.nextDouble(angularVelocityMin, angularVelocityMax)
    )
}

class CubeAnimationRenderer(screenDimension: IntVector2) : AnimationRenderer() {

    val camera = Camera(screenDimension, SphericalVector2(Math.PI / 4, Math.PI / 3))

    var lines = mutableListOf<FloatArray>()

    private val lineStyle = Paint().apply { color = Color.WHITE }

    override fun updateCanvas(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        lines.forEach {
            canvas.drawLines(it, lineStyle)
        }
    }
}

class CubeAnimationInput : AnimationInput()
