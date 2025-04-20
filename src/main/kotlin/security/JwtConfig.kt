package com.example.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

// Класс для настройки JWT
class JwtConfig private constructor(secret: String) {
    private val algorithm = Algorithm.HMAC256(secret) // Алгоритм HMAC256 с секретом

    // Верификатор токена (проверяет подпись, issuer и audience)
    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    // Создание JWT-токена с ID пользователя
    fun createAccessToken(id: Int): String = JWT
        .create()
        .withIssuer(ISSUER) // Указание издателя
        .withAudience(AUDIENCE) // Указание аудитории
        .withClaim(CLAIM, id) // Добавление claim (ID пользователя)
        .sign(algorithm) // Подпись токена

    companion object {
        private const val ISSUER = "YURTOK" // Издатель токена
        private const val AUDIENCE = "YURTOK" // Аудитория
        const val CLAIM = "id" // Название claim для хранения ID

        // Экземпляр JwtConfig (инициализируется через initialize)
        lateinit var instance: JwtConfig
            private set

        // Инициализация JwtConfig с секретным ключом (вызывается один раз)
        fun initialize(secret: String) {
            synchronized(this) {
                if (!this::instance.isInitialized) {
                    instance = JwtConfig(secret)
                }
            }
        }
    }
}