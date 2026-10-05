package com.example.watsapporder.platform

private object UnsupportedDeviceTokenProvider : DeviceTokenProvider {
    override suspend fun getToken(): String = ""
}

actual val deviceTokenProvider: DeviceTokenProvider = UnsupportedDeviceTokenProvider
