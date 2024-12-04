package com.app.batiklens.ui.user.scanBatik

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import kotlinx.coroutines.launch
import java.io.File

class ScannerViewModel(private val repo: MainRepository): ViewModel() {

    private val _modelPredict = MutableLiveData<Result<String>>()
    val modelPredict : LiveData<Result<String>> = _modelPredict

    fun predictModel(file: File) {
        viewModelScope.launch {
            val res = repo.predictModel(file)
            _modelPredict.value = res
        }
    }
}