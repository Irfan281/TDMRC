package com.irfan.tdmrc.ui.settings

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.irfan.tdmrc.R
import com.irfan.tdmrc.data.datastore.SessionPreferences
import com.irfan.tdmrc.data.datastore.SessionPreferences.Companion.getInstance
import com.irfan.tdmrc.databinding.ActivitySettingsBinding
import com.irfan.tdmrc.ui.ViewModelFactory

import com.irfan.tdmrc.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val settingsViewModel by viewModels<SettingsViewModel> {
        ViewModelFactory(SessionPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }


    private fun setupAction() {
        binding.apply {
            Logout.setOnClickListener {
                Toast.makeText(
                    this@SettingsActivity,
                    resources.getString(R.string.logout_status),
                    Toast.LENGTH_SHORT
                ).show()
                settingsViewModel.logout()
                val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }
    }
}