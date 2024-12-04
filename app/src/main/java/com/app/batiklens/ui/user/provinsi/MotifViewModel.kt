package com.app.batiklens.ui.user.provinsi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import com.app.batiklens.di.models.ProvinsiMotifModelItem
import com.app.batiklens.di.models.ResponseMotifItem
import kotlinx.coroutines.launch

class MotifViewModel(private val repo: MainRepository): ViewModel() {

    val loading = repo.loading

    private val _semuaProvinsi = MutableLiveData<List<ProvinsiMotifModelItem>>()
    val semuaProvinsi: LiveData<List<ProvinsiMotifModelItem>> = _semuaProvinsi

    private val _cariMotif = MutableLiveData<List<ResponseMotifItem>>()
    val cariMotif: LiveData<List<ResponseMotifItem>> = _cariMotif

    init {
        getProvinsi()
    }

    fun cariMotif(query: String) {
        viewModelScope.launch {
            val result = repo.cariMotif(query)
            _cariMotif.value = result
        }
    }

    private fun getProvinsi(){
        viewModelScope.launch {
            val result = repo.getAllProvinsi()
            _semuaProvinsi.value = result
        }
    }

}