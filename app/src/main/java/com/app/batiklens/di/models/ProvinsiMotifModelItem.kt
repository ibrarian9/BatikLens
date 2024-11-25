package com.app.batiklens.di.models

import com.google.gson.annotations.SerializedName

data class ProvinsiMotifModelItem(

	@field:SerializedName("Foto")
	val foto: String,

	@field:SerializedName("Provinsi")
	val provinsi: String,

	@field:SerializedName("id")
	val id: Int
)