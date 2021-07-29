package com.example.eventful.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.eventful.EventDetails
import com.example.eventful.R
import com.example.eventful.databinding.FragmentHomeBinding
import com.example.eventful.listOfEvents
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var mMap: GoogleMap
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        for(i in listOfEvents.indices) {
            val longitude = listOfEvents[i].longitude
            val latitude = listOfEvents[i].latitude
            if (!longitude.isNullOrEmpty() && !latitude.isNullOrEmpty()) {
                val location = LatLng(latitude.toDouble(), longitude.toDouble())
                mMap.addMarker(MarkerOptions().position(location).title("Marker in location"))
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val losangeles = LatLng(34.0522, -118.2437)
        mMap.addMarker(MarkerOptions().position(losangeles).title("Marker in LA"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(losangeles))
    }
}