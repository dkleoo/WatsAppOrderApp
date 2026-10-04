package com.example.watsapporder.data.remote.store

import com.example.watsapporder.data.mappers.StoreRequest
import com.example.watsapporder.data.mappers.StoreResponse
import com.example.watsapporder.data.remote.errorMessage
import com.example.watsapporder.domain.util.EndPoints
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class StoreServicesImpl(private val httpClient: HttpClient) : StoreServices {

    override suspend fun getStore(token: String): StoreResponse? {
        val response = httpClient.get(EndPoints.BASE_URL + EndPoints.STORES_ME) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        if (response.status.value == 404 || response.status.value == 400) return null
        if (!response.status.isSuccess()) {
            throw IllegalStateException(response.errorMessage())
        }
        return runCatching { response.body<StoreResponse>() }.getOrNull()
    }

    override suspend fun updateStore(token: String, id: Int, request: StoreRequest): StoreResponse {
        val response = httpClient.put(EndPoints.BASE_URL + EndPoints.STORES + "/$id") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (response.status.isSuccess()) {
            return response.body()
        }
        throw IllegalStateException(response.errorMessage())
    }
}
