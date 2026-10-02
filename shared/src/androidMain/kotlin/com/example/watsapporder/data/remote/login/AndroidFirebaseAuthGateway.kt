package com.example.watsapporder.data.remote.login

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.watsapporder.data.mappers.AuthProvider
import com.example.watsapporder.data.mappers.LoggedUser
import com.example.watsapporder.data.mappers.PhoneCodeResult
import com.example.watsapporder.platform.ActivityHolder
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await

class AndroidFirebaseAuthGateway : FirebaseAuthGateway {

    private val auth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    private val activity: Activity
        get() = ActivityHolder.current
            ?: throw IllegalStateException("No hay Activity disponible")

    override suspend fun signInWithGoogle(): LoggedUser {
        val credentialManager = CredentialManager.create(activity)
        val webClientId = activity.resources.getIdentifier(
            "default_web_client_id",
            "string",
            activity.packageName,
        ).let { resourceId ->
            if (resourceId == 0) null else activity.getString(resourceId)
        } ?: throw IllegalStateException("Falta default_web_client_id en google-services.json")

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val response = credentialManager.getCredential(activity, request)
        val credential = response.credential
        if (credential !is CustomCredential ||
            credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            throw IllegalStateException("Credencial de Google no disponible")
        }

        val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        val user = auth.signInWithCredential(firebaseCredential).await().user
            ?: throw IllegalStateException("No se pudo iniciar sesion con Google")
        return user.toLoggedUser(AuthProvider.GOOGLE)
    }

    override suspend fun sendPhoneCode(phoneNumber: String): PhoneCodeResult =
        suspendCancellableCoroutine { continuation ->
            val currentActivity = ActivityHolder.current
            if (currentActivity == null) {
                continuation.resumeWithException(IllegalStateException("No hay Activity disponible"))
                return@suspendCancellableCoroutine
            }

            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    if (!continuation.isActive) return
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        val user = task.result?.user
                        if (task.isSuccessful && user != null) {
                            continuation.resume(
                                PhoneCodeResult.AutoVerified(user.toLoggedUser(AuthProvider.PHONE)),
                            )
                        } else {
                            continuation.resumeWithException(
                                task.exception ?: IllegalStateException("Error de verificacion"),
                            )
                        }
                    }
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    if (continuation.isActive) {
                        continuation.resumeWithException(exception)
                    }
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken,
                ) {
                    if (continuation.isActive) {
                        continuation.resume(PhoneCodeResult.CodeSent(verificationId))
                    }
                }
            }

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(currentActivity)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

    override suspend fun confirmPhoneCode(verificationId: String, code: String): LoggedUser {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        val user = auth.signInWithCredential(credential).await().user
            ?: throw IllegalStateException("El codigo es invalido")
        return user.toLoggedUser(AuthProvider.PHONE)
    }

    override fun signOut() {
        auth.signOut()
    }
}

private fun FirebaseUser.toLoggedUser(provider: AuthProvider): LoggedUser = LoggedUser(
    id = uid,
    name = displayName ?: email?.substringBefore("@") ?: phoneNumber ?: "Usuario",
    email = email.orEmpty(),
    token = "",
    provider = provider,
)
