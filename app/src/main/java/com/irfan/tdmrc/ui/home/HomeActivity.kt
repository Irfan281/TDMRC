package com.irfan.tdmrc.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.irfan.tdmrc.R
import com.irfan.tdmrc.data.datastore.SessionPreferences
import com.irfan.tdmrc.data.remote.GempaResponseItem
import com.irfan.tdmrc.data.remote.PetaResponseItem
import com.irfan.tdmrc.databinding.ActivityHomeBinding
import com.irfan.tdmrc.ui.ViewModelFactory
import com.irfan.tdmrc.utils.Result
import com.xwray.groupie.GroupieAdapter
import java.util.*
import android.provider.Settings
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import me.moallemi.tools.extension.date.format
import me.moallemi.tools.extension.date.now


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory(SessionPreferences.getInstance(dataStore))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.refreshLayout.setOnRefreshListener {
            getLocation()
            setGempa()

            binding.refreshLayout.isRefreshing = false
        }
        setGempa()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()

        binding.tvDate.text = now().format("dd MMMM yyyy")

    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        var index = 0
                        if (list[0].subAdminArea.contains("Banda Aceh",  true)) {
                            index = 0
                        } else if (list[0].subAdminArea.contains("Aceh Besar",  true)) {
                            index = 1
                        } else if (list[0].subAdminArea.contains("Nagan",  true)) {
                            index = 2
                        } else if (list[0].subAdminArea.contains("Aceh Barat",  true)) {
                            index = 3
                        } else if (list[0].subAdminArea.contains("Aceh Jaya",  true)) {
                            index = 4
                        }

                        binding.actLokasi.setText(binding.actLokasi.adapter.getItem(index).toString(), false)

                        setPeta(location.latitude, location.longitude)
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun setPeta(myLat: Double, myLong: Double) {
        val petaAdapter = GroupieAdapter()

        homeViewModel.getUserToken().observe(this){
            homeViewModel.getPeta(it.token)
        }

        homeViewModel.peta.observe(this){
            when(it){
                is Result.Success<*> -> {
                    for (i in it.data as List<PetaResponseItem>){
                        val results = FloatArray(1)
                        Location.distanceBetween(myLat, myLong, i.latitude.toDouble(), i.longitude.toDouble(), results)
                        i.jarak = (results[0] / 1000).toDouble()
                    }

                    val sortedList = it.data.sortedBy { it.jarak }

                    for (i in sortedList){
                        petaAdapter.add(PetaItem(i))
                    }
                }
                is Result.Loading -> {
                    binding.refreshLayout.isRefreshing = it.isLoading
                }
                is Result.Error -> {
                    Log.d("error ges", it.exception.toString())
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.rvPeta.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = petaAdapter
        }


    }

    private fun setGempa() {
        val gempaAdapter = GroupieAdapter()
        homeViewModel.getGempa()
        homeViewModel.gempa.observe(this){
            when(it){
                is Result.Success<*> -> {
                    Log.d("error ges", it.data.toString())

                    for (i in it.data as MutableList<*>){
                        gempaAdapter.add(GempaItem(i as GempaResponseItem))
                    }
                }
                is Result.Loading -> {
                    binding.refreshLayout.isRefreshing = it.isLoading
                }
                is Result.Error -> {
                    Log.d("error ges", it.exception.toString())
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.rvGempa.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = gempaAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        val lokasi = resources.getStringArray(R.array.lokasi)
        val arrayAdapter = ArrayAdapter(this, R.layout.item_dropdown, lokasi)
        binding.actLokasi.setAdapter(arrayAdapter)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }
}