package com.example.repository

import com.example.security.JwtConfig
import com.example.security.hash
import com.example.service.CreateUserParams
import com.example.service.LoginUserParams
import com.example.service.UserService
import com.example.utils.BaseResponse
import io.ktor.http.HttpStatusCode

// Реализация репозитория для аутентификации
class UserRepositoryImpl(
    private val userService: UserService // Зависимость от сервиса
) : UserRepository {
    override suspend fun registerUser(params: CreateUserParams): BaseResponse<Any> {
        return if (isEmailExist(params.email)) { // Проверка уникальности email
            BaseResponse.ErrorResponse(message = "Электронная почта уже существует")
        } else {
            val user = userService.registerUser(params) // Создание пользователя
            if (user != null) {
                val token = JwtConfig.instance.createAccessToken(user.id) // Генерация JWT
                user.authToken = token // Присвоение токена
                BaseResponse.SuccessResponse(data = user) // Успешный ответ
            } else {
                BaseResponse.ErrorResponse() // Ошибка сервера
            }
        }
    }

    override suspend fun loginUser(params: LoginUserParams): BaseResponse<Any> {
        val user = userService.findUserByEmail(params.email) // Поиск пользователя
        return if (user == null) {
            BaseResponse.ErrorResponse(message = "Неверные учетные данные") // Пользователь не найден
        } else {
            val hashedPassword = hash(params.password) // Хеширование введенного пароля
            if (hashedPassword == user.password) { // Сравнение с хешем из БД
                val token = JwtConfig.instance.createAccessToken(user.id) // Генерация JWT
                user.authToken = token
                BaseResponse.SuccessResponse(data = user) // Успешный вход
            } else {
                BaseResponse.ErrorResponse(message = "Неверные учетные данные") // Неверный пароль
            }
        }
    }

    // Проверка существования email в БД
    private suspend fun isEmailExist(email: String): Boolean {
        return userService.findUserByEmail(email) != null
    }
}