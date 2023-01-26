package com.example.phonelocalization

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.example.phonelocalization.databinding.FragmentSecondBinding
import kotlin.math.*


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
    var delay = 250
    var firstAngle = 0.0;
    private var speedCalculator : SpeedCalculator? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        // binding.positionData.text = ParticleFilter.GeneratePositions()
        val particle = ParticleFilter.Particle(ParticleFilter.Point(95.0, 150.0), 1.0, 10.0, 10.0);
        //arr.add(particle)
        arr = ParticleFilter.GeneratePositions() as ArrayList<ParticleFilter.Particle>
        firstAngle = (calculateRotationAngle(
            SensorReader.Gyroscope.z.toDouble(),
            SensorReader.Gyroscope.timestamp
        ))
        speedCalculator = SpeedCalculator(requireContext())
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

    fun calculateRotationAngle(z: Double, timestamp: Long): Double {
        val dt = (timestamp - previousTimestamp) * NS2S
        previousTimestamp = timestamp
        rotationAngle += z * dt
        return rotationAngle
    }

    fun shiftParticles(particles : ArrayList<ParticleFilter.Particle>, speed : Double, angle : Double, dt : Float): ArrayList<ParticleFilter.Particle> {
        //Calculate the distance in pixels, that the particles have to move.
        var distance : Double = (speed * dt * 1/ 1000 * pixelToMetersRatio)

        var x: Double
        var y: Double

        //Now we iterate through all the particles, and move them the respective distance.
        for(p in particles){
            //Calculate the x coordinate
            x = (distance /
                    sqrt(1 + tan(angle).pow(2.0)))

            //Calculate the y coordinate
            y = (x * tan(angle))

            //Change signs if needed.
            if(angle in 180f..270f || angle in 270f..360f)
                y = - y
            if(angle in 0f..180f)
                x = - x

            //Shift the particles by the distance, expressed as pixels.
            p.position.x += x
            p.position.y += y
        }

        return particles;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
//            arr = ParticleFilter.calculateMovement(
//                SensorReader.Magnetometer.x.toDouble(), SensorReader.Magnetometer.y.toDouble(),
//                SensorReader.Gyroscope.x.toDouble(), SensorReader.Gyroscope.y.toDouble(),
//                SensorReader.Accelerometer.x.toDouble(), SensorReader.Accelerometer.y.toDouble(),
//                arr, delay.toDouble()
//            )
            var diff = abs((calculateRotationAngle(
                SensorReader.Gyroscope.z.toDouble(),
                SensorReader.Gyroscope.timestamp
            )) - firstAngle ) + (( 2 * 3.14 /360) * 89)
            var oldArr = arr
            WifiReader.refresh(requireContext())
            var wifiStrength = WifiReader.Wifi.signalStrength

            arr = shiftParticles(arr, speedCalculator?.getSpeed()!!.toDouble(), diff, delay.toFloat())
            arr = ParticleFilter.particleFilter(arr,oldArr,wifiStrength.toDouble())
            var angle = ((calculateRotationAngle(
                SensorReader.Gyroscope.z.toDouble(),
                SensorReader.Gyroscope.timestamp
            ) * 180 * 0.31830988618) % 360)
            binding.positionData.text = arr.toString() + "\n" + "speed: " + speedCalculator?.getSpeed()!!.toString() + "\n" + "angle: " + diff.toString() ;
            drawMinimap(arr[0].position.x.toInt(), arr[0].position.y.toInt());
        }.also { runnable = it }, delay.toLong())


    }

    override fun onDestroyView() {
        runnable?.let { handler.removeCallbacks(it) }
        super.onDestroyView()
        _binding = null
    }

    fun drawMinimap(x: Int, y: Int) {
        val mapImageView: ImageView = binding.ivFloorMap
        val mapBitmap: Bitmap = (mapImageView.drawable).toBitmap()

        val bitmap = Bitmap.createBitmap(mapBitmap.width, mapBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(
            mapBitmap,
            Rect(0, 0, mapBitmap.width, mapBitmap.height),
            Rect(0, 0, mapBitmap.width, mapBitmap.height),
            null
        )
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.RED
        canvas.drawCircle(
            (x.toFloat() / 531) * mapBitmap.width.toFloat(),
            (y.toFloat() / 449) * mapBitmap.height.toFloat(),
            10.0f,
            paint
        )
        paint.color = Color.MAGENTA
        canvas.drawCircle(
            (x.toFloat() / 531) * mapBitmap.width.toFloat(),
            (y.toFloat() / 449) * mapBitmap.height.toFloat(),
            10.0f,
            paint
        )

        mapImageView.setImageDrawable(BitmapDrawable(resources, bitmap))
    }
}


