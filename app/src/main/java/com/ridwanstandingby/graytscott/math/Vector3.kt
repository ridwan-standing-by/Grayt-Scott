package com.ridwanstandingby.graytscott.math

import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

data class Vector3(val x: Double, val y: Double, val z: Double) {

    operator fun plus(o: Vector3) = Vector3(x + o.x, y + o.y, z + o.z)
    operator fun minus(o: Vector3) = Vector3(x - o.x, y - o.y, z - o.z)
    operator fun times(k: Double) = Vector3(x * k, y * k, z * k)
    infix fun dot(o: Vector3) = x * o.x + y * o.y + z * o.z

    fun rotate(q: Quaternion): Vector3 = (q * (this.toQuaternion() * q.inverse())).axis()

    fun reflect(n: Vector3): Vector3 = this + n * ((this dot n) * -2.0)

    fun toSphericalVector2() = normalise().let { SphericalVector2(atan2(it.y, it.x), acos(it.z / it.size())) }
    fun toQuaternion() = Quaternion(0.0, x, y, z)

    fun normalise() = size().let { norm ->
        Vector3(x / norm, y / norm, z / norm)
    }

    fun size() = sqrt(x * x + y * y + z * z)
}
