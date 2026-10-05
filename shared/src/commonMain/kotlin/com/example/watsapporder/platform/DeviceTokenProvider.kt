package com.example.watsapporder.platform

interface DeviceTokenProvider {
    suspend fun getToken(): String
}

expect val deviceTokenProvider: DeviceTokenProvider
