package com.app.batiklens.ui.user.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import com.app.batiklens.di.models.ArtikelModelItem
import com.app.batiklens.di.models.FashionModelsItem
import com.app.batiklens.di.models.MotifModelItem
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: MainRepository): ViewModel() {

    val loading: LiveData<Boolean> = repo.loading

    private val _semuaBerita = MutableLiveData<List<ArtikelModelItem>>()
    val semuaBerita: LiveData<List<ArtikelModelItem>> = _semuaBerita

    private val _semuaMotif = MutableLiveData<List<MotifModelItem>>()
    val semuaMotif: LiveData<List<MotifModelItem>> = _semuaMotif

    private val _semuaFashion = MutableLiveData<List<FashionModelsItem>>()
    val semuaFashion: LiveData<List<FashionModelsItem>> = _semuaFashion

    init {
        getAllArticle()
        getAllMotif()
        getAllFashion()
    }

    private fun getAllFashion() {
        viewModelScope.launch {
            val res = repo.semuaFashion()
            _semuaFashion.value = res
        }
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

    fun semuaArtikel(search: String) {
        viewModelScope.launch {
            val res = repo.searchArtikel(search)
            _semuaBerita.value = res
        }
    }
}