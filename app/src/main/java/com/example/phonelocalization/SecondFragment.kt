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
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.phonelocalization.databinding.FragmentSecondBinding


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





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler.postDelayed(Runnable {

            handler.postDelayed(runnable!!, delay.toLong())
             arr = ParticleFilter.calculateMovement(SensorReader.Magnetometer.x.toDouble(), SensorReader.Magnetometer.y.toDouble(),
                                                SensorReader.Gyroscope.x.toDouble(),SensorReader.Gyroscope.y.toDouble(),
                                                SensorReader.Accelerometer.x.toDouble(), SensorReader.Accelerometer.y.toDouble(),
                                                arr, delay.toDouble())

            //binding.positionData.text = arr.toString()
        }.also { runnable = it }, delay.toLong())


    }
override fun onDestroyView() {
    runnable?.let { handler.removeCallbacks(it) }
        super.onDestroyView()
        _binding = null
    }
}


