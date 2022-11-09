package com.example.phonelocalization

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.phonelocalization.databinding.FragmentFirstBinding
import java.time.LocalDateTime


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val rssiFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        requireActivity().registerReceiver(myRssiChangeReceiver, rssiFilter)
        val wifiMan = requireActivity().getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiMan.startScan()
        activity?.registerReceiver(myRssiChangeReceiver, IntentFilter(WifiManager.RSSI_CHANGED_ACTION))
        //LocalBroadcastManager.getInstance(requireContext()).registerReceiver(myRssiChangeReceiver, IntentFilter(WifiManager.RSSI_CHANGED_ACTION))
      return binding.root

    }

    val myRssiChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val wifiMan : WifiManager = activity?.getSystemService(Context.WIFI_SERVICE) as WifiManager
            wifiMan.startScan()
            val newRSSI : Int = wifiMan.connectionInfo.rssi
            binding.wifiData.text = newRSSI.toString()
            binding.gyroscopeData.text = LocalDateTime.now().toString()
            wifiMan.startScan()
        }
    }

//    override fun onResume() {
//        super.onResume()
//        //Note: Not using RSSI_CHANGED_ACTION because it never calls me back.
//        val rssiFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
//        requireActivity().registerReceiver(myRssiChangeReceiver, rssiFilter)
//        val wifiMan = requireActivity().getSystemService(Context.WIFI_SERVICE) as WifiManager
//        wifiMan.startScan()
//    }

//    override fun onPause() {
//        super.onPause()
//        requireActivity().unregisterReceiver(myRssiChangeReceiver)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    fun getWifiStrength() {
//        val wmgr = con
//    }
}
