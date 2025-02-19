package com.example.db

import org.jetbrains.exposed.sql.Table

object UserTable: Table("users") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50)
    val email = varchar("email", 100)
    val password = varchar("password", 255)
    val avatar = text("avatar")
    override val primaryKey = PrimaryKey(id)
}