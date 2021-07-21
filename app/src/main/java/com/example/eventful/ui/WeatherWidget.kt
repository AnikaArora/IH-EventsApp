package com.example.eventful.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.eventful.R
import com.example.eventful.databinding.WeatherWidgetBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.util.*

class WeatherWidget(private val weatherView: WeatherWidgetBinding) {

    var CITY: String? = null
    var API: String = "991c4746ce85b4c70a462313962cd22a"

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var context : Context

    suspend fun init(context: Context) {
        // class for api call
        this.context = context
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context)
        Log.d("weatherWidget", "start of init")
        CITY = getLocation()
        Log.d("City", "CITY = $CITY")
        val result = handler()
        postApiCall(result)

    }

    private suspend fun handler(): String? {
        return apiCall()
    }

    private fun getLocation(): String? {

//        var locationManager : LocationManager? = getSystemService(context, ) as LocationManager?
//        var criteria : Criteria = Criteria()
//        var provider : String? = locationManager?.getBestProvider(criteria, true)
//        val location : Location? = locationManager?.getLastKnownLocation(provider)
        var lat : Double
        var lon : Double
        val geocoder : Geocoder = Geocoder(this.context, Locale.getDefault())
        var addressList : List<Address?>
        var address : String? = null

        if (ActivityCompat.checkSelfPermission(
                this.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
                location : Location? ->
            if (location != null) {
                lat = location.latitude
                lon = location.longitude
                addressList = geocoder.getFromLocation(lat, lon, 1)
                address = addressList[0].toString()
            }
        }
        return address
    }

    //override doInBackground()
    private suspend fun apiCall(): String? {
        var response:String?
        try {
            response = URL("api.openweathermap.org/data/2.5/weather?q=$CITY&appid=$API").readText(Charsets.UTF_8)
        } catch (e: Exception) {
            response = null
        }
        return response
    }

    //override onPostExecute()
    private fun postApiCall(result: String?) {
        try {
            val jsonObj = JSONObject(result)
            val main = jsonObj.getJSONObject("main")
            val temp = main.getString("temp") + "\u2103"
            val weather = jsonObj.getJSONObject("weather")
            val conditions = weather.getJSONObject("main")
            val icon = weather.getJSONObject("icon")

            val v : View = weatherView.mainContainer

            v.findViewById<TextView>(R.id.temperature).text = temp

        } catch (e: Exception) {

        }
    }
}