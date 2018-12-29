package com.ridwanstandingby.graytscott.domain

import com.ridwanstandingby.graytscott.render.Animation
import com.ridwanstandingby.graytscott.render.AnimationParameters

class MandelbrotAnimation(parameters: MandelbrotAnimationParameters) :
    Animation<MandelbrotAnimation.MandelbrotAnimationParameters>(parameters) {

    private val pointRe: Double = parameters.pointRe
    private val pointIm: Double = parameters.pointIm
    private val thresholdBase: Int = parameters.thresholdBase
    private val thresholdTimeScale: Double = parameters.thresholdTimeScale

    private val speed: Double = parameters.speed
    private val maxTime: Double = parameters.maxTime
    private val zoomBase: Double = parameters.zoomBase

    private val colour: IntArray = parameters.colour
    var time = 0.0

    override fun update(dt: Double) {

        if (time > maxTime) {
            time = 0.0
        }

        val aspectRatio = worldX.toDouble() / worldY.toDouble()
        val zoom = zoomBase * java.lang.Math.exp(time * speed)
        val xMin = pointRe - aspectRatio / zoom
        val xMax = pointRe + aspectRatio / zoom
        val yMin = pointIm - 1 / zoom
        val yMax = pointIm + 1 / zoom
        val xStep = (xMax - xMin) / worldX.toDouble()
        val yStep = (yMax - yMin) / worldY.toDouble()
        val X = DoubleArray(worldX)
        val Y = DoubleArray(worldY)
        for (i in 0 until worldX) X[i] = xMin + xStep * i
        for (j in 0 until worldY) Y[j] = yMin + yStep * j
        time += dt
        var threshold = thresholdBase
        for (n in 0 until (time / thresholdTimeScale).toInt())
            threshold *= 2

        for (j in 0 until worldY) {
            val jDimX = j * worldX
            for (i in 0 until worldX) {
                val index = jDimX + i
                var zRe = X[i]
                var zIm = Y[j]
                var k = 0
                while (k < threshold) {
                    val zReTemp = zRe
                    val zReTemp2 = zReTemp * zReTemp
                    val zIm2 = zIm * zIm
                    if (zReTemp2 + zIm2 > 4.0) break
                    zRe = zReTemp2 - zIm2 + X[i]
                    zIm = 2.0 * zReTemp * zIm + Y[j]
                    k++
                }
                if (k == threshold) {
                    pixelArray[index] = -0x1000000
                } else {
                    pixelArray[index] = colour[k % 16]
                }
            }
        }
    }

    class MandelbrotAnimationParameters(
        worldX: Int,
        worldY: Int,
        val pointRe: Double = -1.76633656629364184,
        val pointIm: Double = -0.04180922088925528,
        val thresholdBase: Int = 64,
        val thresholdTimeScale: Double = 80.0,

        val speed: Double = 0.3,
        val maxTime: Double = 120.0,
        val zoomBase: Double = 0.1,

        val colour: IntArray = intArrayOf(
            -0xe6f8e6,
            -0xbde1f1,
            -0x95cbfd,
            -0x66a900,
            -0x338000,
            -0x5600,
            -0x736a1,
            -0xe1741,
            -0x2c1308,
            -0x794a1b,
            -0xc6822f,
            -0xe7ad4f,
            -0xf3dd76,
            -0xfff89c,
            -0xfbfbb7,
            -0xf6fed1
        )
    ) : AnimationParameters(worldX, worldY)
}
