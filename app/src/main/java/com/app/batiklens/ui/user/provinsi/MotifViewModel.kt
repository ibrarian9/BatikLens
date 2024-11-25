package com.app.batiklens.ui.user.provinsi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import com.app.batiklens.di.models.ProvinsiMotifModelItem
import kotlinx.coroutines.launch

class MotifViewModel(private val repo: MainRepository): ViewModel() {

    val loading = repo.loading

    private val _semuaProvinsi = MutableLiveData<List<ProvinsiMotifModelItem>>()
    val semuaProvinsi: LiveData<List<ProvinsiMotifModelItem>> = _semuaProvinsi

    init {
        getProvinsi()
    }

    private fun getProvinsi(){
        viewModelScope.launch {
            val result = repo.getAllProvinsi()
            _semuaProvinsi.value = result
        }
    }

}