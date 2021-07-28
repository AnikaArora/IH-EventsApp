package com.example.eventful

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
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

lateinit var CITY: String

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
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


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        val textView = findViewById<TextView>(R.id.text)
        // ...

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val city = "Sacramento"
        val url = "https://app.ticketmaster.com/discovery/v2/events.json?city=$city&apikey={apikey}"

        class EventDetails {
            lateinit var title: String
            lateinit var address: String
        }

        var listOfEvents = mutableListOf<EventDetails>()


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
                    val addressObject = event.getJSONObject("_embedded").getJSONObject("venues")

                    eventInstance.title = event.getString("name")
                    eventInstance.address = addressObject.getJSONObject("address").getString("line1")

                    listOfEvents.add(eventInstance)
                }

            },
            {  })

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

//    val rect1=findViewById<Button>(R.id.rect1);
//    val rect2=findViewById<Button>(R.id.rect2);
//    val rect3=findViewById<Button>(R.id.rect3);

}