package com.example.repository

import com.example.model.User
import com.example.model.Users
import com.example.utils.HashUtils
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepository {
    fun registerUser(username: String, email: String, password: String): User?{
        return transaction {
            if (Users.selectAll().where { Users.email eq email }.count() > 0) {
                null
            } else {
                val userId = Users.insertAndGetId {
                    it[Users.username] = username
                    it[Users.email] = email
                    it[Users.passwordHash] = HashUtils.hashPassword(password)
                }

                getUserById(userId.value)

            }
        }
    }

    // Получение пользователя по email
    fun getUserByEmail(email: String): User? {
        return transaction {
            Users.selectAll().where { Users.email eq email }
                .map { toUser(it) } // Преобразуем строку из таблицы в объект User
                .singleOrNull() // Если пользователя нет, возвращаем null
        }
    }

    // Получение пользователя по id
    fun getUserById(id: Int): User? {
        return transaction {
            Users.selectAll().where { Users.id eq id }
                .map { toUser(it) }
                .singleOrNull()
        }
    }

    // Вспомогательная функция для преобразования строки таблицы в объект User
    private fun toUser(row: ResultRow): User {
        return User(
            id = row[Users.id].value,
            username = row[Users.username],
            email = row[Users.email],
            passwordHash = row[Users.passwordHash],
            photoPath = row[Users.photoPath]
        )
    }

}