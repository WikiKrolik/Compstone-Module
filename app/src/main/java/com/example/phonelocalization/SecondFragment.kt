package com.example.phonelocalization

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.phonelocalization.databinding.FragmentSecondBinding
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.math.tan


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var SensorManage: SensorManager? = null
    private var DegreeStart = 0f
    var arr = arrayListOf<ParticleFilter.Particle>()
    var list = arrayListOf<ParticleFilter.Particle>()
private var _binding: FragmentSecondBinding? = null
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 10
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      _binding = FragmentSecondBinding.inflate(inflater, container, false)
       // binding.positionData.text = ParticleFilter.GeneratePositions()
         arr = ParticleFilter.GeneratePositions() as ArrayList<ParticleFilter.Particle>
        binding.positionData.text = ParticleFilter.GeneratePositions().toString()

      return binding.root

    }


    // Sensor event listener
    val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            // Check the sensor type
            when (event.sensor.type) {
                Sensor.TYPE_GYROSCOPE -> {
                    // Convert the raw gyroscope data into angular velocity (degrees per second)
                    val angularVelocityX = event.values[0]
                    val angularVelocityY = event.values[1]

                    // Update the particle filter with the gyroscope data
//                    particleFilter.predict(angularVelocityX, angularVelocityY, dt)
                }
                Sensor.TYPE_ACCELEROMETER -> {
                    // Convert the raw accelerometer data into acceleration (g)
                    val accelerationX = event.values[0]
                    val accelerationY = event.values[1]

                    // Update the particle filter with the accelerometer data
                    //particleFilter.update(accelerationX, accelerationY)
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    // Convert the raw accelerometer data into acceleration (g)
                    val magnetometerX = event.values[0]
                    val magnetometerY = event.values[1]

                    // Update the particle filter with the accelerometer data
                    //particleFilter.update(accelerationX, accelerationY)
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }


    var NS2S = 1.0f / 1000000000.0f
    var previousTimestamp: Long = 0
    var rotationAngle: Double = 0.0
    val pixelToMetersRatio = 24.411

    fun calculateRotationAngle(z : Double, timestamp : Long): Double {
        val dt = (timestamp - previousTimestamp) * NS2S
        previousTimestamp = timestamp
        rotationAngle += z * dt
        return rotationAngle
    }

    fun shiftParticles(particles : ArrayList<Particle>, speed : Double, angle : Double, dt : Float){
        //Calculate the distance in pixels, that the particles have to move.
        var distance : Int = (speed * dt * NS2S * pixelToMetersRatio).roundToInt()

        var x: Int
        var y: Int

        //Now we iterate through all the particles, and move them the respective distance.
        for(p in particles){
            //Calculate the x coordinate
            x = (distance /
                    sqrt(1 + tan(angle).pow(2.0))).roundToInt()

            //Calculate the y coordinate
            y = (x * tan(angle)).roundToInt()

            //Shift the particles by the distance, expressed as pixels.
            p.x += x
            p.y += y
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler.postDelayed(Runnable {

            handler.postDelayed(runnable!!, delay.toLong())
             arr = ParticleFilter.calculateMovement(SensorReader.Magnetometer.x.toDouble(), SensorReader.Magnetometer.y.toDouble(),
                                                SensorReader.Gyroscope.x.toDouble(),SensorReader.Gyroscope.y.toDouble(),
                                                SensorReader.Accelerometer.x.toDouble(), SensorReader.Accelerometer.y.toDouble(),
                                                arr, delay.toDouble())

            binding.positionData.text = ((calculateRotationAngle(SensorReader.Gyroscope.z.toDouble(), SensorReader.Gyroscope.timestamp) * 180 * 0.31830988618) % 360).toString()

        }.also { runnable = it }, delay.toLong())


    }
override fun onDestroyView() {
    runnable?.let { handler.removeCallbacks(it) }
        super.onDestroyView()
        _binding = null
    }
}


