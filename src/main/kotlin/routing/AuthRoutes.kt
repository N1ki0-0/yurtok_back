package com.example.routing

import com.example.model.AuthRequest
import com.example.repository.UserRepository
import com.example.utils.HashUtils
import com.example.utils.JwtConfig
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(){

    post("/register"){
        val request = call.receive<AuthRequest>()
        if (request.username.isBlank() || request.email.isBlank() || request.password.isBlank()){
            call.respond(HttpStatusCode.BadRequest, "Все поля обязательны для заполнения")
            return@post
        }

        val newUser = UserRepository.registerUser(
            username = request.username,
            email = request.email,
            password = request.password
        )

        if (newUser != null){
            call.respond(HttpStatusCode.Created,  "Пользователь зарегистрирован")
        } else {
            call.respond(HttpStatusCode.Created,"Пользователь с таким email уже существует")
        }

    }

    post("/login"){
        val request = call.receive<AuthRequest>()
        if(request.email.isBlank() || request.password.isBlank()){
            call.respond(HttpStatusCode.BadRequest, "Все поля обязательны для заполнения")
            return@post
        }

        val user = UserRepository.getUserByEmail(request.email)
        if (user == null || !HashUtils.verifyPassword(request.password, user.passwordHash)) {
            call.respond(HttpStatusCode.Unauthorized, "Неверный email или пароль")
            return@post
        }

        val token = JwtConfig.generateToken(user.id, user.username)
        call.respond(HttpStatusCode.OK, mapOf("token" to token))
    }

}