package com.irfan.tdmrc.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.irfan.tdmrc.R
import com.irfan.tdmrc.data.datastore.SessionPreferences
import com.irfan.tdmrc.databinding.ActivityUploadBinding
import com.irfan.tdmrc.ui.home.HomeViewModel
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL
import javax.net.ssl.HttpsURLConnection

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory(SessionPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parseJSON()
    }


    private fun parseJSON() {
        val token = intent.extras?.getString("tkn")
        GlobalScope.launch(Dispatchers.IO) {
            val url =
                URL("https://raw.githubusercontent.com/Irfan281/uxflow/main/data.json")
            val httpsURLConnection = url.openConnection() as HttpsURLConnection
            httpsURLConnection.setRequestProperty(
                "Accept",
                "application/json"
            ) // The format of response we want to get from the server
            httpsURLConnection.requestMethod = "GET"
            httpsURLConnection.doInput = true
            httpsURLConnection.doOutput = false
            // Check if the connection is successful
            val responseCode = httpsURLConnection.responseCode
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {


                    val jsonArray = JSONTokener(response).nextValue() as JSONArray

                    for (i in 0 until jsonArray.length()) {

                        val properties = jsonArray.getJSONObject(i).getJSONObject("properties")

                        val Lokasi = properties.getString("Lokasi")
                        Log.i("Lokasi: ", Lokasi)

                        val Lat = properties.getString("Lat")
                        Log.i("Lat: ", Lat)

                        val Lon = properties.getString("Lon")
                        Log.i("Lon: ", Lon)


                        var Elev : String = ""
                        var Waktu_temp : String = ""
                        var Kondisi_Ra : String = ""
                        try {
                            Elev = properties.getString("Elev")
                            Waktu_temp = properties.getString("Waktu_temp")
                            Kondisi_Ra = properties.getString("Kondisi_Ra")
                            Log.i("Elev: ", Elev)
                            Log.i("Waktu_temp: ", Waktu_temp)
                        } catch (e: Exception) {
                            Elev = "-"
                            Waktu_temp = "-"
                            Kondisi_Ra = "-"
                            Log.i("Elev: ", "null")
                            Log.i("Waktu_temp: ", "null")
                        }

                        val Desa = properties.getString("Desa")
                        Log.i("Desa: ", Desa)

                        val Kecamatan = properties.getString("Kecamatan")
                        Log.i("Kecamatan: ", Kecamatan)

                        var Kabupaten = properties.getString("Kabupaten")
                        if (Kabupaten.equals("A Besar")){Kabupaten = "Aceh Besar"}
                        Log.i("Kabupaten: ", Kabupaten)

                        val Keterangan = properties.getString("Keterangan")
                        Log.i("Keterangan: ", Keterangan)

                        val map = hashMapOf(
                            "lokasi" to Lokasi,
                            "Latitude" to Lat,
                            "Longitude" to Lon,
                            "elev" to Elev,
                            "waktu" to Waktu_temp,
                            "kondisi" to Kondisi_Ra,
                            "desa" to Desa,
                            "kecamatan" to Kecamatan,
                            "kabupaten" to Kabupaten,
                            "keterangan" to Keterangan
                        )

                        if (token != null) {
                            homeViewModel.setPeta(token,map)
                            Toast.makeText(this@UploadActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }
    }
}