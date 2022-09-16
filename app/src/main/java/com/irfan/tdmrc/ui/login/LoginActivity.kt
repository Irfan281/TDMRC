package com.irfan.tdmrc.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.irfan.tdmrc.R
import com.irfan.tdmrc.data.datastore.SessionPreferences
import com.irfan.tdmrc.databinding.ActivityLoginBinding
import com.irfan.tdmrc.ui.ViewModelFactory
import com.irfan.tdmrc.ui.daftar.RegisterActivity
import com.irfan.tdmrc.ui.home.HomeActivity
import com.irfan.tdmrc.utils.Result

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory(SessionPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvCtaDaftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener {
            loginViewModel.postLogin(hashMapOf(
                "email" to binding.edEmail.text.toString(),
                "password" to binding.edPassword.text.toString()
            ))

            loginViewModel.login.observe(this){
                when(it) {
                    is Result.Success<*> -> {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                    is Result.Error -> {
                        binding.buttonLogin.revertAnimation{
                            binding.buttonLogin.background = ActivityCompat.getDrawable(this, R.drawable.button_main)
                        }
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        binding.buttonLogin.startAnimation()
                    }
                }
            }
        }
    }
}