package com.app.batiklens.di.api

import com.app.batiklens.di.models.ArtikelModelItem
import com.app.batiklens.di.models.MotifModelItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/news")
    suspend fun semuaArtikel(): Response<List<ArtikelModelItem>>

    @GET("/motif")
    suspend fun semuaMotifHome(): Response<List<MotifModelItem>>

}