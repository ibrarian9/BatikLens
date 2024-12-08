package com.app.batiklens.di

import com.app.batiklens.di.api.ApiService
import com.app.batiklens.di.api.ModelApiService
import com.app.batiklens.ui.nonUser.login.LoginViewModel
import com.app.batiklens.ui.nonUser.register.RegisterViewModel
import com.app.batiklens.ui.user.berita.BeritaViewModel
import com.app.batiklens.ui.user.detailMotif.DetailViewModel
import com.app.batiklens.ui.user.editProfile.EditProfileViewModel
import com.app.batiklens.ui.user.history.HistoryViewModel
import com.app.batiklens.ui.user.home.HomeViewModel
import com.app.batiklens.ui.user.motif.MotifListViewModel
import com.app.batiklens.ui.user.profil.ProfileViewModel
import com.app.batiklens.ui.user.provinsi.MotifViewModel
import com.app.batiklens.ui.user.result.ResultViewModel
import com.app.batiklens.ui.user.scanBatik.ScannerViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single(named("MainApi")) {
        val url = "https://api-tysphbhbhq-uc.a.run.app"

        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val authInterceptor = Interceptor {
            val req = it.request()
            val reqHeaders = req.newBuilder()
                .build()
            it.proceed(reqHeaders)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    single(named("ModelApi")) {
        val url = "https://batiklast-516544967646.asia-southeast1.run.app"

        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val authInterceptor = Interceptor {
            val req = it.request()
            val reqHeaders = req.newBuilder()
                .build()
            it.proceed(reqHeaders)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // Provide ApiService instance
    single(named("MainApi")) { get<Retrofit>(named("MainApi")).create(ApiService::class.java) }
    single(named("ModelApi")) { get<Retrofit>(named("ModelApi")).create(ModelApiService::class.java) }

    // Provide Repository
    single {
        MainRepository(
            apiService = get(named("MainApi")),
            modelApiService = get(named("ModelApi"))
        )
    }

    // Provide ViewModel
    viewModel { HomeViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { BeritaViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { MotifViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { ScannerViewModel(get()) }
    viewModel { EditProfileViewModel(get()) }
    viewModel { MotifListViewModel(get()) }
    viewModel { ResultViewModel(get()) }
}