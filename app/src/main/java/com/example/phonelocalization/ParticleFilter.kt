package com.example.phonelocalization

import java.util.*
import kotlin.Boolean
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.math.*


object ParticleFilter {


    // A simple data class to represent a 2D point with x and y coordinates
    data class Point(var x: Double, var y: Double)

    // A simple data class to represent a particle with a position (x, y) and weight
    data class Particle(val position: Point, var weight: Double, var dx: Double, var dy: Double)

    data class PositionVelocityData(var currentPos : Point, var previousPos : Point, var currentVel : Point, var previousVel : Point)
    var IntegrationData = PositionVelocityData(ParticleFilter.Point(0.0,0.0), ParticleFilter.Point(0.0,0.0),ParticleFilter.Point(0.0,0.0),ParticleFilter.Point(0.0,0.0))

    fun particleFilter(predictedParticles :ArrayList<Particle>, previousParticles : ArrayList<Particle>, wifiStrength : Double) : ArrayList<Particle>  {

        var x = 0
        for (particle in predictedParticles) {
            val dx = particle.position.x - previousParticles[x].position.x
            val dy = particle.position.y - previousParticles[x].position.y
            val dr = 0;
            x++
//            val positionComponent1 = sqrt(Math.pow(newX, 2.0) + Math.pow(newY, 2.0))
            val positionComponent2 = Math.atan2(dy, dx) - atan2(particle.dy, particle.dx)
//            val positionComponent3 = Math.atan2(-dy, -dx) - atan2(particle.position.dy, particle.position.dx)

            val const1 = 1
            val const2 = 1
            val const3 = 1

            val power1 = -Math.pow(dx - particle.dx, 2.0) / 2 * const1 * const1
            val power2 = -Math.pow(dy - particle.dy, 2.0) / 2 * const1 * const1
//            val power3 = -Math.pow()

            val probability = 1 / (sqrt(2 * Math.PI) * const1) * Math.pow(Math.E, power1) * 1 / sqrt(2 * Math.PI * const1) * Math.pow(Math.E, power2)
            // Update weight and change in position
            particle.weight = probability
            particle.dx = particle.dx
            particle.dy = particle.dy
        }

        // Normalize the weights of the particles
        normalizeWeights(predictedParticles)


        val ESSThreshold = 0.5
        if(calculateESS(predictedParticles) < ESSThreshold) {
            resample(predictedParticles.toMutableList() as java.util.ArrayList<ParticleFilter.Particle>)
        }

        val wifiThreshold = 2000
        val iterator = predictedParticles.iterator()
//        while(iterator.hasNext()) {
//            val element = iterator.next()
//            try{
//            if( Math.abs(WifiHeatmap.getWifiStrength(element.position.x.roundToInt(),
//                    element.position.y.roundToInt()) - wifiStrength) > wifiThreshold) {
//                iterator.remove()
//            }
//            }
//            catch (e: Exception) {
//
//            }
//
//        }

        return predictedParticles
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
       while (a <= 200){
           var x: Double =  ((0 .. 530).random()).toDouble()
           var y: Double = ((0.. 488).random()).toDouble()
           var pair = Particle(Point(x, y), 1.0, 0.0, 0.0)
           if (AvailablePositions(Point(x, y))){
               list.add(pair)
               a++
           }
        }
       return list;
    }

    fun resample(particles : ArrayList<Particle>){
        //Sort the list of particles by their weight
        particles.sortedBy { it.weight }

        var numOfParticlesToRemove : Int = 0

        //TODO: Change the value of thresholdWeight to one that makes sense
        var thresholdWeight : Double = 0.5

        //Find particle with the biggest weight
        var maxParticle : Particle = particles.maxBy { it.weight }

        //Remove those particles, whose weight is too small.

        val iterator = particles.iterator()

        while(iterator.hasNext()) {
            val element = iterator.next()
            if(element.weight < thresholdWeight) {
                numOfParticlesToRemove++
                iterator.remove()
            }
        }
//        //Copy the particle with the biggest weight, as many times, as many particles have been deleted in this iteration.
        var a = 0
        while (a <= numOfParticlesToRemove){
            var x: Double =  ((0 .. 530).random()).toDouble()
            var y: Double = ((0.. 488).random()).toDouble()
            var pair = Particle(Point(x, y), maxParticle.weight, maxParticle.dx, maxParticle.dy)
            if (AvailablePositions(Point(x, y))){
                particles.add(pair)
                a++
            }
        }
    }
    fun calculateESS(particles: ArrayList<Particle>): Double {

        var weightSum = 0.0

        var weightSquaredSum = 0.0

        for (particle in particles) {

            weightSum += particle.weight

            weightSquaredSum += particle.weight * particle.weight

        }

        return weightSum * weightSum / weightSquaredSum
    }
    fun normalizeWeights(particles: ArrayList<Particle>) {

        var weightSum = 0.0

        for (particle in particles) {

            weightSum += particle.weight

        }

        for (particle in particles) {

            particle.weight /= weightSum

        }
    }
}