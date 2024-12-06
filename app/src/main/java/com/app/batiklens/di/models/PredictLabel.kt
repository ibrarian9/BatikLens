package com.app.batiklens.di.models

import com.google.gson.annotations.SerializedName

data class PredictLabel(

	@field:SerializedName("predicted_label")
	val predictedLabel: PredictedLabel
)

data class PredictedLabel(

	@field:SerializedName("id_motif")
	val idMotif: Int,

	@field:SerializedName("predicted_label")
	val predictedLabel: String,

	@field:SerializedName("id_provinsi")
	val idProvinsi: Int
)
