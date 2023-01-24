package com.example.phonelocalization

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.HandlerThread

object SensorReader : SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometerSensor: Sensor
    private lateinit var gyroscopeSensor: Sensor
    private lateinit var magnetometerSensor: Sensor
    private lateinit var handlerThread: HandlerThread
    private lateinit var handler : Handler
    private var paused : Boolean = false

    object Accelerometer {
        var x : Float = 0.0f
        var y : Float = 0.0f
        var z : Float = 0.0f
    }
    object Gyroscope {
        var x : Float = 0.0f
        var y : Float = 0.0f
        var z : Float = 0.0f
    }
    object Magnetometer {
        var x : Float = 0.0f
        var y : Float = 0.0f
        var z : Float = 0.0f
    }

    fun start(context: Context) {
        this.sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        this.accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        this.gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        this.magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        this.handlerThread = HandlerThread(Accelerometer::class.java.simpleName)
        this.handlerThread.start()
        handler = this.handlerThread.looper.let { Handler(it) }
        this.sensorManager.registerListener(this, this.accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL, handler)
        this.sensorManager.registerListener(this, this.gyroscopeSensor,SensorManager.SENSOR_DELAY_NORMAL, handler)
        this.sensorManager.registerListener(this, this.magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL, handler)
    }

    fun pause() {
        sensorManager.unregisterListener(this)
        if (this.handlerThread.isAlive)
            this.handlerThread.quitSafely()
        paused = true
    }

    fun resume() {
        if(paused) {
            paused = false
            this.handlerThread = HandlerThread(Accelerometer::class.java.simpleName)
            this.handlerThread.start()
            handler = this.handlerThread.looper.let { Handler(it) }
            this.sensorManager.registerListener(this, this.accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST, handler)
            this.sensorManager.registerListener(this, this.gyroscopeSensor,SensorManager.SENSOR_DELAY_FASTEST, handler)
            this.sensorManager.registerListener(this, this.magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL, handler)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    Accelerometer.x = event.values[0]
                    Accelerometer.y = event.values[1]
                    Accelerometer.z = event.values[2]
                }
                Sensor.TYPE_GYROSCOPE -> {
                    Gyroscope.x = event.values[0]
                    Gyroscope.y = event.values[1]
                    Gyroscope.z = event.values[2]
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    Magnetometer.x = event.values[0]
                    Magnetometer.y = event.values[1]
                    Magnetometer.z = event.values[2]
                }
            }

        }
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}
