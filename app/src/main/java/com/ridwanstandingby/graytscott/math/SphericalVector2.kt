package com.ridwanstandingby.graytscott.math

import kotlin.math.cos
import kotlin.math.sin

data class SphericalVector2(val phi: Double, val theta: Double) {

    val sinTheta = sin(theta)
    val cosTheta = cos(theta)
    val sinPhi = sin(phi)
    val cosPhi = cos(phi)

    fun resolve() = Vector3(sinTheta * cosPhi, sinTheta * sinPhi, cosTheta)

    operator fun times(k: Double) = SphericalVector3(k, phi, theta)
}
