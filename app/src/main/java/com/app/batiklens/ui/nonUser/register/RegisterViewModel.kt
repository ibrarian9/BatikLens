package com.app.batiklens.ui.nonUser.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import com.app.batiklens.di.models.dto.RegisterDTO
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class RegisterViewModel(private val repo: MainRepository): ViewModel() {

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> = _registerResult

    fun register(registerDTO: RegisterDTO, image: MultipartBody.Part){
        viewModelScope.launch {
            val result = repo.postRegister(registerDTO = registerDTO, image = image)
            _registerResult.value = result
        }
    }
}