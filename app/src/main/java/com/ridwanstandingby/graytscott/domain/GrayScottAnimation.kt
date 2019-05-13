package com.ridwanstandingby.graytscott.domain

import android.util.Log
import com.ridwanstandingby.graytscott.animation.Animation
import com.ridwanstandingby.graytscott.animation.AnimationParameters
import com.ridwanstandingby.graytscott.render.PixelArrayAnimationRenderer

@Suppress("NOTHING_TO_INLINE")
class GrayScottAnimation(parameters: GrayScottAnimationParameters, renderer: GrayScottAnimationRenderer) :
    Animation<GrayScottAnimationParameters, GrayScottAnimationRenderer>(parameters, renderer) {

    private val worldX = renderer.worldX
    private val worldY = renderer.worldY

    private val timeScale = parameters.timeScale

    private val laplacianSelfFactor = parameters.laplacianSelfFactor
    private val laplacianAdjacencyFactor = parameters.laplacianAdjacencyFactor
    private val laplacianDiagonalFactor = parameters.laplacianDiagonalFactor

    private val diffusionRateA = parameters.diffusionRateA
    private val diffusionRateB = parameters.diffusionRateB
    private val feedRate = parameters.feedRate
    private val killRate = parameters.killRate
    private val feedRatePlusKillRate = feedRate + killRate

    private var a = DoubleArray(worldX * worldY) { 1.0 }
    private var b = DoubleArray(worldX * worldY) { 0.0 }

    private val aLap = DoubleArray(worldX * worldY)
    private val bLap = DoubleArray(worldX * worldY)

    private var nDts = 0
    private var averageDt = 0.0

    init {
        writeCircle(80, 60, 15)
        writeCircle(70, 100, 10)
        writeCircle(120, 120, 20)
    }

    private fun writeCircle(x: Int, y: Int, r: Int) {
        for (i in 0 until worldX) {
            for (j in 0 until worldY) {
                if ((x - i) * (x - i) + (y - j) * (y - j) < r * r) {
                    val n = i + j * worldX
                    b[n] = 1.0
                }
            }
        }
    }

    override fun update(dt: Double) {
        nDts++
        averageDt = (averageDt * (nDts - 1) + dt) / nDts

        Log.d("####", "average dt: ${averageDt * 1000.0}")

        val aAdj = a.map { it * laplacianAdjacencyFactor }
        val aDia = a.map { it * laplacianDiagonalFactor }
        val bAdj = b.map { it * laplacianAdjacencyFactor }
        val bDia = b.map { it * laplacianDiagonalFactor }

        tickTock("laplacian dt: ") {
            for (n in 0 until worldX * worldY) {
                val i = n % worldX
                val j = n / worldX
                val ip1 = (i + 1) loopEnd worldX
                val im1 = (i - 1) loopStart worldX
                val jp1 = ((j + 1) loopEnd worldY) * worldX
                val jm1 = ((j - 1) loopStart worldY) * worldX
                val jsc = j * worldX
                aLap[n] = a[n] * laplacianSelfFactor +
                        aAdj[ip1 + jsc] + aDia[ip1 + jp1] + aAdj[i + jp1] + aDia[im1 + jp1] +
                        aAdj[im1 + jsc] + aDia[im1 + jm1] + aAdj[i + jm1] + aDia[ip1 + jm1]
                bLap[n] = b[n] * laplacianSelfFactor +
                        bAdj[ip1 + jsc] + bDia[ip1 + jp1] + bAdj[i + jp1] + bDia[im1 + jp1] +
                        bAdj[im1 + jsc] + bDia[im1 + jm1] + bAdj[i + jm1] + bDia[ip1 + jm1]
            }
        }

        val ab2 = a.zip(b) { a, b -> a * b * b }

        val aNew = DoubleArray(worldX * worldY) { n ->
            a[n] + timeScale * dt * (diffusionRateA * aLap[n] - ab2[n] + feedRate * (1.0 - a[n]))
        }
        val bNew = DoubleArray(worldX * worldY) { n ->
            b[n] + timeScale * dt * (diffusionRateB * bLap[n] + ab2[n] - feedRatePlusKillRate * b[n])
        }

        a = aNew
        b = bNew

        for (n in 0 until worldX * worldY) {
            renderer.pixelArray[n] = (b[n] * 16).toInt() * 1118481 + Int.MIN_VALUE
        }
    }

    private inline infix fun Int.loopStart(divisor: Int) = if (this < 0) this + divisor else this

    private inline infix fun Int.loopEnd(divisor: Int) = if (this < divisor) this else this - divisor

    private inline fun <T> tickTock(message: String, block: () -> T): T {
        val t1 = System.nanoTime()
        val result = block()
        val t2 = System.nanoTime()
        Log.d("ticktock", message + "${(t2 - t1) / 1000000.0}")
        return result
    }
}

class GrayScottAnimationParameters(
    val timeScale: Double = 5.0,
    val laplacianSelfFactor: Double = -1.0,
    val laplacianAdjacencyFactor: Double = 0.146446609406726237799577819,
    val laplacianDiagonalFactor: Double = 0.103553390593273762200422181,
    val diffusionRateA: Double = 1.0,
    val diffusionRateB: Double = 0.5,
    val feedRate: Double = 0.0545,
    val killRate: Double = 0.062
) : AnimationParameters()

class GrayScottAnimationRenderer(worldX: Int, worldY: Int) : PixelArrayAnimationRenderer(worldX, worldY)
