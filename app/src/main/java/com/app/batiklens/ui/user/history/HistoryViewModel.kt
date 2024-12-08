package com.app.batiklens.ui.user.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.batiklens.di.MainRepository
import com.app.batiklens.di.database.History

class HistoryViewModel(private val repo: MainRepository): ViewModel() {
    fun getAllHistory(): LiveData<List<History>> = repo.getAllHistory()
}