package com.example.service

import com.example.db.DatabaseFactory.dbQuery
import com.example.db.UserTable
import com.example.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class UserServiceImpl : UserService {
    override suspend fun registerUser(params: CreateUserParams): User? {
        var statement: InsertStatement<Number>? = null
        dbQuery{
            statement = UserTable.insert {
                it[email] = params.email
                it[password] = params.password
                it[username] = params.username
                it[avatar] = params.avatar
            }
        }
        return rowToUser(statement?.resultedValues?.get(0))
    }

    override suspend fun findUserByEmail(email: String): User? {
        val user = dbQuery {
            UserTable.selectAll().where { UserTable.email.eq(email) }
                .map { rowToUser(it) }.singleOrNull()
        }
        return user
    }

    private fun rowToUser(row: ResultRow?): User?{
        return if(row == null) null
        else User(
            id = row[UserTable.id],
            username = row[UserTable.username],
            email = row[UserTable.email],
            password = row[UserTable.password],
            avatar = row[UserTable.avatar]
        )
    }
}