package com.example.phonelocalization

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.getSystemService
import java.lang.String
import kotlin.Float


class SpeedCalculator(private val context: Context) {

    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null

    private var lax = 0f
    private var lay = 0f
    private var laz = 0f

    private var vx = 0.0
    private var vy = 0.0
    private var vz = 0.0

    private var timestamp : Long = 0

    private var speed = 0f

    private val NS2S = 1.0f / 1000000000.0f

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor == mAccelerometer) {
                if (timestamp != 0L) {
                    val dT: Float = (event.timestamp - timestamp) * NS2S
                    lax = event.values[0]
                    lay = event.values[1]
                    laz = event.values[2]
                    vx = vx + lax * dT
                    vy = vy + lay * dT
                    vz = vz + laz * dT
                    speed = Math.sqrt(vx * vx + vy * vy + vz * vz).toFloat()
                    if (speed < 0.01) {
                        speed = 0f
                    }
                }
                timestamp = event.timestamp
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        }
    }

    init {
        mSensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mSensorManager?.registerListener(listener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun getSpeed(): Float {
        return speed
    }
}