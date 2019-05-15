package com.ridwanstandingby.graytscott.math

import kotlin.math.cos
import kotlin.math.sin

data class Quaternion(val t: Double, val x: Double, val y: Double, val z: Double) {
    constructor(angle: Double, axis: Vector3) : this(
        cos(angle / 2),
        sin(angle / 2) * axis.x,
        sin(angle / 2) * axis.y,
        sin(angle / 2) * axis.z
    )

    operator fun plus(o: Quaternion) = Quaternion(t + o.t, x + o.x, y + o.y, z + o.z)

    operator fun times(o: Quaternion) =
        Quaternion(
            t * o.t - x * o.x - y * o.y - z * o.z,
            t * o.x + x * o.t + y * o.z - z * o.y,
            t * o.y - x * o.z + y * o.t + z * o.x,
            t * o.z + x * o.y - y * o.x + z * o.t
        )

    fun inverse() = Quaternion(t, -x, -y, -z)

    fun axis() = Vector3(x, y, z)
}
