package com.ridwanstandingby.graytscott.animation

abstract class Animation<P : AnimationParameters, R : AnimationRenderer, I : AnimationInput>(
    val parameters: P,
    val renderer: R,
    val input: I
) {

    abstract fun update(dt: Double)
}
