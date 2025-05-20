package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Application(
    val id: Int,
    val userId: Int,
    val vacancyId: Int,
    val status: String,
    val createdAt: String,
    val message: String?
)