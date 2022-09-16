package com.irfan.tdmrc.ui.gempa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.irfan.tdmrc.R
import com.irfan.tdmrc.databinding.ActivityDetailGempaBinding


class DetailGempaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDetailGempaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailGempaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.fab.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val point = intent.extras?.get("koordinat").toString()
        val listPoint = point.split(",")

        val wilayah = intent.extras?.get("wilayah").toString()

        binding.cardGempa.apply {
            tvSkala.text = intent.extras?.get("skala").toString()
            tvTempatGempa.text = wilayah
            tvDalam.text = intent.extras?.get("dalam").toString()
            tvRasa.text = intent.extras?.get("rasa").toString()
            tvWaktu.text = intent.extras?.get("waktu").toString()
        }



        // Add a marker in Sydney and move the camera
        val gempa = LatLng(listPoint[0].toDouble(), listPoint[1].toDouble())
        mMap.apply {
            addMarker(MarkerOptions().position(gempa).title(wilayah))
            moveCamera(CameraUpdateFactory.newLatLng(gempa))
            animateCamera(CameraUpdateFactory.zoomTo( 8.0f ) )
        }

        binding.cardGempa.card.setOnClickListener {
            mMap.apply {
                moveCamera(CameraUpdateFactory.newLatLng(gempa))
                animateCamera(CameraUpdateFactory.zoomTo( 8.0f ) )
            }
        }

    }
}