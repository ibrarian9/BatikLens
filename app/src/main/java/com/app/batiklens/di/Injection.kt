package com.app.batiklens.di

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

object Injection {
    fun messageToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getPath(context: Context, uri: Uri): String? {
        return try {
            val returnCursor = context.contentResolver.query(uri, null, null, null, null)
            returnCursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                it.moveToFirst()
                val name = it.getString(nameIndex)
                val file = File(context.cacheDir, name)

                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                file.absolutePath
            }
        } catch (e: Exception) {
            messageToast(context, "Failed to get the file path: ${e.message}")
            null
        }
    }

}