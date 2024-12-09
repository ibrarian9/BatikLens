package com.app.batiklens.di.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHistory(history: History)

    @Query("SELECT * FROM history")
    fun getAllHistory(): LiveData<List<History>>

    @Delete
    fun deleteHistory(history: History)
}