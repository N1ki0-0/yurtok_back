package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationRequest(
    val vacancyId: Int,
    val message: String? = null
)