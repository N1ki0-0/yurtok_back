package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val avatar: String,
    val authToken: String? = null
)