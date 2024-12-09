package com.app.batiklens.ui.user.editProfile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import com.app.batiklens.di.models.UserModel
import com.app.batiklens.di.models.dto.EditProfileDTO
import kotlinx.coroutines.launch
import java.io.File

class EditProfileViewModel(private val repo: MainRepository): ViewModel() {

    private val _detailProfil = MutableLiveData<UserModel?>()
    val detailProfil: LiveData<UserModel?> = _detailProfil

    private val _editProfil = MutableLiveData<Result<String>>()
    val editProfile: LiveData<Result<String>> = _editProfil

    private val _filePoto = MutableLiveData<File?>()
    val filePoto: LiveData<File?> = _filePoto

    fun dataPhoto(context: Context, fileUrl: String, fileName: String) {
        viewModelScope.launch {
            val res = repo.urlToFile(context, fileUrl, fileName)
            _filePoto.value = res
        }
    }

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