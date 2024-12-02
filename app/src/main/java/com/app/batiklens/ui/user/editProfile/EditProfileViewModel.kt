package com.app.batiklens.ui.user.editProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import com.app.batiklens.di.models.UserModel
import kotlinx.coroutines.launch

class EditProfileViewModel(private val repo: MainRepository): ViewModel() {

    private val _detailProfil = MutableLiveData<UserModel?>()
    val detailProfil: LiveData<UserModel?> = _detailProfil

    fun getDetailProfile(id: String) {
        viewModelScope.launch {
            val res = repo.getDetailProfil(id)
            _detailProfil.value = res
        }
    }
}