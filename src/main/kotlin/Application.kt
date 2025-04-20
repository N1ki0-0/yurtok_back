package com.example

import com.example.db.DatabaseFactory
import com.example.repository.UserRepository
import com.example.repository.UserRepositoryImpl
import com.example.routes.authRoutes
import com.example.routes.vacancyRoutes
import com.example.security.configureSecurity
import com.example.service.UserService
import com.example.service.UserServiceImpl
import com.example.service.VacancyService
import com.example.service.VacancyServiceImpl
import com.example.uploads.avatars.avatarRoutes
import com.example.uploads.icons.vacancyIconRoutes
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt
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
//    install(Authentication) {
//        jwt("auth-jwt") {  verifier, validate -> UserIdPrincipalForUser  }
//    }

    configureSecurity()
    avatarRoutes()
    vacancyIconRoutes()
    val service: UserService = UserServiceImpl()
    val repository: UserRepository = UserRepositoryImpl(service)
    val vacancy: VacancyService = VacancyServiceImpl()

    authRoutes(repository)
    vacancyRoutes(vacancy)

    configureRouting()
}
