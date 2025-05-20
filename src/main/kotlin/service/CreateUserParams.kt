package com.example.service

import kotlinx.serialization.Serializable

// DTO (Data Transfer Object) для создания пользователя
@Serializable // Позволяет сериализовать класс в JSON и обратно
data class CreateUserParams(
    val username: String, // Имя пользователя
    val email: String, // Email
    val password: String, // Пароль
    val avatar: String? // Ссылка на аватар
)

// DTO для входа пользователя
@Serializable
data class LoginUserParams(
    val email: String, // Email
    val password: String // Пароль
)

