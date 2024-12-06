package com.app.batiklens.di.models

import com.google.gson.annotations.SerializedName

data class ArtikelModelItem(

	@field:SerializedName("tahun")
	val tahun: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("tanggal")
	val tanggal: String,

	@field:SerializedName("judul")
	val judul: String,

	@field:SerializedName("bulan")
	val bulan: String
)
