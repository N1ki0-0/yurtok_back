package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Vacancy(
    val id: Int,
    val icon: String,
    val name: String,
    val serviceType: String,
    val serviceSubType: String,
    val rating: Float,
    val address: String,
    val experienceYears: Int,
    val needsEmployment: Boolean,
    val freeConsultation: Boolean,
    val workDays: String,
    val description: String,
    val email: String,
    val phone: String,
    val priceList: List<Pair<String, String>>,
    val tags: List<String> = emptyList()
)