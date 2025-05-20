package com.example.repository

import com.example.model.User
import com.example.service.CreateUserParams
import com.example.service.LoginUserParams
import com.example.utils.BaseResponse

interface UserRepository {
    suspend fun registerUser(params: CreateUserParams): BaseResponse<Any>
    suspend fun loginUser(params: LoginUserParams): BaseResponse<Any>
    suspend fun findUserById(id: Int): User?
}