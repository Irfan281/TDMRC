package com.irfan.tdmrc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.irfan.tdmrc.data.datastore.SessionPreferences
import com.irfan.tdmrc.ui.daftar.RegisterViewModel
import com.irfan.tdmrc.ui.home.HomeViewModel
import com.irfan.tdmrc.ui.login.LoginViewModel
import com.irfan.tdmrc.ui.splash.SplashViewModel

class ViewModelFactory(private val preferences: SessionPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(preferences) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(preferences) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(preferences) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(preferences) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}