package com.example.watsapporder.data.remote

import com.example.watsapporder.data.mappers.ErrorResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

internal suspend fun HttpResponse.errorMessage(): String =
    runCatching { body<ErrorResponse>().error }.getOrDefault(status.value.toString())
