package com.app.batiklens.ui.user.detailMotif

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import com.app.batiklens.di.models.DetailMotifHome
import com.app.batiklens.di.models.ListBatikItem
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DetailViewModel(private val repo: MainRepository): ViewModel() {

    private val _detailMotif = MutableLiveData<ListBatikItem?>()
    val detailMotif: LiveData<ListBatikItem?> = _detailMotif

    private val _detailMotifHome = MutableLiveData<DetailMotifHome?>()
    val detailMotifHome: LiveData<DetailMotifHome?> = _detailMotifHome

    fun detailMotifHome(id: Int){
        viewModelScope.launch {
            val res = repo.detailHomeBatik(id)
            _detailMotifHome.value = res
        }
    }

    fun detailMotif(id: Int, motifId: Int){
        viewModelScope.launch {
            val res = repo.getDetailMotif(id, motifId)
            _detailMotif.value = res
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}