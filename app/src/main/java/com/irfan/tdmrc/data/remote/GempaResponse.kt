package com.irfan.tdmrc.data.remote

import com.google.gson.annotations.SerializedName

data class GempaResponse(

	@field:SerializedName("GempaResponse")
	val gempaResponse: List<GempaResponseItem>
)

data class GempaResponseItem(
	@field:SerializedName("Magnitude")
	val magnitude: String,

	@field:SerializedName("Wilayah")
	val wilayah: String,

	@field:SerializedName("Kedalaman")
	val kedalaman: String,

	@field:SerializedName("Dirasakan")
	val dirasakan: String,

	@field:SerializedName("DateTime")
	val dateTime: String,

	@field:SerializedName("point")
	val point: Point
)

data class Point(

	@field:SerializedName("coordinates")
	val coordinates: String
)
