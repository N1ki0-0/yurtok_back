package com.example.service

data class CreateUserParams (
    val username: String,
    val email: String,
    val password: String,
    val avatar: String
)