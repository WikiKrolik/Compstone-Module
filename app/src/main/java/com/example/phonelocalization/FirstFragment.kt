package com.example.phonelocalization

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
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

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onResume() {

        super.onResume()
        activity?.let {
            handler.postDelayed(Runnable {
                handler.postDelayed(runnable!!, delay.toLong())
                val wifiManager = activity?.getSystemService(Context.WIFI_SERVICE) as WifiManager?
                val info = wifiManager?.connectionInfo
                val rssi = info?.rssi
                binding.wifiData.text = rssi.toString()
            }.also { runnable = it }, delay.toLong())
        }
        SensorReader.start(requireContext())
        binding.accelerometerData.text = SensorReader.Accelerometer.x.toString() + "\n" +
                SensorReader.Accelerometer.y.toString() + "\n" +
                SensorReader.Accelerometer.z.toString()

        binding.gyroscopeData.text = SensorReader.Gyroscope.x.toString() + "\n" +
                SensorReader.Gyroscope.y.toString() + "\n" +
                SensorReader.Gyroscope.z.toString() + "\n"

        binding.magnetometerData.text = SensorReader.Magnetometer.x.toString() + "\n"
        SensorReader.Magnetometer.y.toString() + "\n"
        SensorReader.Magnetometer.z.toString() + "\n"

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

