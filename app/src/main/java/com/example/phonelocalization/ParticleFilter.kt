package com.example.phonelocalization

import android.content.Context
import android.graphics.Bitmap
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.HandlerThread
import java.util.*
import kotlin.random.Random.Default.nextInt

object ParticleFilter {

   public fun AvailablePositions(): Array<Int> {
        var arr = arrayOf<Int>() // frvlstr blank arr
        for (x in 0..530){
            for(y in 0..448){
                if(( x in 0..96 && y in 4 .. 124) ||
                    (x in 94 .. 398 && y in 124 .. 173) ||
                    (x in 399 .. 530 && y in 42 .. 173) ||
                    (x in 450 .. 530 && y in 173 .. 448) ||
                    (x in 66 .. 450 && y in 390 .. 448) ||
                    (x in 0 .. 60 && y in 173 .. 448 )) {
                    arr += arrayOf(x, y);
                }
            }
        }
        return arr
    }

   public fun GeneratePositions( array:Array<Int>): Array<Int> {
       var arr = arrayOf<Int>()
        for (i in 1..10){
            var x: Int =  (1..array.size).random()
            arr+= array[x];

        }
            return arr;
    }
}