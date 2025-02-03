package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: String = "",
    val email: String,
    val password: String
)