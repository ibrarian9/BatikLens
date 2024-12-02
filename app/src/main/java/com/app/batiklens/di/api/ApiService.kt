package com.app.batiklens.di.api

import com.app.batiklens.di.models.ArtikelModelItem
import com.app.batiklens.di.models.DetailProvinsi
import com.app.batiklens.di.models.ListBatikItem
import com.app.batiklens.di.models.MotifModelItem
import com.app.batiklens.di.models.ProvinsiMotifModelItem
import com.app.batiklens.di.models.ResponseMessage
import com.app.batiklens.di.models.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Multipart
    @POST("/register")
    suspend fun register(
        @PartMap registerDTO: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part profileImage: MultipartBody.Part
    ): Response<ResponseMessage>

    @GET("/news")
    suspend fun semuaArtikel(): Response<List<ArtikelModelItem>>

    @GET("/news/search")
    suspend fun searchArtikel(
        @Query("search") search: String
    ): Response<List<ArtikelModelItem>>

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

    @GET("/user/{id}")
    suspend fun getDetailProfile(
        @Path("id") id: String
    ): Response<UserModel>
}