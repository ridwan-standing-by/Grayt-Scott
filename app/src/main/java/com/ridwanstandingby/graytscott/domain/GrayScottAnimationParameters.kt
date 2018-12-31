package com.ridwanstandingby.graytscott.domain

import com.ridwanstandingby.graytscott.render.AnimationParameters

class GrayScottAnimationParameters(
    worldX: Int,
    worldY: Int,
    val timeScale: Double = 1.0,
    val laplacianSelfFactor: Double = -1.0,
    val laplacianAdjacencyFactor: Double = 0.2,
    val laplacianDiagonalFactor: Double = 0.05,
    val diffusionRateA: Double = 1.0,
    val diffusionRateB: Double = 0.5,
    val feedRate: Double = 0.055,
    val killRate: Double = 0.062
) : AnimationParameters(worldX, worldY)
