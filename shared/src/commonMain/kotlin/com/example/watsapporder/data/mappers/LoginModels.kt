package com.example.watsapporder.data.mappers

enum class AuthProvider {
    GOOGLE,
    EMAIL,
    PHONE,
}

data class LoggedUser(
    val id: String,
    val name: String,
    val email: String,
    val token: String,
    val provider: AuthProvider,
    val photoUrl: String? = null,
)

sealed class PhoneCodeResult {
    data class CodeSent(val verificationId: String) : PhoneCodeResult()

    data class AutoVerified(val user: LoggedUser) : PhoneCodeResult()
}
