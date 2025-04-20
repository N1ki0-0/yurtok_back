package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Application(
    val id: Int,
    val vacancyId: Int,
    val createdAt: String,
    val status: String,
    val message: String?
)