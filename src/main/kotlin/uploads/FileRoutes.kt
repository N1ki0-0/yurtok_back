package com.example.uploads

import com.example.service.UserService
import com.example.service.VacancyService
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.io.File
import java.util.UUID


fun Application.fileRoutes(userService: UserService, vacancyService: VacancyService) {
    routing{
        authenticate {
            // Загрузка аватара пользователя
            post("/users/{id}/avatar") {
                val userId = call.parameters["id"]?.toInt()
                    ?: throw BadRequestException("Неверный ID пользователя")
                val multipart = call.receiveMultipart()
                var fileUrl: String? = null

                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        // Сохранение в папку uploads/avatars
                        val fileName = "avatar_${UUID.randomUUID()}.${part.contentType?.contentSubtype}"
                        val targetDir = "uploads/avatars"
                        File(targetDir).mkdirs()
                        File("$targetDir/$fileName").writeBytes(part.streamProvider().readBytes())
                        fileUrl = "/avatars/$fileName"
                    }
                    part.dispose()
                }

                userService.updateUserAvatar(userId, fileUrl)
                call.respond(HttpStatusCode.OK, mapOf("avatarUrl" to fileUrl))
            }

            // Загрузка иконки вакансии
            post("/vacancies/{id}/icon") {
                val vacancyId = call.parameters["id"]?.toInt()
                    ?: throw BadRequestException("Неверный ID вакансии")
                val multipart = call.receiveMultipart()
                var fileUrl: String? = null

                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        // Сохранение в папку uploads/icons
                        val fileName = "icon_${UUID.randomUUID()}.${part.contentType?.contentSubtype}"
                        val targetDir = "uploads/icons"
                        File(targetDir).mkdirs()
                        File("$targetDir/$fileName").writeBytes(part.streamProvider().readBytes())
                        fileUrl = "/icons/$fileName"
                    }
                    part.dispose()
                }

                vacancyService.updateVacancyIcon(vacancyId, fileUrl)
                call.respond(HttpStatusCode.OK, mapOf("iconUrl" to fileUrl))
            }
        }
    }
}