package com.app.batiklens.di.models.dto

data class RegisterDTO(
    val email: String,
    val name: String,
    val password: String,
    val confirmPassword: String
)
