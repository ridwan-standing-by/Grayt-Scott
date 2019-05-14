package com.ridwanstandingby.graytscott.render

import com.ridwanstandingby.graytscott.math.*

class Camera(val screenDimension: IntVector2, private val initialDirection: SphericalVector2) {

    private var direction: SphericalVector2 = initialDirection
        set(value) {
            field = value
            rightDirection = Vector3(-direction.sinPhi, direction.cosPhi, 0.0)
            leftDirection = rightDirection * -1.0
            downDirection = Vector3(
                direction.cosTheta * direction.cosPhi,
                direction.cosTheta * direction.sinPhi,
                -direction.sinTheta
            )
            upDirection = downDirection * -1.0
        }

    lateinit var rightDirection: Vector3 private set
    lateinit var leftDirection: Vector3 private set
    lateinit var downDirection: Vector3 private set
    lateinit var upDirection: Vector3 private set

    fun transform(q: Quaternion) {
        direction = initialDirection.resolve().rotate(q).toSphericalVector2()
    }

    fun project(v: Vector3): FloatVector2 {
        val ksi = v.y * direction.cosPhi - v.x * direction.sinPhi
        val eta =
            v.z * direction.sinTheta - v.x * direction.cosPhi * direction.cosTheta - v.y * direction.sinPhi * direction.cosTheta
        val camX = ksi + screenDimension.x / 2
        val camY = -eta + screenDimension.y / 2
        return FloatVector2(camX.toFloat(), camY.toFloat())
    }
}