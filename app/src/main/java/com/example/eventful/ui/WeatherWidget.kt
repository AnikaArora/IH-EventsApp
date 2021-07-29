package com.example.eventful.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.icu.number.NumberFormatter.with
import android.location.*
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.eventful.CITY
import com.example.eventful.R
import com.example.eventful.databinding.WeatherWidgetBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.util.*

class WeatherWidget(private val weatherView: WeatherWidgetBinding) {

    var API: String = "991c4746ce85b4c70a462313962cd22a"

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var context : Context

    fun init(context: Context) {
        this.context = context
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context)
        Log.d("weatherWidget", "start of init")
        Log.d("City", "CITY = $CITY")

        val queue = Volley.newRequestQueue(this.context)
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$CITY&appid=$API"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                postApiCall(response)
                Log.d("response", "Post API response = $response")
        },
            {error -> Log.d("error", "Post API error = $error")  })
        queue.add(jsonObjectRequest)
    }

    //override onPostExecute()
    private fun postApiCall(result: JSONObject) {
            try {
                val main = result.getJSONObject("main")
                val temp = main.getString("temp") + "\u2103"
                val weather = result.getJSONObject("weather")
                val conditions = weather.getJSONObject("main").toString()
                val icon = weather.getJSONObject("icon").toString()
                val iconURL = "http://openweathermap.org/img/w/$icon.png"

                val v : View = weatherView.mainContainer

                v.findViewById<TextView>(R.id.temperature).text = temp
                v.findViewById<TextView>(R.id.location).text = CITY
                v.findViewById<TextView>(R.id.conditions).text = conditions
                Picasso.get().load(iconURL).resize(30,30).into(v.findViewById<ImageView>(R.id.icon))

            } catch (e: Exception) {

            }
    }
}
