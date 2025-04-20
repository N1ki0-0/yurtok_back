package com.example.db

import org.jetbrains.exposed.sql.Table

object VacancyTable : Table("vacancies") {
    val id = integer("id").autoIncrement()
    val icon = varchar("icon", 255)
    val name = varchar("name", 100)
    val serviceType = varchar("service_type", 50)
    val serviceSubType = varchar("service_sub_type", 50)
    val rating = float("rating")
    val address = varchar("address", 255)
    val experienceYears = integer("experience_years")
    val needsEmployment = bool("needs_employment")
    val freeConsultation = bool("free_consultation")
    val workDays = varchar("work_days", 50)
    val description = text("description")
    val email = varchar("email", 100)
    val phone = varchar("phone", 20)
    val priceList = text("price_list") // Сериализовать как JSON
    val tags = text("tags") // Сериализовать как JSON
    override val primaryKey = PrimaryKey(id)
}