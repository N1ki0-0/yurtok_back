@file:Suppress("DEPRECATION")

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
import com.example.uploads.fileRoutes
import io.ktor.server.resources.Resources
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.files
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    install(ContentNegotiation){
        gson()
    }

    CoroutineScope(Dispatchers.Default).launch {
        while (true) {
            val vacancyService: VacancyService = VacancyServiceImpl()
            vacancyService.updateApplicationStatuses()
            delay(30_000) // Проверка каждую минуту
        }
    }

    routing {
        static("avatars") {
            files("uploads/avatars") // Отдает файлы по пути /avatars/*
        }
        static("icons") {
            files("uploads/icons") // Отдает файлы по пути /icons/*
        }
    }
    configureSecurity()

    //imageRoutes()

    val service: UserService = UserServiceImpl()
    val repository: UserRepository = UserRepositoryImpl(service)
    val vacancy: VacancyService = VacancyServiceImpl()
    fileRoutes(service, vacancy)
    authRoutes(repository)
    vacancyRoutes(vacancy)

    configureRouting()
}
