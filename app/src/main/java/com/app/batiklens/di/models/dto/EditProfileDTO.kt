package com.app.batiklens.di.models.dto

import java.io.File

data class EditProfileDTO(
    val uid: String,
    val name: String,
    val email: String,
    val photo: File
)