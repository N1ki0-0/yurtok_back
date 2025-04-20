package com.example.service

import com.example.model.Application
import com.example.model.Vacancy

interface VacancyService {
    suspend fun createVacancy(vacancy: Vacancy): Vacancy?
    suspend fun getVacancyById(id: Int): Vacancy?
    suspend fun addToFavorites(userId: Int, vacancyId: Int)
    suspend fun removeFromFavorites(userId: Int, vacancyId: Int)
    suspend fun getFavorites(userId: Int): List<Int>
    suspend fun applyToVacancy(userId: Int, vacancyId: Int, message: String?)
    suspend fun getApplications(userId: Int): List<Application>
}