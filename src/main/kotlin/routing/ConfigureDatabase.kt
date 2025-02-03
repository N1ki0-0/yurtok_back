package com.example.routing

import com.example.model.Users
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


fun Application.configureDatabase() {
    val dbUrl = "jdbc:postgresql://localhost:5432/postgres"
    val dbUser = "postgres"
    val dbPassword = "Nikita0110!"

    Database.connect(
        url = dbUrl,
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )

    // Создаём таблицы, если их нет
    transaction {
        SchemaUtils.create(Users)
    }
}