package com.example.watsapporder.data.mappers

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val deviceToken: String = "",
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
    val deviceToken: String = "",
)

@Serializable
data class AuthResponse(
    val token: String,
    val tokenType: String,
    val expiresIn: Long,
    val user: UserResponse,
)

@Serializable
data class UserResponse(
    val id: Int,
    val email: String,
    val name: String,
    val storeId: Int? = null,
)

@Serializable
data class ErrorResponse(
    val error: String,
)

fun AuthResponse.toLoggedUser(provider: AuthProvider): LoggedUser = LoggedUser(
    id = user.id.toString(),
    name = user.name,
    email = user.email,
    token = token,
    provider = provider,
    storeId = user.storeId,
    backendId = user.id,
)

fun UserResponse.toLoggedUser(provider: AuthProvider, token: String = ""): LoggedUser = LoggedUser(
    id = id.toString(),
    name = name,
    email = email,
    token = token,
    provider = provider,
    storeId = storeId,
    backendId = id,
)
