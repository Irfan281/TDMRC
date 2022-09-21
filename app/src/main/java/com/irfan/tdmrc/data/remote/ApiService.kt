package com.irfan.tdmrc.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @GET("quakes/datas")
    suspend fun getGempa() : List<GempaResponseItem>

    @GET("Maps")
    suspend fun getPeta(
        @Header("Authorization") header: String
    ) : List<PetaResponseItem>

    @POST("auth/register")
    suspend fun postRegister(
        @Body params: HashMap<String, String>
    ) : AuthResponse

    @POST("auth/login")
    suspend fun postLogin(
        @Body params: HashMap<String, String>
    ) : AuthResponse

    @POST("Maps")
    suspend fun postPeta(
        @Header("Authorization") header: String,
        @Body params: HashMap<String, String>
    ) : PetaResponseItem
}