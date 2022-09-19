package com.irfan.tdmrc.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PetaResponse(

	@field:SerializedName("PetaResponse")
	val petaResponse: List<PetaResponseItem>
)

@Parcelize
data class PetaResponseItem(

	@field:SerializedName("desa")
	val desa: String,

	@field:SerializedName("keterangan")
	val keterangan: String,

	@field:SerializedName("kabupaten")
	val kabupaten: String,

	@field:SerializedName("kondisi")
	val kondisi: String,

	@field:SerializedName("elev")
	val elev: String,

	@field:SerializedName("kecamatan")
	val kecamatan: String,

	@field:SerializedName("waktu")
	val waktu: String,

	@field:SerializedName("lokasi")
	val lokasi: String,

	@field:SerializedName("Latitude")
	val latitude: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("Longitude")
	val longitude: String,

	var jarak: Double
): Parcelable
