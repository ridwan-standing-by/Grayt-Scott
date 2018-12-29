package com.ridwanstandingby.graytscott.render

class AnimationRule<A : Animation<P>, P : AnimationParameters>(
    private val animationConstructor: (P) -> A,
    var animationParameters: P
) {

    fun create(): A = animationConstructor(animationParameters)
}
