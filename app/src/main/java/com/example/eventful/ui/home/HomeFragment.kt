package com.example.eventful.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.eventful.CITY
import com.example.eventful.R
import com.example.eventful.databinding.FragmentHomeBinding
import com.example.eventful.listOfEvents
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import org.json.JSONObject


class HomeFragment : Fragment() {

    private var API: String = "991c4746ce85b4c70a462313962cd22a"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        Log.d("CreateView", "New view created")
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val v : View = root.findViewById(R.id.fragment_home_container)!!

        if (savedInstanceState == null) {
            init(this.requireContext(), v)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init(context: Context, v : View) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        Log.d("weatherWidget", "start of init")
//        Log.d("City", "CITY = $CITY")

        val queue = Volley.newRequestQueue(context)
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=imperial&appid=$API"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                postApiCall(response, v)
                Log.d("response", "Post API response = $response")
            },
            {error -> Log.d("error", "Post API error = $error")  })
        queue.add(jsonObjectRequest)
    }

    //override onPostExecute()
    private fun postApiCall(result: JSONObject, v : View) {
        try {
            val main = result.getJSONObject("main")
            val temp = main.getString("temp") + "\u2103"
            val weather = result.getJSONArray("weather")
            val conditions = weather.getJSONObject(0).getString("main")
            val icon = weather.getJSONObject(0).getString("icon")
            val iconURL = "https://openweathermap.org/img/w/$icon.png"

            v.findViewById<TextView>(R.id.temperature).text = temp
            v.findViewById<TextView>(R.id.location).text = CITY
            v.findViewById<TextView>(R.id.conditions).text = conditions
            Picasso.get().load(iconURL).resize(120,120).into(v.findViewById<ImageView>(R.id.weather_icon_iv))

        } catch (e: Exception) {
            Log.d("postApiCall Exception", "Exception = $e")
        }
    }
}