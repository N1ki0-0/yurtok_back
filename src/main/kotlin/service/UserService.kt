package com.example.service

import com.example.model.User

// Интерфейс сервиса для работы с пользователями
interface UserService {
    suspend fun registerUser(params: CreateUserParams): User? // Регистрация пользователя
    suspend fun findUserByEmail(email: String): User? // Поиск пользователя по email
}