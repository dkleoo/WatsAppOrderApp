package com.example.watsapporder.data.remote.login

import com.example.watsapporder.data.mappers.AuthResponse
import com.example.watsapporder.data.mappers.LoginRequest
import com.example.watsapporder.data.mappers.RegisterRequest
import com.example.watsapporder.data.mappers.UserResponse

interface AuthServices {
    suspend fun register(request: RegisterRequest): AuthResponse
    suspend fun login(request: LoginRequest): AuthResponse
    suspend fun profile(token: String): UserResponse
}
