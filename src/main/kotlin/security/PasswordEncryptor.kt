package com.example.security

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

val SECRET_KEY = "656464957327" // Секретный ключ для хеширования
val ALGORITHM = "HmacSHA1" // Алгоритм хеширования

// Преобразование секретного ключа в байты
val HASH_KAY = hex(SECRET_KEY)
// Создание ключа для HMAC
val HMAC_KAY = SecretKeySpec(HASH_KAY, ALGORITHM)

// Функция для хеширования пароля
fun hash(password: String): String {
    val hmac = Mac.getInstance(ALGORITHM) // Инициализация HMAC
    hmac.init(HMAC_KAY) // Инициализация с ключом
    // Хеширование пароля и преобразование в hex-строку
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}

