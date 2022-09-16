package com.irfan.tdmrc.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irfan.tdmrc.data.datastore.SessionPreferences
import com.irfan.tdmrc.data.remote.ApiConfig.Companion.getApiService
import com.irfan.tdmrc.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val pref: SessionPreferences) : ViewModel() {

    private val _login = MutableLiveData<Result>()
    val login: LiveData<Result> = _login

    fun postLogin(map : HashMap<String, String>) {
        viewModelScope.launch {
            flow {
                val result = withContext(Dispatchers.IO) {
                    getApiService.postLogin(map)
                }

                emit(result)
            }.onStart {
                _login.value = Result.Loading(true)
            }.onCompletion {
                _login.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                _login.value = Result.Error(it)
            }.collect{
                _login.value = Result.Success(it)

                pref.login(it.token, it.user.firstName, it.userId)
            }
        }
    }
}