package com.example.eventful

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.eventful.databinding.ActivityMainBinding
import com.example.eventful.databinding.WeatherWidgetBinding
import com.example.eventful.ui.WeatherWidget
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.processNextEventInCurrentThread

class EventDetails {
    lateinit var title: String
    lateinit var address: String
    lateinit var longitude: String
    lateinit var latitude: String
}

lateinit var CITY: String
var listOfEvents = mutableListOf<EventDetails>()
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setContentView(R.layout.activity_main)

        CITY = getLocation()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val navView: BottomNavigationView = binding.navView

        val weatherWidget: WeatherWidgetBinding = binding.weatherWidgetMain

        val weatherView: WeatherWidget = WeatherWidget(weatherWidget)

        val appContext = applicationContext
        weatherView.init(appContext)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        val textView = findViewById<TextView>(R.id.text)
        // ...

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val city = "Denver"
        val url = "https://app.ticketmaster.com/discovery/v2/events.json?city=$city&apikey=t3Pw9IXSSkl3TrReE9v2nrm238YfhoXb"


        // Request a string response from the provided URL.
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // Display the first 500 characters of the response string.
                val jsonObject = response.getJSONObject("_embedded")
                val eventsArr = jsonObject.getJSONArray("events")

                for(i in 0 until eventsArr.length()) {
                    val event = eventsArr.getJSONObject(i)
                    val eventInstance = EventDetails()
                    val addressObject = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0)


                    eventInstance.title = event.getString("name")
                    eventInstance.address = addressObject.getJSONObject("address").optString("line1")
                    val longitude = addressObject.getJSONObject("location").optString("longitude")
                    val latitude = addressObject.getJSONObject("location").optString("latitude")
                    eventInstance.longitude = longitude
                    eventInstance.latitude = latitude

                    listOfEvents.add(eventInstance)

                }
        // Add the request to the RequestQueue.
//        queue.add(JsonObjectRequest)

    }

    private fun getLocation(): String {

        var lat : Double
        var lon : Double
        val geocoder : Geocoder = Geocoder(this, Locale.getDefault())
        var addressList : List<Address?>
        var address = "Dallas"

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                            if (location != null) {
                                lat = location.latitude
                                lon = location.longitude
                                addressList = geocoder.getFromLocation(lat, lon, 1)
                                address = addressList[0].toString()
                            }
                        }
                    }

                }
            }

        return address
    }

            },
            { error -> Log.d("error", error.toString()) })
            queue.add(jsonRequest)

    }

}