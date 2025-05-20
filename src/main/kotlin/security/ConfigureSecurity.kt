package com.example.security

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity() {
    // Инициализация JWT с секретным ключом
    JwtConfig.initialize("e9d9578c-5b22-4acb-b412-18d8a8af24c9!MySuperSecret123")
    install(Authentication) { // Установка аутентификации
        jwt() { // Настройка JWT
            verifier(JwtConfig.instance.verifier) // Верификатор токена
            validate { // Проверка токена
                val claim = it.payload.getClaim(JwtConfig.CLAIM).asInt() // Получение ID из токена
                if (claim != null) {
                    UserIdPrincipalForUser(claim) // Создание Principal (если токен валиден)
                } else null
            }
        }
    }
}