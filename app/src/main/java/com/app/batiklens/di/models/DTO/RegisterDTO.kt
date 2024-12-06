package com.app.batiklens.di.models.DTO

data class RegisterDTO(
    val email: String,
    val name: String,
    val password: String,
    val confirmPassword: String
)
