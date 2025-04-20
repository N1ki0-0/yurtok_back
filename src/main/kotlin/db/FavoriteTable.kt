package com.example.db

import org.jetbrains.exposed.sql.Table

object FavoriteTable : Table("favorites") {
    val userId = integer("user_id").references(UserTable.id)
    val vacancyId = integer("vacancy_id").references(VacancyTable.id)
    override val primaryKey = PrimaryKey(userId, vacancyId)
}