package com.ridwanstandingby.graytscott.animation

abstract class Animation<P : AnimationParameters, R : AnimationRenderer>(val parameters: P, val renderer: R) {

    abstract fun update(dt: Double)
}
