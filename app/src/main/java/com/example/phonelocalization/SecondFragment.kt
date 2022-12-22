package com.example.phonelocalization

import android.hardware.Sensor
import android.hardware.SensorEvent
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

private var _binding: FragmentSecondBinding? = null
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 500
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      _binding = FragmentSecondBinding.inflate(inflater, container, false)
       // binding.positionData.text = ParticleFilter.GeneratePositions()


      return binding.root

    }


//    fun onSensorChanged(event: SensorEvent) {
//        // get angle around the z-axis rotated
//        val degree = Math.round(event.values[0]).toFloat()
//        binding.positionData.text = ("Heading: " + java.lang.Float.toString(degree) + " degrees")
//        DegreeStart = -degree
//    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())

            binding.positionData.text = ParticleFilter.convertToCompassValues().toString()
        }.also { runnable = it }, delay.toLong())


    }
override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun ShowArray(array:Array<Int>): String {
    var result: String = ""
    for (i in 1..array.size) {
        result += array[i-1].toString() + " ,"
    }
    return result;
}
