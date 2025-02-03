package com.example.utils

import org.apache.commons.codec.digest.DigestUtils

object HashUtils {
    fun hashPassword(password: String): String{
        return DigestUtils.sha256Hex(password)
    }

    fun verifyPassword(paswword: String, hash: String): Boolean{
        return hashPassword(paswword) == hash
    }
}