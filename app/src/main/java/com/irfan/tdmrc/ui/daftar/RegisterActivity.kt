package com.irfan.tdmrc.ui.daftar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.irfan.tdmrc.R
import com.irfan.tdmrc.data.datastore.SessionPreferences
import com.irfan.tdmrc.databinding.ActivityRegisterBinding
import com.irfan.tdmrc.ui.ViewModelFactory
import com.irfan.tdmrc.ui.home.HomeActivity
import com.irfan.tdmrc.ui.login.LoginActivity
import com.irfan.tdmrc.utils.Result

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory(SessionPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding.tvCtaLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.buttonDaftar.setOnClickListener{
            registerViewModel.postRegister(hashMapOf(
                "firstName" to binding.edNama.text.toString(),
                "lastName" to "",
                "email" to binding.edEmail.text.toString(),
                "password" to binding.edPassword.text.toString()
            ))

            registerViewModel.daftar.observe(this) {
                when(it) {
                    is Result.Success<*> -> {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                    is Result.Error -> {
                        binding.buttonDaftar.revertAnimation {
                            binding.buttonDaftar.background = ActivityCompat.getDrawable(this, R.drawable.button_main)
                        }
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        binding.buttonDaftar.startAnimation()
                    }
                }
            }
        }
    }
}