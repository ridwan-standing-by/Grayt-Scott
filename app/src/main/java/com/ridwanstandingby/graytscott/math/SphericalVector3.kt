package com.ridwanstandingby.graytscott.math

import kotlin.math.cos
import kotlin.math.sin

data class SphericalVector3(val r: Double, val phi: Double, val theta: Double) {

    fun resolve() = Vector3(r * sin(theta) * cos(phi), r * sin(theta) * sin(phi), r * cos(theta))
}
