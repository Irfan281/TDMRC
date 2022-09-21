package com.irfan.tdmrc.ui.tes

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.irfan.tdmrc.R
import com.irfan.tdmrc.data.datastore.SessionPreferences
import com.irfan.tdmrc.data.remote.PetaResponseItem
import com.irfan.tdmrc.databinding.ActivityAllLocationBinding
import com.irfan.tdmrc.ui.ViewModelFactory
import com.irfan.tdmrc.ui.home.HomeViewModel
import com.irfan.tdmrc.ui.home.PetaItem
import com.irfan.tdmrc.utils.Result
import com.xwray.groupie.GroupieAdapter

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AllLocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllLocationBinding
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory(SessionPreferences.getInstance(dataStore))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val petaAdapter = GroupieAdapter()

        val data = intent.extras?.get("tkn")

        homeViewModel.getPeta(data.toString())
        homeViewModel.peta.observe(this){
            when(it){
                is Result.Success<*> -> {
                    binding.tvKab.text = getText(R.string.semua)
                    for (i in it.data as List<PetaResponseItem>){
                        petaAdapter.add(AllLocationItem(i))
                    }

                    binding.chipPilih.setOnCheckedStateChangeListener { group, checkedIds ->
                        petaAdapter.clear()

                        if (checkedIds.isNotEmpty()){
                            val chip = group.findViewById<Chip>(checkedIds[0]).text.toString()
                            binding.tvKab.text = chip

                            for (i in it.data){
                                if (i.kabupaten == chip){
                                    petaAdapter.add(AllLocationItem(i))
                                }
                            }
                        } else {
                            binding.tvKab.text = getText(R.string.semua)
                            for (i in it.data){
                                petaAdapter.add(AllLocationItem(i))
                            }
                        }
                    }
                }
                is Result.Loading -> {
                    binding.progress.isVisible = it.isLoading
                }
                is Result.Error -> {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.rvAll.apply {
            layoutManager = LinearLayoutManager(this@AllLocationActivity)
            adapter = petaAdapter
        }
        petaAdapter.notifyDataSetChanged()
    }

}