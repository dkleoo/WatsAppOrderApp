package com.example.watsapporder.data.remote.login

import com.example.watsapporder.data.mappers.LoggedUser
import com.example.watsapporder.data.mappers.PhoneCodeResult

interface FirebaseAuthGateway {
    suspend fun signInWithGoogle(): LoggedUser
    suspend fun sendPhoneCode(phoneNumber: String): PhoneCodeResult
    suspend fun confirmPhoneCode(verificationId: String, code: String): LoggedUser
    fun signOut()
}
