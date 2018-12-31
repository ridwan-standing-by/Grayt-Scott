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

    init {
        for (i in 85 until 115) {
            for (j in 85 until 115) {
                val n = i + j * worldX
                b[n] = 1.0
            }
        }
//        b[100 + 100 * worldX] = 1.0
    }

    override fun update(dt: Double) {

        Log.d("####", "${pixelArray[100 + 100 * worldX]}")

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
            pixelArray[n] = (b[n] * 0x11111111).toInt()
        }
    }

    private fun laplacian(f: DoubleArray): DoubleArray {
        val fAdj = f.map { it * laplacianAdjacencyFactor }
        val fDia = f.map { it * laplacianDiagonalFactor }

        return DoubleArray(worldX * worldY) { n ->
            val i = n % worldX
            val j = n / worldX
            val ip1 = (i + 1) rem worldX
            val im1 = (i - 1) rem  worldX
            val jp1 = ((j + 1) rem  worldY) * worldX
            val jm1 = ((j - 1) rem  worldY) * worldX
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
