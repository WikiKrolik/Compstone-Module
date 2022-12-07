package com.example.phonelocalization

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.HandlerThread
import java.util.*
import kotlin.random.Random.Default.nextInt

object ParticleFilter {

   public fun AvailablePositions( pair: Pair<Int, Int>): Boolean{
       // var arr = arrayOf<Pair<Int, Int>>() // frvlstr blank arr
       // for (x in 2..530){
            //for(y in 4..448){
                if(( pair.first in 2..96 && pair.second in 4 .. 173) ||
                    (pair.first in 94 .. 398 && pair.second in 124 .. 173) ||
                    (pair.first in 398 .. 530 && pair.second in 4 .. 42) ||
                    (pair.first in 453 .. 530 && pair.second in 42 .. 173) ||
                    (pair.first in 66 .. 450 && pair.second in 390 .. 448) ||
                    (pair.first in 2 .. 60)) {
                    return true
                }
            //}
        //}
        return false
    }

   public fun GeneratePositions(): String {
       var arr: String = ""
       var a: Int = 0
       var x: Int
       var y : Int
       while (a <= 50){
           var x: Int =  (0 .. 530).random()
           var y: Int = (0.. 488).random()
           var pair = Pair(x, y)
           if (AvailablePositions(pair)){
               a++
               arr += "$pair;     "
           }
        }
       return arr;
    }
}