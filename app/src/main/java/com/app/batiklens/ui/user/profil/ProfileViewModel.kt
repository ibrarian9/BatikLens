package com.app.batiklens.ui.user.profil

import androidx.lifecycle.ViewModel
import com.app.batiklens.di.MainRepository

class ProfileViewModel(private val repo: MainRepository): ViewModel() {

    fun logout() = repo.logout()
}