package com.example.security

import io.ktor.server.auth.Principal


@Suppress("DEPRECATION")
data class UserIdPrincipalForUser(
    val id: Int
) : Principal
