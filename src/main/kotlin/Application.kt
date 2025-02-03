package com.example


import com.example.routing.configureDatabase
import com.example.routing.configureSecurity
import com.example.routing.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    //DataBaseFactory.init()
    configureDatabase()

    // Настройка сериализации (JSON)
    configureSerialization()

    // Настройка аутентификации (JWT)
    configureSecurity()



    // Настройка маршрутов
    configureRouting()
}
