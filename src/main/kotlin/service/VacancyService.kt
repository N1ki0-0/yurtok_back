package com.example.service

import com.example.model.Application
import com.example.model.Vacancy

interface VacancyService {
    //Вакансия
    suspend fun createVacancy(vacancy: Vacancy): Vacancy?
    suspend fun getVacancyById(id: Int): Vacancy?
    suspend fun getAllVacancies(): List<Vacancy>
    suspend fun updateVacancyIcon(vacancyId: Int, icon: String?)
    suspend fun searchVacancies(query: String): List<Vacancy>
    //Избранное
    suspend fun addToFavorites(userId: Int, vacancyId: Int)
    suspend fun removeFromFavorites(userId: Int, vacancyId: Int)
    suspend fun getFavorites(userId: Int): List<Int>
    //Отклик
    suspend fun applyToVacancy(userId: Int, vacancyId: Int, message: String?)
    suspend fun getApplications(userId: Int): List<Application>
    suspend fun updateApplicationStatuses()
}