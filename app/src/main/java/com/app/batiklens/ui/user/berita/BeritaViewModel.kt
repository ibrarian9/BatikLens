package com.app.batiklens.ui.user.berita

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import com.app.batiklens.di.models.ArtikelModelItem
import com.app.batiklens.di.models.FashionModelsItem
import kotlinx.coroutines.launch

class BeritaViewModel(private val repo: MainRepository): ViewModel() {

    private val _beritaData = MutableLiveData<ArtikelModelItem?>()
    val beritaData: LiveData<ArtikelModelItem?> = _beritaData

    private val _fashionItem = MutableLiveData<FashionModelsItem?>()
    val fashionItem: LiveData<FashionModelsItem?> = _fashionItem

    fun berita(id: Int){
        viewModelScope.launch {
            val result = repo.getDetailArtikel(id)
            _beritaData.value = result
        }
    }

    fun fashion(id: Int) {
        viewModelScope.launch {
            val result = repo.detailFashion(id)
            _fashionItem.value = result
        }
    }
}