package com.app.batiklens.di

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.batiklens.di.api.ApiService
import com.app.batiklens.di.api.ModelApiService
import com.app.batiklens.di.models.ArtikelModelItem
import com.app.batiklens.di.models.ListBatikItem
import com.app.batiklens.di.models.MotifModelItem
import com.app.batiklens.di.models.ProvinsiMotifModelItem
import com.app.batiklens.di.models.RegisterDTO
import com.app.batiklens.di.models.ResponseMotifItem
import com.app.batiklens.di.models.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class MainRepository(
    private val apiService: ApiService,
    private val modelApiService: ModelApiService
) {
    private val timeLoading: Long = 1000
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _image = MutableLiveData<String>()
    val image: LiveData<String> get() = _image

    private val _nameProvinsi = MutableLiveData<String>()
    val provinsi: LiveData<String> get() = _nameProvinsi

    fun logout() {
        try {
            Firebase.auth.signOut()
        } catch (e: Exception){
            _error.value = e.message.toString()
        }
    }

    suspend fun cariMotif(query: String): List<ResponseMotifItem> {
        _loading.value = true

        return try {
            val res = apiService.cariMotif(query)
            if (res.isSuccessful){
                res.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception){
            _error.value = e.toString()
            emptyList()
        } finally {
            _loading.value = false
        }
    }

    suspend fun predictModel(image: File): Result<String> {
        _loading.value = true

        val photo = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", image.name, photo)

        return try {
            val res = modelApiService.predictModel(imageMultipart)
            Log.e("Error guys", res.toString())
            if (res.isSuccessful){
                Result.success(res.body()?.predictedLabel ?: "Predict Berhasil")
            } else {
                Result.failure(Exception("Predict Gagal"))
            }
        } catch (e: Exception){
            Result.failure(e)
        } finally {
            _loading.value = false
        }

    }

    suspend fun getDetailProfil(id: String): UserModel? {
        _loading.value = true
        return try {
            val res = apiService.getDetailProfile(id = id)
            if (res.isSuccessful){
                res.body()
            } else {
                _error.value = "Data tidak Ada"
                null
            }
        } catch (e: Exception){
            _error.value = e.message.toString()
            null
        } finally {
            _loading.value = false
        }
    }

    suspend fun login(email: String, password: String): Result<FirebaseUser?> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(firebaseAuth.currentUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun postRegister(
        registerDTO: RegisterDTO,
        image: MultipartBody.Part
    ): Result<String> {
        _loading.value = true
        return try {
            val registerMap = mapOf(
                "email" to registerDTO.email.toRequestBody("text/plain".toMediaTypeOrNull()),
                "name" to registerDTO.name.toRequestBody("text/plain".toMediaTypeOrNull()),
                "password" to registerDTO.password.toRequestBody("text/plain".toMediaTypeOrNull()),
                "confirmPassword" to registerDTO.confirmPassword.toRequestBody("text/plain".toMediaTypeOrNull()),
            )

            val res = apiService.register(registerDTO = registerMap, profileImage = image)
            if (res.isSuccessful){
                Result.success(res.body()?.message ?: "Upload is Successfully")
            } else {
                Result.failure(Exception("Upload Error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            _loading.value = false
        }
    }

    suspend fun getDetailArtikel(id: Int): ArtikelModelItem? {
        _loading.value = true
        return try {
            delay(timeLoading)
            val res = apiService.detailArtikel(id)
            if (res.isSuccessful) {
                res.body()
            } else {
                _error.value = "Data Not Found"
                null
            }
        } catch (e: Exception){
            _error.value = e.toString()
            null
        } finally {
            _loading.value = false
        }
    }

    suspend fun getDetailMotif(id: Int, motifId: Int): ListBatikItem? {
        _loading.value = true
        return try {
            val res = apiService.detailMotifId(id, motifId)
            if (res.isSuccessful) {
                res.body()
            } else {
                _error.value = "Error : ${res.code()} - ${res.message()}"
                null
            }
        } catch (e: Exception){
            _error.value = e.toString()
            null
        } finally {
            _loading.value = false
        }
    }

    suspend fun getAllProvinsi(): List<ProvinsiMotifModelItem> {
        _loading.value = true
        return try {
            delay(timeLoading)
            val res = apiService.semuaProvinsiMotif()
            if (res.isSuccessful) {
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

    suspend fun getAllListMotif(id: Int): List<ListBatikItem> {
        _loading.value = true
        return try {
            val res = apiService.detailProvinsi(id = id)
            if (res.isSuccessful){
                res.body()?.let {
                    _nameProvinsi.value = it.provinsi
                    _image.value = it.foto
                }
                res.body()?.listBatik ?: emptyList()
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

    suspend fun searchArtikel(search: String): List<ArtikelModelItem> {
        _loading.value = true
        return try {
            delay(timeLoading)
            val res = apiService.searchArtikel(search)
            if (res.isSuccessful){
                res.body() ?: emptyList()
            } else {
                _error.value = "Error : ${res.code()} - ${res.message()}"
                emptyList()
            }
        } catch (e: Exception) {
            _error.value = e.toString()
            emptyList()
        } finally {
            _loading.value = false
        }
    }
}