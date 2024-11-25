package com.app.batiklens.ui.user.motif

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import com.app.batiklens.di.models.ListBatikItem
import kotlinx.coroutines.launch

class MotifListViewModel(private val repo: MainRepository): ViewModel() {

    val loading = repo.loading
    val image = repo.image
    val provinsi = repo.provinsi

    private val _listMotif = MutableLiveData<List<ListBatikItem>>()
    val listMotif: LiveData<List<ListBatikItem>> = _listMotif

    fun listMotif(id: Int){
        viewModelScope.launch {
            val res = repo.getAllListMotif(id)
            _listMotif.value = res
        }
    }
}