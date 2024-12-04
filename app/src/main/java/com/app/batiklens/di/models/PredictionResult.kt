package com.app.batiklens.di.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictionResult(
    val nameModul: String,
    val time: Double,
    val confidance: String
): Parcelable
