package com.irfan.tdmrc.ui.peta

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.irfan.tdmrc.BuildConfig
import com.irfan.tdmrc.R
import com.irfan.tdmrc.data.remote.PetaResponseItem
import com.irfan.tdmrc.databinding.ActivityDetailPetaBinding
import com.tonyakitori.inc.easyroutes.EasyRoutesDirections
import com.tonyakitori.inc.easyroutes.EasyRoutesDrawer
import com.tonyakitori.inc.easyroutes.enums.TransportationMode
import com.tonyakitori.inc.easyroutes.extensions.drawRoute
import com.tonyakitori.inc.easyroutes.extensions.getGoogleMapsLink
import com.xwray.groupie.GroupieAdapter


class DetailPetaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var p0: GoogleMap
    private lateinit var binding: ActivityDetailPetaBinding

    private lateinit var peta: PetaResponseItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPetaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        peta = intent.getParcelableExtra<PetaResponseItem>("peta") as PetaResponseItem

        binding.tvNama.text = peta.lokasi

        val compose = mutableListOf<String>()

        compose.add("Kabupaten:" + peta.kabupaten)
        compose.add("Desa:" + peta.desa)
        compose.add("Elevasi:" + peta.elev)
        compose.add("Kecamatan:" + peta.kecamatan)
        compose.add("Keterangan:" + peta.keterangan)
        compose.add("Kondisi:" + peta.kondisi)
        compose.add("Lokasi:" + peta.lokasi)
        compose.add("Waktu:" + peta.waktu)

        val detailAdapter = GroupieAdapter()

        for (i in compose){
            detailAdapter.add(DetailItem(i))
        }

        binding.rvInfo.apply {
            layoutManager = GridLayoutManager(this@DetailPetaActivity, 2)
            adapter = detailAdapter
        }

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
    override fun onMapReady(p0: GoogleMap) {

        val origin = LatLng(intent.extras?.get("myLat") as Double, intent.extras?.get("myLong") as Double)
        val destination = LatLng(peta.latitude.toDouble(), peta.longitude.toDouble())

        val placeDirections = EasyRoutesDirections(
            originLatLng = origin,
            destinationLatLng = destination,
            apiKey = BuildConfig.GMK_KEY,
            showDefaultMarkers= false,
            transportationMode = TransportationMode.DRIVING
        )

        val bounds = LatLngBounds.Builder().include(origin).include(destination).build()


        p0.apply {
            addMarker(MarkerOptions()
                .position(LatLng(peta.latitude.toDouble(), peta.longitude.toDouble()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
            addMarker(MarkerOptions()
                .position(origin)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("Lokasi Anda"))
            animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 900,900,15))
        }




        val routeDrawer = EasyRoutesDrawer.Builder(p0)
            .pathWidth(10f)
            .pathColor(Color.GREEN)
            .geodesic(true)
            .previewMode(false)
            .build()

        val markersList = mutableListOf<Marker>()
        p0.drawRoute(
            context = this@DetailPetaActivity,
            easyRoutesDirections = placeDirections,
            markersListCallback = {markers -> markersList.addAll(markers)},
            routeDrawer = routeDrawer,
            googleMapsLink = { url -> Log.d("GoogleLink", url)}
        ){ legs ->
            legs?.forEach {
                Log.d("Point startAddress:", it?.startAddress.toString())
                Log.d("Point endAddress:", it?.endAddress.toString())
                Log.d("Distance:", it?.distance.toString())
                Log.d("Duration:", it?.duration.toString())
            }
        }

        binding.fab.setOnClickListener {
            val url = getGoogleMapsLink(placeDirections)

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}