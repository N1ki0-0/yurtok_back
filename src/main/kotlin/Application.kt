package com.example

import com.example.db.DatabaseFactory
import com.example.repository.UserRepository
import com.example.repository.UserRepositoryImpl
import com.example.routes.authRoutes
import com.example.security.configureSecurity
import com.example.service.UserService
import com.example.service.UserServiceImpl
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    install(ContentNegotiation){
        gson()
    }
    configureSecurity()

    val service: UserService = UserServiceImpl()
    val repository: UserRepository = UserRepositoryImpl(service)

    authRoutes(repository)

    configureRouting()
}
