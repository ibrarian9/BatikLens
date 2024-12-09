package com.app.batiklens.di.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [History::class], version = 2, exportSchema = false)
abstract class HistoryDatabase: RoomDatabase() {

    abstract fun getHistoryDao(): HistoryDao
}