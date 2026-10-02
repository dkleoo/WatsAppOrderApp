package com.example.watsapporder.data.remote.login

import com.example.watsapporder.data.mappers.LoggedUser
import com.example.watsapporder.data.mappers.PhoneCodeResult

class UnsupportedFirebaseAuthGateway : FirebaseAuthGateway {
    override suspend fun signInWithGoogle(): LoggedUser = unsupported()

    override suspend fun sendPhoneCode(phoneNumber: String): PhoneCodeResult = unsupported()

    override suspend fun confirmPhoneCode(verificationId: String, code: String): LoggedUser = unsupported()

    override fun signOut() = Unit

    private fun unsupported(): Nothing =
        throw UnsupportedOperationException("Firebase Auth no esta soportado en esta plataforma")
}
