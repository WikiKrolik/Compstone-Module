package com.example.phonelocalization

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.HandlerThread

class Accelerometer(context: Context) : SensorEventListener {

    private val sensorManager: SensorManager
    private val lightSensor: Sensor
    private var handlerThread: HandlerThread
    private var x : Float = 0.0f
    private var y : Float = 0.0f
    private var z : Float = 0.0f
    private var paused : Boolean = false
    private var handler : Handler

    init {
        this.sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        this.lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        this.handlerThread = HandlerThread(Accelerometer::class.java.simpleName)
        this.handlerThread.start()
        handler = this.handlerThread.looper.let { Handler(it) }
        this.sensorManager.registerListener(this, this.lightSensor, SensorManager.SENSOR_DELAY_NORMAL, handler)
    }

    fun pause() {
        sensorManager.unregisterListener(this)
        if (this.handlerThread.isAlive)
            this.handlerThread.quitSafely()
        paused = true
    }

    fun resume() {
        if(paused) {
            this.handlerThread = HandlerThread(Accelerometer::class.java.simpleName)
            this.handlerThread.start()
            handler = this.handlerThread.looper.let { Handler(it) }
            this.sensorManager.registerListener(this, this.lightSensor, SensorManager.SENSOR_DELAY_NORMAL, handler)
        }
    }

    fun getX() : Float {
        return x
    }

    fun getY() : Float {
        return y
    }

    fun getZ() : Float {
        return z
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0 != null) {
            x = p0.values[0]
            y = p0.values[1]
            z = p0.values[2]
        }
    }
}