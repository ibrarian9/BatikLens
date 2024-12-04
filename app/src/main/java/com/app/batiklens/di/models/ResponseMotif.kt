package com.app.batiklens.di.models

import com.google.gson.annotations.SerializedName

data class ResponseMotifItem(

	@field:SerializedName("namaMotif")
	val namaMotif: String,

	@field:SerializedName("provinsi")
	val provinsi: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("Arti_Motif")
	val artiMotif: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("Sejarah_Batik")
	val sejarahBatik: String
)
