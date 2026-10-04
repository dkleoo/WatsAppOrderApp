package com.example.watsapporder.domain.repository.login

import com.example.watsapporder.data.mappers.LoggedUser
import com.example.watsapporder.data.repositoyImpl.login.LoginResults
import com.example.watsapporder.data.repositoyImpl.login.PhoneResults

interface AuthRepository {
    suspend fun restoreSession(): LoggedUser?
    suspend fun signInWithGoogle(): LoginResults
    suspend fun signInWithEmail(email: String, password: String): LoginResults
    suspend fun registerWithEmail(email: String, password: String, name: String): LoginResults
    suspend fun sendPhoneCode(phoneNumber: String): PhoneResults
    suspend fun confirmPhoneCode(verificationId: String, code: String): LoginResults
    suspend fun refreshSession()
    fun signOut()
}
