package com.example.watsapporder.platform

import com.google.firebase.messaging.FirebaseMessaging
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

private class AndroidDeviceTokenProvider : DeviceTokenProvider {
    override suspend fun getToken(): String = suspendCancellableCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!continuation.isActive) return@addOnCompleteListener
                continuation.resume(if (task.isSuccessful) task.result.orEmpty() else "")
            }
    }
}

actual val deviceTokenProvider: DeviceTokenProvider = AndroidDeviceTokenProvider()
