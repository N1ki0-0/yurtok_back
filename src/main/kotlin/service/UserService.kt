package com.example.service

import com.example.model.User

// Интерфейс сервиса для работы с пользователями
interface UserService {
    suspend fun registerUser(params: CreateUserParams): User? // Регистрация пользователя
    suspend fun findUserByEmail(email: String): User? // Поиск пользователя по email
    suspend fun updateUserAvatar(userId: Int, avatar: String?)
    suspend fun findUserById(id: Int): User?
}