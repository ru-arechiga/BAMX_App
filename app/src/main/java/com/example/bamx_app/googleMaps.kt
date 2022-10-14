package com.example.bamx_app

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class googleMaps : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        val bamxGDL = LatLng(20.655955781826687, -103.35519252907912)
        googleMap.addMarker(MarkerOptions().position(bamxGDL).title("BAMX Guadalajara"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bamxGDL, 15f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_google_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}