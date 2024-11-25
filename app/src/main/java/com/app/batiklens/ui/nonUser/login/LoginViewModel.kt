package com.app.batiklens.ui.nonUser.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: MainRepository): ViewModel() {

    private val _loginResult = MutableLiveData<Result<FirebaseUser?>>()
    val loginResult: LiveData<Result<FirebaseUser?>> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repo.login(email, password)
            _loginResult.value = result
        }
    }
}