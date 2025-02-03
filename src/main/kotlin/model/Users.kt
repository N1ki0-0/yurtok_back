package com.example.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object Users : IntIdTable(){
    val username = varchar("username", 100)
    val email = varchar("email", 70).uniqueIndex()
    val passwordHash = varchar("password", 70)
    val photoPath = varchar("photo_path", 255).nullable()
}