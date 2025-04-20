package com.example.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object ApplicationTable : Table("applications") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(UserTable.id)
    val vacancyId = integer("vacancy_id").references(VacancyTable.id)
    val status = varchar("status", 20) // "pending", "accepted", "rejected"
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val message = text("message").nullable()
    override val primaryKey = PrimaryKey(id)
}