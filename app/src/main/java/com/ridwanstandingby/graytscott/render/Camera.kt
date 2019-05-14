package com.ridwanstandingby.graytscott.render

import com.ridwanstandingby.graytscott.math.FloatVector2
import com.ridwanstandingby.graytscott.math.IntVector2
import com.ridwanstandingby.graytscott.math.SphericalVector2
import com.ridwanstandingby.graytscott.math.Vector3

class Camera(val screenDimension: IntVector2, var direction: SphericalVector2) {

    var rightDirection = Vector3(-direction.sinPhi, direction.cosPhi, 0.0)
    var leftDirection = rightDirection * -1.0
    var downDirection =
        Vector3(direction.cosTheta * direction.cosPhi, direction.cosTheta * direction.sinPhi, -direction.sinTheta)
    var upDirection = downDirection * -1.0

    fun project(v: Vector3): FloatVector2 {
        val ksi = v.y * direction.cosPhi - v.x * direction.sinPhi
        val eta =
            v.z * direction.sinTheta - v.x * direction.cosPhi * direction.cosTheta - v.y * direction.sinPhi * direction.cosTheta
        val camX = ksi + screenDimension.x / 2
        val camY = -eta + screenDimension.y / 2
        return FloatVector2(camX.toFloat(), camY.toFloat())
    }
}