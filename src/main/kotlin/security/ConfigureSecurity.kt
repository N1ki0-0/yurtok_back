package com.example.security

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity(){
    JwtConfig.initialize("e9d9578c-5b22-4acb-b412-18d8a8af24c9!MySuperSecret123")
    install(Authentication){
        jwt {
            verifier(JwtConfig.instance.verifier)
            validate {
                val claim = it.payload.getClaim(JwtConfig.CLAIM).asInt()
                if(claim != null ){
                    UserIdPrincipalForUser(claim)
                }else null
            }
        }
    }
}