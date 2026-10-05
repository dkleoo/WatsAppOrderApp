package com.example.watsapporder.data.repositoyImpl.login

import com.example.watsapporder.data.local.SessionStore
import com.example.watsapporder.data.mappers.AuthProvider
import com.example.watsapporder.data.mappers.LoggedUser
import com.example.watsapporder.data.mappers.LoginRequest
import com.example.watsapporder.data.mappers.PhoneCodeResult
import com.example.watsapporder.data.mappers.RegisterRequest
import com.example.watsapporder.data.mappers.toLoggedUser
import com.example.watsapporder.data.remote.login.AuthServices
import com.example.watsapporder.data.remote.login.FirebaseAuthGateway
import com.example.watsapporder.domain.repository.login.AuthRepository
import com.example.watsapporder.platform.DeviceTokenProvider

class AuthRepositoryImpl(
    private val firebaseGateway: FirebaseAuthGateway,
    private val authServices: AuthServices,
    private val sessionStore: SessionStore,
    private val deviceTokenProvider: DeviceTokenProvider,
) : AuthRepository {

    override suspend fun restoreSession(): LoggedUser? = sessionStore.get()

    override suspend fun signInWithGoogle(): LoginResults =
        runAuth { linkBackend(firebaseGateway.signInWithGoogle()) }

    override suspend fun signInWithEmail(email: String, password: String): LoginResults = runAuth {
        authServices.login(
            LoginRequest(email = email, password = password, deviceToken = deviceToken()),
        ).toLoggedUser(AuthProvider.EMAIL).also(sessionStore::save)
    }

    override suspend fun registerWithEmail(email: String, password: String, name: String): LoginResults =
        runAuth {
            authServices.register(
                RegisterRequest(
                    email = email,
                    password = password,
                    name = name,
                    deviceToken = deviceToken(),
                ),
            ).toLoggedUser(AuthProvider.EMAIL).also(sessionStore::save)
        }

    override suspend fun sendPhoneCode(phoneNumber: String): PhoneResults = try {
        when (val result = firebaseGateway.sendPhoneCode(phoneNumber)) {
            is PhoneCodeResult.CodeSent -> PhoneResults.CodeSent(result.verificationId)
            is PhoneCodeResult.AutoVerified -> PhoneResults.Success(linkBackend(result.user))
        }
    } catch (exception: Exception) {
        PhoneResults.MessageError(exception.message.orEmpty())
    }

    override suspend fun confirmPhoneCode(verificationId: String, code: String): LoginResults =
        runAuth { linkBackend(firebaseGateway.confirmPhoneCode(verificationId, code)) }

    override fun signOut() {
        firebaseGateway.signOut()
        sessionStore.clear()
    }

    private suspend fun linkBackend(firebaseUser: LoggedUser): LoggedUser {
        val email = firebaseUser.email
        if (email.isBlank()) return firebaseUser

        sessionStore.get()?.takeIf { it.email == email }?.let { return it }

        val password = federatedPassword(email)

        return try {
            authServices.register(
                RegisterRequest(
                    email = email,
                    password = password,
                    name = firebaseUser.name,
                    deviceToken = deviceToken(),
                ),
            ).toLoggedUser(firebaseUser.provider).also(sessionStore::save)
        } catch (registerException: Exception) {
            try {
                authServices.login(
                    LoginRequest(email = email, password = password, deviceToken = deviceToken()),
                ).toLoggedUser(firebaseUser.provider)
                    .also(sessionStore::save)
            } catch (loginException: Exception) {
                throw IllegalStateException(
                    loginException.message ?: registerException.message ?: "No se pudo iniciar sesión",
                )
            }
        }
    }

    override suspend fun refreshSession() {
        val current = sessionStore.get() ?: return
        runCatching {
            val storeId = authServices.profile(current.token).storeId ?: current.storeId
            sessionStore.save(current.copy(storeId = storeId))
        }
    }

    private suspend fun runAuth(block: suspend () -> LoggedUser): LoginResults = try {
        LoginResults.Success(block())
    } catch (exception: Exception) {
        LoginResults.MessageError(exception.message.orEmpty())
    }

    private suspend fun deviceToken(): String = runCatching {
        deviceTokenProvider.getToken()
    }.getOrDefault("")
}

private fun federatedPassword(email: String): String {
    var hash = 5381
    email.lowercase().forEach { char ->
        hash = (hash * 33) xor char.code
    }
    return "federated_${email.lowercase()}_${hash.toUInt().toString(16)}"
}
