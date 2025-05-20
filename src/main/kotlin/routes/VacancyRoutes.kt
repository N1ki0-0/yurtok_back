package com.example.routes

import com.example.model.ApplicationRequest
import com.example.model.Vacancy
import com.example.security.UserIdPrincipalForUser
import com.example.service.VacancyService
import com.example.service.seedSpecialists
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import java.io.File
import java.util.UUID

fun Application.vacancyRoutes(service: VacancyService) {
    routing {
        post("/seed-specialists") {
            seedSpecialists(service)
            call.respond(HttpStatusCode.OK, "Специалисты успешно добавлены в базу!")
        }
        post("/vacancies") {
            val multipart = call.receiveMultipart()
            var iconUrl: String? = null
            var name = ""
            var serviceType = ""
            var serviceSubType = ""
            var address = ""
            var description = ""
            var email = ""
            var phone = ""
            var priceList = mutableListOf<Pair<String, String>>()
            var experienceYears = 0
            var rating = 0f
            var needsEmployment = false
            var freeConsultation = false
            var workDays = ""
            var tags = mutableListOf<String>()

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        when (part.name) {
                            "name" -> name = part.value
                            "serviceType" -> serviceType = part.value
                            "serviceSubType" -> serviceSubType = part.value
                            "address" -> address = part.value
                            "description" -> description = part.value
                            "email" -> email = part.value
                            "phone" -> phone = part.value
                            "priceList" -> {
                                val parsed = Json.decodeFromString<List<Pair<String, String>>>(part.value)
                                priceList.addAll(parsed)
                            }
                            "experienceYears" -> experienceYears = part.value.toInt()
                            "rating" -> rating = part.value.toFloat()
                            "needsEmployment" -> needsEmployment = part.value.toBoolean()
                            "freeConsultation" -> freeConsultation = part.value.toBoolean()
                            "workDays" -> workDays = part.value
                            "tags" -> {
                                val parsed = Json.decodeFromString<List<String>>(part.value)
                                tags.addAll(parsed)
                            }
                        }
                    }
                    is PartData.FileItem -> {
                        if (part.name == "icon") {
                            if (part.contentType !in listOf(ContentType.Image.JPEG, ContentType.Image.PNG)) {
                                throw BadRequestException("Только JPEG/PNG изображения для иконки!")
                            }
                            val fileName = "icon_${UUID.randomUUID()}.${part.contentType?.contentSubtype}"
                            val targetDir = File("uploads/icons").apply { mkdirs() }
                            File(targetDir, fileName).writeBytes(part.streamProvider().readBytes())
                            iconUrl = "/icons/$fileName"
                        }
                    }
                    else -> part.dispose()
                }
            }

            if (name.isBlank() || serviceType.isBlank() || address.isBlank()) {
                throw BadRequestException("Заполните все обязательные поля!")
            }

            val vacancy = Vacancy(
                id = 0, // Автоинкремент на стороне базы
                icon = iconUrl ,
                name = name,
                serviceType = serviceType,
                serviceSubType = serviceSubType,
                rating = rating,
                address = address,
                experienceYears = experienceYears,
                needsEmployment = needsEmployment,
                freeConsultation = freeConsultation,
                workDays = workDays,
                description = description,
                email = email,
                phone = phone,
                priceList = priceList,
                tags = tags
            )

            val createdVacancy = service.createVacancy(vacancy)
                ?: throw BadRequestException("Ошибка создания вакансии")

            call.respond(HttpStatusCode.Created, createdVacancy)
        }

        get("/vacancies") {
            try {
                val vacancies = service.getAllVacancies()
                call.respond(vacancies)
            } catch (e: Exception) {
                application.log.error("Ошибка при получении вакансий", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Не удалось загрузить вакансии")
                )
            }
        }

        // Получить вакансию по ID
        get("/vacancies/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw BadRequestException("Вакансия не найдена")
            val vacancy = service.getVacancyById(id)
                ?: throw NotFoundException("Вакансия не найдена")
            call.respond(vacancy)
        }

        get("/vacancies/search"){
            val raw = call.request.queryParameters["query"] ?: ""
            val q = raw.trim()
            if(q.isBlank()){
                call.respond(service.getAllVacancies())
            }else{
                val filtered = service.searchVacancies(q)
                call.respond(filtered)
            }
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
            post("/favorites/{vacancyId}") {

                val userId = call.principal<UserIdPrincipalForUser>()?.id ?: return@post call.respond(HttpStatusCode.Unauthorized, "Пользователь не найден")
                val vacancyId = call.parameters["vacancyId"]?.toInt() ?: throw BadRequestException("Вакансия не найдена")


                service.addToFavorites(userId, vacancyId)
                call.respond(HttpStatusCode.OK)
            }

            delete("/favorites/{vacancyId}") {
                val userId = call.principal<UserIdPrincipalForUser>()?.id ?:
                    return@delete call.respond(HttpStatusCode.Unauthorized, "Пользователь не найден")
                val vacancyId = call.parameters["vacancyId"]?.toInt() ?: throw BadRequestException("Вакансия не найдена")
                service.removeFromFavorites(userId, vacancyId)
                call.respond(HttpStatusCode.OK)
            }

            // Создать отклик
            post("/applications") {
                val userId = call.principal<UserIdPrincipalForUser>()?.id
                    ?: return@post call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Пользователь не авторизован"))

                try {
                    val request = call.receive<ApplicationRequest>()

                    // Проверка существования вакансии
                    val vacancy = service.getVacancyById(request.vacancyId)
                        ?: return@post call.respond(HttpStatusCode.NotFound, mapOf("error" to "Вакансия не найдена"))

                    // Создание отклика
                    service.applyToVacancy(userId, request.vacancyId, request.message)
                    call.respond(HttpStatusCode.Created)

                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный формат данных"))
                } catch (e: Exception) {
                    application.log.error("Ошибка при создании отклика", e)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Внутренняя ошибка сервера"))
                }
            }

            // Получить отклики пользователя
            get("/applications") {
                val userId = call.principal<UserIdPrincipalForUser>()?.id
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)
                val applications = service.getApplications(userId)
                call.respond(applications)
            }
        }
    }
}