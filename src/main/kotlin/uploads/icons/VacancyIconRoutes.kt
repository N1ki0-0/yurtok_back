package com.example.uploads.icons

import com.example.db.DatabaseFactory.dbQuery
import com.example.db.VacancyTable
import com.example.security.UserIdPrincipalForUser
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.content.*
import io.ktor.http.*
import io.ktor.server.plugins.BadRequestException
import java.io.File
import java.util.UUID
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll

//На доработке!!!
fun Application.vacancyIconRoutes() {
    routing {
        authenticate {
            // Роут для загрузки/обновления иконки вакансии
            post("/vacancies/{id}/icon") {
                // Получаем ID вакансии из пути
                val vacancyId = call.parameters["id"]?.toIntOrNull()
                    ?: throw BadRequestException("Неверный ID вакансии")

                // Получаем принципал пользователя, чтобы проверить права (опционально)
                val userId = call.principal<UserIdPrincipalForUser>()?.id
                    ?: return@post call.respond(HttpStatusCode.Unauthorized, "Пользователь не найден")

                // Здесь можно проверить, что пользователь имеет право менять эту вакансию
                // Например: if (!service.isOwner(userId, vacancyId)) throw ForbiddenException("Нет доступа")

                // Принимаем multipart-данные
                val multipart = call.receiveMultipart()
                var savedPath: String? = null // Путь к сохранённому файлу

                multipart.forEachPart { part: PartData ->
                    if (part is PartData.FileItem && part.name == "icon") {
                        val ext = File(part.originalFileName ?: "").extension
                            .takeIf { it.isNotBlank() } ?: "png"
                        val fileName = "${UUID.randomUUID()}.$ext"
                        val folder = File("uploads/icons").apply { mkdirs() }
                        val file = File(folder, fileName)

                        // Сохраняем новый файл на диск
                        part.streamProvider().use { input ->
                            file.outputStream().buffered().use { output ->
                                input.copyTo(output)
                            }
                        }

                        savedPath = "/uploads/icons/$fileName"
                    }
                    part.dispose()
                }


                // Если файл не передан — ошибка
                if (savedPath == null) {
                    throw BadRequestException("Файл не получен")
                }

                // При желании удаляем старую иконку вакансии из диска
                dbQuery {
                    val old = VacancyTable.selectAll().where { VacancyTable.id eq vacancyId }
                        .singleOrNull()?.get(VacancyTable.icon)
                    old?.let {
                        File("." + it).takeIf { file -> file.exists() }?.delete()
                    }
                }

                // Обновляем запись в БД, записываем новый URL иконки
                dbQuery {
                    VacancyTable.update({ VacancyTable.id eq vacancyId }) {
                        it[icon] = savedPath
                    }
                }

                // Отправляем клиенту новый URL иконки вакансии
                call.respond(HttpStatusCode.OK, mapOf("icon" to savedPath))
            }
        }
    }
}