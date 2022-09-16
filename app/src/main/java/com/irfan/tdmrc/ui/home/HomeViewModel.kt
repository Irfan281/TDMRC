package com.irfan.tdmrc.ui.home

import androidx.lifecycle.*
import com.irfan.tdmrc.data.datastore.SessionModel
import com.irfan.tdmrc.data.datastore.SessionPreferences
import com.irfan.tdmrc.data.remote.ApiConfig
import com.irfan.tdmrc.data.remote.ApiConfig.Companion.getApiService
import com.irfan.tdmrc.data.remote.GempaResponse
import com.irfan.tdmrc.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val pref: SessionPreferences) : ViewModel(){
    private val _gempa = MutableLiveData<Result>()
    val gempa: LiveData<Result> = _gempa

    private val _peta = MutableLiveData<Result>()
    val peta: LiveData<Result> = _peta

    fun getGempa(){
        viewModelScope.launch {
            flow {
                val result = withContext(Dispatchers.IO){
                    getApiService.getGempa()
                }
                emit(result)
            }.onStart {
                _gempa.value = Result.Loading(true)
            }.onCompletion {
                _gempa.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                _gempa.value = Result.Error(it)
            }.collect{
                _gempa.value = Result.Success(it)
            }
        }
    }

    fun getUserToken(): LiveData<SessionModel> {
        return pref.getToken().asLiveData()
    }

    fun getPeta(token: String) {
        viewModelScope.launch {
            flow {
                val result = withContext(Dispatchers.IO){
                    getApiService.getPeta("Bearer $token")
                }

                emit(result)
            }.onStart {
                _peta.value = Result.Loading(true)
            }.onCompletion {
                _peta.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                _peta.value = Result.Error(it)
            }.collect{
                _peta.value = Result.Success(it)
            }
        }
    }
}