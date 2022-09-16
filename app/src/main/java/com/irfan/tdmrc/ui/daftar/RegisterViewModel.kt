package com.irfan.tdmrc.ui.daftar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irfan.tdmrc.data.datastore.SessionPreferences
import com.irfan.tdmrc.data.remote.ApiConfig.Companion.getApiService
import com.irfan.tdmrc.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(private val pref: SessionPreferences) : ViewModel() {

    private val _daftar = MutableLiveData<Result>()
    val daftar: LiveData<Result> = _daftar

    fun postRegister(map : HashMap<String, String>) {
        viewModelScope.launch {
            flow {
                val result = withContext(Dispatchers.IO) {
                    getApiService.postRegister(map)
                }

                emit(result)
            }.onStart {
                _daftar.value= Result.Loading(true)
            }.onCompletion {
                _daftar.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                _daftar.value = Result.Error(it)
            }.collect{
                _daftar.value = Result.Success(it)

                pref.login(it.token, it.user.firstName, it.userId)
            }
        }
    }
}