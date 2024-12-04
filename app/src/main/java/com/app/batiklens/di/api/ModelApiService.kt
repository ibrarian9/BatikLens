package com.app.batiklens.di.api

import com.app.batiklens.di.models.PredictModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ModelApiService {

    @Multipart
    @POST("/predict")
    suspend fun predictModel(
        @Part scannerImage: MultipartBody.Part
    ): Response<PredictModel>
}