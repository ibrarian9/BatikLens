package com.app.batiklens.ui.user.editProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import com.app.batiklens.di.models.DTO.EditProfileDTO
import com.app.batiklens.di.models.UserModel
import kotlinx.coroutines.launch

class EditProfileViewModel(private val repo: MainRepository): ViewModel() {

    private val _detailProfil = MutableLiveData<UserModel?>()
    val detailProfil: LiveData<UserModel?> = _detailProfil

    private val _editProfil = MutableLiveData<Result<String>>()
    val editProfile: LiveData<Result<String>> = _editProfil

    fun editDataProfile(editProfileDTO: EditProfileDTO) {
        viewModelScope.launch {
            val res = repo.editProfile(editProfileDTO)
            _editProfil.value = res
        }
    }

    fun getDetailProfile(id: String) {
        viewModelScope.launch {
            val res = repo.getDetailProfil(id)
            _detailProfil.value = res
        }
    }
}