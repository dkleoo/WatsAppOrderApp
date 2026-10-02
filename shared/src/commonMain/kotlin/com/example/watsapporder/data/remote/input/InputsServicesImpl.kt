package com.example.watsapporder.data.remote.input

import com.example.watsapporder.data.mappers.InputCreateRequest
import com.example.watsapporder.data.mappers.InputResponse
import com.example.watsapporder.data.remote.errorMessage
import com.example.watsapporder.domain.util.EndPoints
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class InputsServicesImpl(private val httpClient: HttpClient) : InputsServices {

    override suspend fun getInputs(): List<InputResponse> {
        val response = httpClient.get(EndPoints.BASE_URL + EndPoints.INPUTS)
        return response.bodyOrThrow()
    }

    override suspend fun createInput(request: InputCreateRequest): InputResponse {
        val response = httpClient.post(EndPoints.BASE_URL + EndPoints.INPUTS) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.bodyOrThrow()
    }

    private suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T {
        if (status.isSuccess()) return body()
        throw IllegalStateException(errorMessage())
    }
}
