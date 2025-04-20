package com.example.uploads.avatars

import com.example.db.DatabaseFactory.dbQuery
import com.example.db.UserTable
import com.example.security.UserIdPrincipalForUser
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.content.*
import io.ktor.http.*
import io.ktor.server.http.content.files
import io.ktor.server.http.content.static
import io.ktor.server.plugins.BadRequestException
import java.io.File
import java.util.UUID
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll


//На доработке!!!
fun Application.avatarRoutes() {
    routing {
        // Роут для отдачи статических файлов, таких как загруженные аватары
        static("/uploads") {
            files("uploads")
        }

        authenticate {
            // Роут для загрузки/обновления аватара пользователя
            post("/users/{id}/avatar") {
                // Получаем ID пользователя из JWT токена
                val userId = call.principal<UserIdPrincipalForUser>()?.id
                    ?: return@post call.respond(HttpStatusCode.Unauthorized, "Пользователь не найден")

                // Получаем ID пользователя из URL и сравниваем с ID из токена
                val pathId = call.parameters["id"]?.toIntOrNull()
                    ?: throw BadRequestException("Неверный ID пользователя")
                if (pathId != userId) throw return@post call.respond(HttpStatusCode.Unauthorized, "Нельзя менять чужой аватар")

                // Получаем multipart-данные из запроса
                val multipart = call.receiveMultipart()
                var savedPath: String? = null // Здесь будет путь к новому файлу

                // Обработка каждой части multipart-данных
                multipart.forEachPart { part: PartData ->
                    if (part is PartData.FileItem && part.name == "avatar") {
                        val ext = File(part.originalFileName ?: "").extension
                            .takeIf { it.isNotBlank() } ?: "png"
                        val fileName = "${UUID.randomUUID()}.$ext"
                        val folder = File("uploads/avatar").apply { mkdirs() }
                        val file = File(folder, fileName)

                        // Сохраняем новый файл на диск
                        part.streamProvider().use { input ->
                            file.outputStream().buffered().use { output ->
                                input.copyTo(output)
                            }
                        }

                        savedPath = "/uploads/avatar/$fileName"
                    }
                    part.dispose()
                }

                // Если файл не был загружен — ошибка
                if (savedPath == null) {
                    throw BadRequestException("Файл не получен")
                }

                // Удаляем старый аватар, если он был
                dbQuery {
                    val old = UserTable.selectAll().where { UserTable.id eq userId }
                        .singleOrNull()?.get(UserTable.avatar)
                    old?.let {
                        File("." + it).takeIf { file -> file.exists() }?.delete()
                    }
                }

                // Обновляем путь к новому аватару в базе данных
                dbQuery {
                    UserTable.update({ UserTable.id eq userId }) {
                        it[avatar] = savedPath
                    }
                }

                // Отправляем клиенту путь к новому аватару
                call.respond(HttpStatusCode.OK, mapOf("avatar" to savedPath))
            }
        }
    }
}
