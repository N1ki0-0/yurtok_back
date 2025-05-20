package com.example.routes

import com.example.db.UserTable.avatar
import com.example.repository.UserRepository
import com.example.security.UserIdPrincipalForUser
import com.example.service.CreateUserParams
import com.example.service.LoginUserParams
import com.example.utils.BaseResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
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
        post("/register") {
            val multipart = call.receiveMultipart()
            var username = ""
            var email = ""
            var password = ""
            var avatarUrl: String? = null

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        when (part.name) {
                            "username" -> username = part.value
                            "email" -> email = part.value
                            "password" -> password = part.value
                        }
                    }
                    is PartData.FileItem -> {
                        // Проверяем, что это поле для аватара
                        if (part.name == "avatar") {
                            // Проверяем тип файла
                            if (part.contentType !in listOf(ContentType.Image.JPEG, ContentType.Image.PNG)) {
                                throw BadRequestException("Только JPEG/PNG изображения!")
                            }

                            // Сохраняем файл
                            val fileName = "avatar_${UUID.randomUUID()}.${part.contentType?.contentSubtype}"
                            val targetDir = File("uploads/avatars").apply { mkdirs() }
                            File(targetDir, fileName).writeBytes(part.streamProvider().readBytes())
                            avatarUrl = "/avatars/$fileName"
                        }
                    }
                    else -> part.dispose() // Игнорируем другие части
                }
            }

            // 🛠 Если не было аватара — ставим дефолтный путь
            if (username.isBlank() || email.isBlank() || password.isBlank()) {
                throw BadRequestException("Заполните все обязательные поля!")
            }

            val finalAvatarUrl = avatarUrl ?: "/avatars/default.png"

            val params = CreateUserParams(
                username = username,
                email = email,
                password = password,
                avatar = finalAvatarUrl
            )

            val result = repository.registerUser(params)
            call.respond(result.statusCode, result)
        }
        post("/login") { // Обработка POST-запроса на вход
            val params = call.receive<LoginUserParams>() // Получение email и пароля
            val result = repository.loginUser(params) // Проверка учетных данных
            call.respond(result.statusCode, result) // Отправка ответа
        }
        authenticate {
            get("/user/me") {
                val userId = call.principal<UserIdPrincipalForUser>()?.id ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    "Пользователь не найден"
                )

                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid token")
                    return@get
                }

                val user = repository.findUserById(userId)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                } else {
                    call.respond(BaseResponse.SuccessResponse(user))
                }
            }
        }
    }
}