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
    val storeId: Int? = null,
    val backendId: Int = 0,
)

sealed class PhoneCodeResult {
    data class CodeSent(val verificationId: String) : PhoneCodeResult()

    data class AutoVerified(val user: LoggedUser) : PhoneCodeResult()
}
