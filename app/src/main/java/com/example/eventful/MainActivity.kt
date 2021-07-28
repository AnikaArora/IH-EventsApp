package com.example.eventful

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.eventful.databinding.ActivityMainBinding
import com.example.eventful.databinding.WeatherWidgetBinding
import com.example.eventful.ui.WeatherWidget
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

lateinit var CITY: String

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
}