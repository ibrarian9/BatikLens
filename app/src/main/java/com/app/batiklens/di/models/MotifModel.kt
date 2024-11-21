package com.app.batiklens.di.models

import com.google.gson.annotations.SerializedName

data class MotifModelItem(

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("jenis")
	val jenis: String,

	@field:SerializedName("id")
	val id: Int
)
