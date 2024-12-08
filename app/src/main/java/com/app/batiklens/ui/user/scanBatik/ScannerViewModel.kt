package com.app.batiklens.ui.user.scanBatik

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.batiklens.di.MainRepository
import com.app.batiklens.di.database.History
import com.app.batiklens.di.models.PredictLabel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ScannerViewModel(private val repo: MainRepository): ViewModel() {

    val loading: LiveData<Boolean> = repo.loading

    private val _modelPredict = MutableLiveData<PredictLabel?>()
    val modelPredict : LiveData<PredictLabel?> = _modelPredict

    fun insertHistory(history: History) = repo.insertHistory(history)

    fun predictModel(file: File) {
        viewModelScope.launch(Dispatchers.Main) {
            val res = repo.predictModel(file)
            _modelPredict.value = res
        }
    }
}