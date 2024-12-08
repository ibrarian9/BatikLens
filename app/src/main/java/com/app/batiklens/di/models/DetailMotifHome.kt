package com.app.batiklens.di.models

import com.google.gson.annotations.SerializedName

data class DetailMotifHome(

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("jenis")
	val jenis: String,

	@field:SerializedName("sejarah")
	val sejarah: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("arti")
	val arti: String
)
