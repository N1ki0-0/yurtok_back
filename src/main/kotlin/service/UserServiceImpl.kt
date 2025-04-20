package com.example.service

import com.example.db.DatabaseFactory.dbQuery
import com.example.db.UserTable
import com.example.model.User
import com.example.security.hash
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

// Реализация интерфейса UserService
class UserServiceImpl : UserService {
    override suspend fun registerUser(params: CreateUserParams): User? {
        var statement: InsertStatement<Number>? = null // Результат INSERT-запроса
        dbQuery { // Асинхронный запрос к БД
            statement = UserTable.insert { // Вставка новой записи в таблицу
                it[email] = params.email // Заполнение email
                it[password] = hash(params.password) // Хеширование пароля
                it[username] = params.username // Заполнение имени
                it[avatar] = params.avatar // Заполнение аватара
            }
        }
        // Преобразование результата в объект User
        return rowToUser(statement?.resultedValues?.get(0))
    }

    override suspend fun findUserByEmail(email: String): User? {
        val user = dbQuery { // Асинхронный запрос к БД
            UserTable.selectAll() // Выбор всех записей
                .where { UserTable.email.eq(email) } // Фильтр по email
                .map { rowToUser(it) } // Преобразование строки в User
                .singleOrNull() // Возврат null, если пользователь не найден
        }
        return user
    }


    // Преобразование строки ResultRow в объект User
    private fun rowToUser(row: ResultRow?): User? {
        return if (row == null) null
        else User(
            id = row[UserTable.id], // ID пользователя
            username = row[UserTable.username], // Имя
            email = row[UserTable.email], // Email
            password = row[UserTable.password], // Пароль (хеш)
            avatar = row[UserTable.avatar] // Аватар
        )
    }
}