package com.example.routes

import com.example.model.Vacancy
import com.example.security.UserIdPrincipalForUser
import com.example.service.VacancyService
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.io.File
import java.util.UUID

fun Application.vacancyRoutes(service: VacancyService) {
    routing {
        post("/vacancies") {
            val vacancy = call.receive<Vacancy>()
            val createdVacancy = service.createVacancy(vacancy)
                ?: throw BadRequestException("Ошибка создания вакансии")
            call.respond(HttpStatusCode.Created, createdVacancy)
        }

        // Получить вакансию по ID
        get("/vacancies/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw BadRequestException("Вакансия не найдена")
            val vacancy = service.getVacancyById(id)
                ?: throw NotFoundException("Вакансия не найдена")
            call.respond(vacancy)
        }


        authenticate {
            // Получить избранные ID вакансий
            get("/favorites") {
                val userId = call.principal<UserIdPrincipalForUser>()?.id ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    "Пользователь не найден"
                )
                val favorites = service.getFavorites(userId)
                call.respond(mapOf("favorites" to favorites))
            }

            // Добавить в избранное
            post("/favorites") {
                val userId = call.principal<UserIdPrincipalForUser>()?.id
                    ?: return@post call.respond(HttpStatusCode.Unauthorized, "Пользователь не найден")
                val params = call.receive<Map<String, Int>>()
                val vacancyId = params["vacancyId"] ?: throw BadRequestException("Вакансия не найдена")
                service.addToFavorites(userId, vacancyId)
                call.respond(HttpStatusCode.Created)
            }

            delete("/favorites/{vacancyId}") {
                val userId = call.principal<UserIdPrincipalForUser>()?.id ?:
                    return@delete call.respond(HttpStatusCode.Unauthorized, "Пользователь не найден")
                val vacancyId = call.parameters["vacancyId"]?.toInt() ?: throw BadRequestException("Вакансия не найдена")
                service.removeFromFavorites(userId, vacancyId)
                call.respond(HttpStatusCode.OK)
            }

            // Получить отклики пользователя
            get("/applications") {
                val userId = call.principal<UserIdPrincipalForUser>()?.id ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    "Пользователь не найден"
                )
                val applications = service.getApplications(userId)
                call.respond(applications)
            }

            // Создать отклик
            post("/applications") {
                val userId = call.principal<UserIdPrincipalForUser>()?.id
                    ?: return@post call.respond(HttpStatusCode.Unauthorized, "Пользователь не найден")
                val request = try {
                    call.receive<com.example.model.Application>()
                } catch (e: ContentTransformationException) {
                    return@post call.respond(HttpStatusCode.BadRequest, "Неверный формат данных")
                }
                try {
                    service.applyToVacancy(userId, request.vacancyId, request.message)
                    call.respond(HttpStatusCode.Created)
                } catch (e: NotFoundException) {
                    call.respond(HttpStatusCode.NotFound, "Вакансия не найдена")
                }
            }
        }
    }
}