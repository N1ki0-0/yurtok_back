package com.example.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val passwordHash: String,
    val photoPath: String? = null // Фото необязательно
)
