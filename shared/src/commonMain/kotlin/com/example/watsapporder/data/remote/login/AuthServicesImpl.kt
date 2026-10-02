package com.example.watsapporder.data.remote.login

import com.example.watsapporder.data.mappers.AuthResponse
import com.example.watsapporder.data.mappers.ErrorResponse
import com.example.watsapporder.data.mappers.LoginRequest
import com.example.watsapporder.data.mappers.RegisterRequest
import com.example.watsapporder.data.mappers.UserResponse
import com.example.watsapporder.domain.util.EndPoints
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class AuthServicesImpl(private val httpClient: HttpClient) : AuthServices {

    override suspend fun register(request: RegisterRequest): AuthResponse {
        val response = httpClient.post(EndPoints.BASE_URL + EndPoints.REGISTER) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.authBody()
    }

    override suspend fun login(request: LoginRequest): AuthResponse {
        val response = httpClient.post(EndPoints.BASE_URL + EndPoints.LOGIN) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.authBody()
    }

    override suspend fun profile(token: String): UserResponse {
        val response = httpClient.get(EndPoints.BASE_URL + EndPoints.ME) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        if (response.status.isSuccess()) {
            return response.body()
        }
        throw IllegalStateException(response.errorMessage())
    }

    private suspend fun HttpResponse.authBody(): AuthResponse {
        if (status.isSuccess()) {
            return body()
        }
        throw IllegalStateException(errorMessage())
    }

    private suspend fun HttpResponse.errorMessage(): String =
        runCatching { body<ErrorResponse>().error }.getOrDefault(status.value.toString())
}
