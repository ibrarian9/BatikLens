package com.app.batiklens.di.models

import com.google.gson.annotations.SerializedName

data class PredictModel(

	@field:SerializedName("predicted_label")
	val predictedLabel: String
)
