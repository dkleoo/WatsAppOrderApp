package com.example.watsapporder.domain.useCase.login

import com.example.watsapporder.data.mappers.LoggedUser
import com.example.watsapporder.data.repositoyImpl.login.LoginResults
import com.example.watsapporder.data.repositoyImpl.login.PhoneResults
import com.example.watsapporder.domain.repository.login.AuthRepository

class AuthUseCases(private val authRepository: AuthRepository) {
    suspend fun restoreSession(): LoggedUser? = authRepository.restoreSession()

    suspend fun signInWithGoogle(): LoginResults = authRepository.signInWithGoogle()

    suspend fun signInWithEmail(email: String, password: String): LoginResults =
        authRepository.signInWithEmail(email, password)

    suspend fun registerWithEmail(email: String, password: String, name: String): LoginResults =
        authRepository.registerWithEmail(email, password, name)

    suspend fun sendPhoneCode(phoneNumber: String): PhoneResults =
        authRepository.sendPhoneCode(phoneNumber)

    suspend fun confirmPhoneCode(verificationId: String, code: String): LoginResults =
        authRepository.confirmPhoneCode(verificationId, code)

    suspend fun refreshSession() = authRepository.refreshSession()

    fun signOut() = authRepository.signOut()
}
