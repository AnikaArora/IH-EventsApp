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
import com.example.eventful.*
import com.example.eventful.databinding.FragmentHomeBinding
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
            populateView(v)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //override onPostExecute()
    private fun populateView(v : View) {
        try {

            v.findViewById<TextView>(R.id.temperature).text = temp
            v.findViewById<TextView>(R.id.location).text = CITY
            v.findViewById<TextView>(R.id.conditions).text = conditions
            Picasso.get().load(iconURL).resize(120,120).into(v.findViewById<ImageView>(R.id.weather_icon_iv))

        } catch (e: Exception) {
            Log.d("postApiCall Exception", "Exception = $e")
        }
    }
}