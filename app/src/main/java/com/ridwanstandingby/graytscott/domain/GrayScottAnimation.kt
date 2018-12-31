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

        val aLap = laplacian(a)
        val bLap = laplacian(b)
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

    private fun laplacian(f: DoubleArray): DoubleArray {
        val fAdj = f.map { it * laplacianAdjacencyFactor }
        val fDia = f.map { it * laplacianDiagonalFactor }

        return DoubleArray(worldX * worldY) { n ->
            val i = n % worldX
            val j = n / worldX
            val ip1 = (i + 1) rem worldX
            val im1 = (i - 1) rem worldX
            val jp1 = ((j + 1) rem worldY) * worldX
            val jm1 = ((j - 1) rem worldY) * worldX
            f[n] * laplacianSelfFactor +
                    fAdj[ip1 + j * worldX] + fDia[ip1 + jp1] + fAdj[i + jp1] + fDia[im1 + jp1] +
                    fAdj[im1 + j * worldX] + fDia[im1 + jm1] + fAdj[i + jm1] + fDia[ip1 + jm1]
        }
    }

    private inline infix operator fun Int.rem(divisor: Int): Int {
        val result = this % divisor
        return if (result < 0) result + divisor else result
    }
}
