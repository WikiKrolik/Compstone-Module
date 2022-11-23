package com.example.phonelocalization

import android.content.Context

object WifiReader {

    object Wifi{
        var signalStrength = "0"
        var MACAddress = "0"
    }

    fun refresh(context : Context){
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as android.net.wifi.WifiManager?
            val info = wifiManager?.connectionInfo
            val rssi = info?.rssi
            val mac = info?.bssid
            Wifi.signalStrength = rssi.toString()
            Wifi.MACAddress = mac.toString()
    }
}