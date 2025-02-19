package com.example.security

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

val SECRET_KEY = "656464957327"
val ALGORITHM = "HmacSHA1"

val HASH_KAY = hex(SECRET_KEY)
val HMAC_KAY = SecretKeySpec(HASH_KAY,ALGORITHM)

fun hash(password: String): String{
    val hmac = Mac.getInstance(ALGORITHM)
    hmac.init(HMAC_KAY)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}