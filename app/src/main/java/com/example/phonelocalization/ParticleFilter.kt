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
import android.provider.Telephony.Mms.Part
import java.util.*
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.*

object ParticleFilter {


    // A simple data class to represent a 2D point with x and y coordinates
    data class Point(val x: Double, val y: Double)

    // A simple data class to represent a particle with a position (x, y) and weight
    data class Particle(val position: Point, var weight: Double)

    fun calculateMovement(
        magX: Double, magY: Double, gyroX: Double, gyroY: Double, accelX: Double, accelY: Double,
        particles: ArrayList<Particle>, dt: Double
    ): ArrayList<Particle> {
        var predictedParticles = particles
        // Predict the new positions of the particles using the gyroscope readings
        if (abs(accelX) > 0.7 || abs(accelY) > 0.7) {
             predictedParticles = particles.map { particle ->
                 val newX = (particle.position.x + accelX * dt/1000 * dt/1000 / 2)
                 val newY = (particle.position.y + accelY * dt/1000 * dt/1000 / 2)
                 Particle(Point(newX, newY), particle.weight)
             } as ArrayList<Particle>
        }
        // Update the weights of the particles using the magnetometer and accelerometer readings
        val updatedParticles = predictedParticles.map { particle ->
            val errorX = particle.position.x - magX
            val errorY = particle.position.y - magY
            val newWeight = exp(-errorX * errorX / 2.0 - errorY * errorY / 2.0)
            Particle(particle.position, newWeight)
        }

        // Normalize the weights of the particles
        val totalWeight = updatedParticles.map { it.weight }.sum()
        val normalizedParticles = updatedParticles.map { Particle(it.position, it.weight / totalWeight) }

        return normalizedParticles as ArrayList<Particle>
    }

   public fun AvailablePositions( pair: Point): Boolean{
       // var arr = arrayOf<Pair<Int, Int>>() // frvlstr blank arr
       // for (x in 2..530){
            //for(y in 4..448){
                if(( pair.x in 2.0..96.0 && pair.y in 4.0 .. 173.0) ||
                    (pair.x in 94.0 .. 398.0 && pair.y in 124.0 .. 173.0) ||
                    (pair.x in 398.0 .. 530.0 && pair.y in 4.0 .. 42.0) ||
                    (pair.x in 453.0 .. 530.0 && pair.y in 42.0 .. 173.0) ||
                    (pair.x in 66.0 .. 450.0 && pair.y in 390.0 .. 448.0) ||
                    (pair.x in 2.0 .. 60.0)) {
                    return true
                }
            //}
        //}
        return false
    }
    fun convertToCompassValues(): Float {

        // Calculate the heading using the magnetometer readings and trigonometry
        var heading = (atan2(SensorReader.Magnetometer.y.toDouble(), SensorReader.Magnetometer.x.toDouble()) *180/ PI - 90).toFloat()
        if (heading < 0 ) {
            heading += 360;
        }
        // Adjust the heading for the device's orientation (omitted for simplicity)

        return heading
    }


   public fun GeneratePositions(): List<Particle> {
       //var arr : ArrayList<Particle> = ArrayList<Particle>(3)
       val list = arrayListOf<Particle>()

       var a: Int = 0
       while (a <= 2){
           var x: Double =  ((0 .. 530).random()).toDouble()
           var y: Double = ((0.. 488).random()).toDouble()
           var pair = Particle(Point(x, y), 1.0)
           if (AvailablePositions(Point(x, y))){
               list.add(pair)
               a++
           }
        }
       return list;
    }

    fun Resample(particles : ArrayList<Particle>){
        //Sort the list of particles by their weight
        particles.sortedBy { it.weight }

        var numOfParticlesToRemove : Int = 0

        //TODO: Change the value of thresholdWeight to one that makes sense
        var thresholdWeight : Int = 50

        //Find particle with the biggest weight
        var maxParticle : Particle = particles.maxBy { it.weight }

        //Remove those particles, whose weight is too small.
        for(p in particles)
        {
            if(p.weight < thresholdWeight){
                numOfParticlesToRemove++
                particles.remove(p)
            }
        }

//        //Copy the particle with the biggest weight, as many times, as many particles have been deleted in this iteration.
//        for(i in 0..numOfParticlesToRemove){
//            particles.add(Particle(maxParticle.x, maxParticle.y, maxParticle.weight))
//        }

    }
}