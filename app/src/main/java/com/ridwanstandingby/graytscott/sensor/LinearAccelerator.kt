package com.ridwanstandingby.graytscott.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.ridwanstandingby.graytscott.math.Vector3

class LinearAccelerator(
    private val sensorManager: SensorManager,
    private val linearAccelerationSensor: Sensor
) {

    private data class Event(val acceleration: Vector3, val timestamp: Long)

    private fun SensorEvent.toEvent() = Event(values.toVector3(), timestamp)

    private var lastEvent: Event? = null
    private var accumulatedVelocity = Vector3(0.0, 0.0, 0.0)

    fun start(pollingIntervalMicros: Int, sensitivityScale: Double) {
        sensorManager.registerListener(object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.toEvent()?.let { newEvent ->
                    lastEvent?.let { lastEvent ->
                        accumulatedVelocity += (newEvent.acceleration - lastEvent.acceleration) *
                                ((newEvent.timestamp - lastEvent.timestamp).toDouble() * sensitivityScale / 1e9)
                    }
                    lastEvent = newEvent.copy()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }
        }, linearAccelerationSensor, pollingIntervalMicros)
    }

    fun calculateVelocity(): Vector3 {
        val v = accumulatedVelocity.copy()
        accumulatedVelocity = Vector3(0.0, 0.0, 0.0)
        return v
    }

    private fun FloatArray.toVector3() = Vector3(this[0].toDouble(), this[1].toDouble(), this[2].toDouble())
}
