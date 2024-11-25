package com.app.batiklens.ui.nonUser.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class RegisterViewModel(private val repo: MainRepository): ViewModel() {

    private val _registerResult = MutableLiveData<Result<FirebaseUser?>>()
    val registerResult: LiveData<Result<FirebaseUser?>> = _registerResult

    fun register(email: String, password: String){
        viewModelScope.launch {
            val result = repo.register(email = email, password = password)
            _registerResult.value = result
        }
    }
}