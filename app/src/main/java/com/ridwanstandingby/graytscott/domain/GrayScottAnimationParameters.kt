package com.ridwanstandingby.graytscott.domain

import com.ridwanstandingby.graytscott.render.AnimationParameters

class GrayScottAnimationParameters(
    worldX: Int,
    worldY: Int,
    val timeScale: Double = 5.0,
    val laplacianSelfFactor: Double = -1.0,
    val laplacianAdjacencyFactor: Double = 0.146446609406726237799577819,
    val laplacianDiagonalFactor: Double = 0.103553390593273762200422181,
    val diffusionRateA: Double = 1.0,
    val diffusionRateB: Double = 0.5,
    val feedRate: Double = 0.0545,
    val killRate: Double = 0.062
) : AnimationParameters(worldX, worldY)
