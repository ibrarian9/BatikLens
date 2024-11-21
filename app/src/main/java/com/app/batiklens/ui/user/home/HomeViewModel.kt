package com.app.batiklens.ui.user.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import com.app.batiklens.di.models.ArtikelModelItem
import com.app.batiklens.di.models.MotifModelItem
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: MainRepository): ViewModel() {

    val loading: LiveData<Boolean> = repo.loading
    val error: LiveData<String> = repo.error

    private val _semuaBerita = MutableLiveData<List<ArtikelModelItem>>()
    val semuaBerita: LiveData<List<ArtikelModelItem>> = _semuaBerita

    private val _semuaMotif = MutableLiveData<List<MotifModelItem>>()
    val semuaMotif: LiveData<List<MotifModelItem>> = _semuaMotif

    init {
        getAllArticle()
        getAllMotif()
    }

    private fun getAllArticle() {
        viewModelScope.launch {
            val res = repo.getAllArticle()
            _semuaBerita.value = res
        }
    }

    private fun getAllMotif() {
        viewModelScope.launch {
            val res = repo.getAllMotifHome()
            _semuaMotif.value = res
        }
    }
}