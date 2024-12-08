package com.app.batiklens.di.models

import com.google.gson.annotations.SerializedName

data class PredictLabel(

	@field:SerializedName("id_motif")
	val idMotif: Int,

	@field:SerializedName("confidence")
	val confidence: Double,

	@field:SerializedName("predicted_label")
	val predictedLabel: String,

	@field:SerializedName("id_provinsi")
	val idProvinsi: Int
)
