package com.example.phonelocalization

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.phonelocalization.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 500
    private var speedCalculator : SpeedCalculator? = null


    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        speedCalculator = SpeedCalculator(requireContext())
        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        activity?.let {
            handler.postDelayed(Runnable {
                handler.postDelayed(runnable!!, delay.toLong())

//                WifiReader.refresh(requireContext())
//                binding.wifiData.text = WifiReader.Wifi.signalStrength
                binding.wifiData.text = speedCalculator?.getSpeed().toString()

                SensorReader.start(requireContext())
                binding.accelerometerData.text = SensorReader.Accelerometer.x.toString() + "\n" +
                        SensorReader.Accelerometer.y.toString() + "\n" +
                        SensorReader.Accelerometer.z.toString()

                binding.gyroscopeData.text = speedCalculator?.getET().toString()
//                    SensorReader.Gyroscope.x.toString() + "\n" +
//                        SensorReader.Gyroscope.y.toString() + "\n" +
//                        SensorReader.Gyroscope.z.toString() + "\n"

                binding.magnetometerData.text = SensorReader.Magnetometer.x.toString() + "\n" +
                        SensorReader.Magnetometer.y.toString() + "\n" +
                        SensorReader.Magnetometer.z.toString() + "\n"

            }.also { runnable = it }, delay.toLong())
        }

        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        runnable?.let { handler.removeCallbacks(it) }
        super.onDestroyView()
        _binding = null
    }
}

