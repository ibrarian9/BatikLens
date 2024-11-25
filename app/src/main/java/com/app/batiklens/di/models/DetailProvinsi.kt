package com.app.batiklens.di.models

import com.google.gson.annotations.SerializedName

data class DetailProvinsi(

	@field:SerializedName("ListBatik")
	val listBatik: List<ListBatikItem>,

	@field:SerializedName("Foto")
	val foto: String,

	@field:SerializedName("Provinsi")
	val provinsi: String,

	@field:SerializedName("id")
	val id: Int
)

data class ListBatikItem(

	@field:SerializedName("namaMotif")
	val namaMotif: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("Arti_Motif")
	val artiMotif: String,

	@field:SerializedName("Sejarah_Batik")
	val sejarahBatik: String
)
