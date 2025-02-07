package com.example.service

import com.example.model.User

interface UserService {
    suspend fun registerUser(params: CreateUserParams): User?

    suspend fun findUserByEmail(email: String): User?
}