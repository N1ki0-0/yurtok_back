package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.*

object JwtConfig {
    // Потом нужно исправить
    private const val secret = "KJHJHKHjkhsad1234w23f43fj"
    private const val issuer = "http://0.0.0.0:8080/"
    private const val audience = "YURTOK"
    const val realm = "Access to 'ktor-example'"
    //
    private val algorithm = Algorithm.HMAC256(secret)

    fun generateToken(userId: Int, username: String): String{
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .withClaim("username", username)
            .sign(algorithm)
    }

    fun configure(config: JWTAuthenticationProvider.Config){
        config.realm = realm
        config.verifier(
            JWT.require(algorithm)
                .withIssuer(issuer)
                .withAudience(audience)
                .build()
        )
        config.validate { credentail ->
            if(credentail.payload.getClaim("userId").isNull) null
            else JWTPrincipal(credentail.payload)
        }
    }

}