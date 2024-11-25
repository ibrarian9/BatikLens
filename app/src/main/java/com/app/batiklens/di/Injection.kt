package com.app.batiklens.di

import android.content.Context
import android.widget.Toast

object Injection {
    fun messageToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}