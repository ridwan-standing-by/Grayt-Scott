package com.ridwanstandingby.graytscott.animation

class AnimationRule<A : Animation<P, R>, P : AnimationParameters, R : AnimationRenderer>(
    private val animationConstructor: (P, R) -> A,
    var animationParameters: P,
    var animationRenderer: R
) {

    fun create(): A = animationConstructor(animationParameters, animationRenderer)
}
