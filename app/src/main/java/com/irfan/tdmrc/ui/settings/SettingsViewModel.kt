package com.irfan.tdmrc.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irfan.tdmrc.data.datastore.SessionPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val pref: SessionPreferences) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}