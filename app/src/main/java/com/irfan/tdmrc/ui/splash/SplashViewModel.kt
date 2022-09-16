package com.irfan.tdmrc.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.irfan.tdmrc.data.datastore.SessionModel
import com.irfan.tdmrc.data.datastore.SessionPreferences

class SplashViewModel(private val preferences: SessionPreferences) : ViewModel() {
    fun getUserToken(): LiveData<SessionModel> {
        return preferences.getToken().asLiveData()
    }
}