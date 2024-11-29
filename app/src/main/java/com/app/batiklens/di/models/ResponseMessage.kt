package com.app.batiklens.di.models

import com.google.gson.annotations.SerializedName

data class ResponseMessage(
    @field:SerializedName("message")
    val message: String,
)
