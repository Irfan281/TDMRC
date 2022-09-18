package com.irfan.tdmrc.data.remote

import com.google.gson.annotations.SerializedName

data class PetaResponse(

	@field:SerializedName("PetaResponse")
	val petaResponse: List<PetaResponseItem>
)

data class PetaResponseItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("lokasi")
	val lokasi: String,

	@field:SerializedName("Latitude")
	val latitude: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("Longitude")
	val longitude: String,

	var jarak: Double
)
