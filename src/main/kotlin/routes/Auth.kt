package com.example.routes

import com.example.repository.UserRepository
import com.example.service.CreateUserParams
import com.example.service.LoginUserParams
import com.example.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.util.UUID

// Настройка маршрутов аутентификации
fun Application.authRoutes(
    repository: UserRepository) {
    routing {
        route("/auth") { // Группа маршрутов для аутентификации
            post("/register") { // Обработка POST-запроса на регистрацию
                val params = call.receive<CreateUserParams>() // Получение параметров из тела запроса
                val result = repository.registerUser(params) // Вызов репозитория
                call.respond(result.statusCode, result) // Отправка ответа со статусом
            }
        }
        post("/login") { // Обработка POST-запроса на вход
            val params = call.receive<LoginUserParams>() // Получение email и пароля
            val result = repository.loginUser(params) // Проверка учетных данных
            call.respond(result.statusCode, result) // Отправка ответа
        }


    }
}