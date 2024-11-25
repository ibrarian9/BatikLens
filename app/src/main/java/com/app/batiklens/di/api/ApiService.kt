package com.app.batiklens.di.api

import com.app.batiklens.di.models.ArtikelModelItem
import com.app.batiklens.di.models.DetailProvinsi
import com.app.batiklens.di.models.ListBatikItem
import com.app.batiklens.di.models.MotifModelItem
import com.app.batiklens.di.models.ProvinsiMotifModelItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/news")
    suspend fun semuaArtikel(): Response<List<ArtikelModelItem>>

    @GET("/news/{id}")
    suspend fun detailArtikel(@Path("id") id: Int): Response<ArtikelModelItem>

    @GET("/motif")
    suspend fun semuaMotifHome(): Response<List<MotifModelItem>>

    @GET("/provinsi")
    suspend fun semuaProvinsiMotif(): Response<List<ProvinsiMotifModelItem>>

    @GET("/provinsi/{id}")
    suspend fun detailProvinsi(@Path("id") id: Int): Response<DetailProvinsi>

    @GET("/provinsi/{id}/batik/{motifId}")
    suspend fun detailMotifId(
        @Path("id") id: Int,
        @Path("motifId") motifId: Int
    ): Response<ListBatikItem>

}