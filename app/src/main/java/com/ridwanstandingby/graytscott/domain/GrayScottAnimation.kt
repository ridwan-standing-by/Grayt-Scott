package com.ridwanstandingby.graytscott.domain

import android.util.Log
import com.ridwanstandingby.graytscott.render.Animation

@Suppress("NOTHING_TO_INLINE")
class GrayScottAnimation(parameters: GrayScottAnimationParameters) :
    Animation<GrayScottAnimationParameters>(parameters) {

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
                if ((x-i) * (x-i) + (y-j) * (y-j) < r * r) {
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
                val ip1 = (i + 1) rem worldX
                val im1 = (i - 1) rem worldX
                val jp1 = ((j + 1) rem worldY) * worldX
                val jm1 = ((j - 1) rem worldY) * worldX
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
            pixelArray[n] = (b[n] * 16).toInt() * 1118481 + Int.MIN_VALUE
        }
    }

    private inline infix operator fun Int.rem(divisor: Int): Int {
        val result = this % divisor
        return if (result < 0) result + divisor else result
    }

    private inline fun <T>tickTock(message: String, block: () -> T): T {
        val t1 = System.nanoTime()
        val result = block()
        val t2 = System.nanoTime()
        Log.d("ticktock", message + "${(t2 - t1)/1000000.0}")
        return result
    }
}
