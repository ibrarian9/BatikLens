package com.app.batiklens.di.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "confidence")
    val confidence: Double = 0.0,

    @ColumnInfo(name = "image_uri")
    val imageUri: String,

    @ColumnInfo(name = "nama_batik")
    val namaBatik: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)