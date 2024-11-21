package com.app.batiklens.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.batiklens.di.api.ApiService
import com.app.batiklens.di.models.ArtikelModelItem
import com.app.batiklens.di.models.MotifModelItem
import kotlinx.coroutines.delay

class MainRepository(
    private val apiService: ApiService
) {
    private val timeLoading: Long = 1000

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    suspend fun getAllArticle(): List<ArtikelModelItem> {
        _loading.value = true
        return try {
            delay(timeLoading)
            val res = apiService.semuaArtikel()
            if (res.isSuccessful){
                res.body() ?: emptyList()
            } else {
                _error.value = "Error : ${res.code()} - ${res.message()}"
               emptyList()
            }
        } catch (e: Exception){
            _error.value = e.toString()
            emptyList()
        } finally {
            _loading.value = false
        }
    }

    suspend fun getAllMotifHome(): List<MotifModelItem> {
        _loading.value = true
        return try {
            delay(timeLoading)
            val res = apiService.semuaMotifHome()
            if (res.isSuccessful){
                res.body() ?: emptyList()
            } else {
                _error.value = "Error : ${res.code()} - ${res.message()}"
               emptyList()
            }
        } catch (e: Exception){
            _error.value = e.toString()
            emptyList()
        } finally {
            _loading.value = false
        }
    }
}