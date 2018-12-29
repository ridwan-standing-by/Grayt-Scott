package com.ridwanstandingby.graytscott.render

abstract class Animation<P : AnimationParameters>(parameters: P) {

    val worldX = parameters.worldX
    val worldY = parameters.worldY
    val pixelArray: IntArray = IntArray(worldX * worldY)

    init {
        for (i in 0 until worldX * worldY) {
            pixelArray[i] = -0x1000000
        }
    }

    abstract fun update()
}
