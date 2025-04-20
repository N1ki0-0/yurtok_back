package com.example.service

import com.example.db.ApplicationTable
import com.example.db.DatabaseFactory.dbQuery
import com.example.db.FavoriteTable
import com.example.db.VacancyTable
import com.example.model.Application
import com.example.model.Vacancy
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and


class VacancyServiceImpl: VacancyService {
    override suspend fun createVacancy(vacancy: Vacancy): Vacancy? {
        var insertedId: Int? = null
        dbQuery {
            // Вставка и получение ID через resultedValues
            val result = VacancyTable.insert {
                it[icon] = vacancy.icon
                it[name] = vacancy.name
                it[serviceType] = vacancy.serviceType
                it[serviceSubType] = vacancy.serviceSubType
                it[rating] = vacancy.rating
                it[address] = vacancy.address
                it[experienceYears] = vacancy.experienceYears
                it[needsEmployment] = vacancy.needsEmployment
                it[freeConsultation] = vacancy.freeConsultation
                it[workDays] = vacancy.workDays
                it[description] = vacancy.description
                it[email] = vacancy.email
                it[phone] = vacancy.phone
                it[priceList] = Json.encodeToString(vacancy.priceList)
                it[tags] = Json.encodeToString(vacancy.tags)
            }
            // Извлечение ID из результата
            insertedId = result.resultedValues?.get(0)?.get(VacancyTable.id)
        }
        return insertedId?.let { getVacancyById(it) }
    }

    override suspend fun getVacancyById(id: Int): Vacancy? = dbQuery {
        VacancyTable.selectAll().where { VacancyTable.id eq id }
            .map { row ->
                Vacancy(
                    id = row[VacancyTable.id],
                    icon = row[VacancyTable.icon],
                    name = row[VacancyTable.name],
                    serviceType = row[VacancyTable.serviceType],
                    serviceSubType = row[VacancyTable.serviceSubType],
                    rating = row[VacancyTable.rating],
                    address = row[VacancyTable.address],
                    experienceYears = row[VacancyTable.experienceYears],
                    needsEmployment = row[VacancyTable.needsEmployment],
                    freeConsultation = row[VacancyTable.freeConsultation],
                    workDays = row[VacancyTable.workDays],
                    description = row[VacancyTable.description],
                    email = row[VacancyTable.email],
                    phone = row[VacancyTable.phone],
                    priceList = Json.decodeFromString(row[VacancyTable.priceList]),
                    tags = Json.decodeFromString(row[VacancyTable.tags])
                )
            }.singleOrNull()
    }

    override suspend fun addToFavorites(userId: Int, vacancyId: Int) {
        dbQuery {
            FavoriteTable.insert {
                it[FavoriteTable.userId] = userId
                it[FavoriteTable.vacancyId] = vacancyId
            }
        }
    }

    override suspend fun removeFromFavorites(userId: Int, vacancyId: Int) {
        dbQuery {
            FavoriteTable.deleteWhere {
                (FavoriteTable.userId eq userId) and (FavoriteTable.vacancyId eq vacancyId)
            }
        }
    }

    override suspend fun getFavorites(userId: Int): List<Int> = dbQuery {
        FavoriteTable
            .selectAll().where { FavoriteTable.userId eq userId }
            .map { it[FavoriteTable.vacancyId] }
    }
/*
* Нужно доработать, пока не работает
*Авто установка состояния статуса пока не работает
*Удаление после 7 дней как была добавлена (для тестов конечно меньше)
* */
    override suspend fun applyToVacancy(userId: Int, vacancyId: Int, message: String?) {
        dbQuery {
            ApplicationTable.insert {
                it[ApplicationTable.userId] = userId
                it[ApplicationTable.vacancyId] = vacancyId
                it[ApplicationTable.message] = message
            }
        }
    }

    // Получить отклики пользователя
    override suspend fun getApplications(userId: Int): List<Application> = dbQuery {
        ApplicationTable
            .selectAll().where { ApplicationTable.userId eq userId }
            .map { row ->
                Application(
                    id = row[ApplicationTable.id],
                    vacancyId = row[ApplicationTable.vacancyId],
                    createdAt = row[ApplicationTable.createdAt].toString(),
                    status = row[ApplicationTable.status],
                    message = row[ApplicationTable.message]
                )
            }
    }
}