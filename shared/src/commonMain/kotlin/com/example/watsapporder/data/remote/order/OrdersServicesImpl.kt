package com.example.watsapporder.data.remote.order

import com.example.watsapporder.data.mappers.OrderResponse
import com.example.watsapporder.data.mappers.OrderStatus
import com.example.watsapporder.data.mappers.SequenceResponse
import com.example.watsapporder.data.mappers.UpdateOrderStatusRequest
import com.example.watsapporder.data.remote.errorMessage
import com.example.watsapporder.domain.util.EndPoints
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class OrdersServicesImpl(private val httpClient: HttpClient) : OrdersServices {

    override suspend fun getOrders(token: String): List<OrderResponse> {
        val response = httpClient.get(EndPoints.BASE_URL + EndPoints.ORDERS) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        return response.bodyOrThrow()
    }

    override suspend fun getOrdersSince(token: String, sequence: Long): List<OrderResponse> {
        val response = httpClient.get(
            EndPoints.BASE_URL + EndPoints.ORDERS + "/since/$sequence",
        ) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        return response.bodyOrThrow()
    }

    override suspend fun getSequence(token: String): Long {
        val response = httpClient.get(EndPoints.BASE_URL + EndPoints.ORDERS_SEQUENCE) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        return response.bodyOrThrow<SequenceResponse>().sequence
    }

    override suspend fun updateStatus(
        token: String,
        id: Int,
        status: OrderStatus,
    ): OrderResponse {
        val response = httpClient.patch(EndPoints.BASE_URL + EndPoints.ORDERS + "/$id/status") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(UpdateOrderStatusRequest(status))
        }
        return response.bodyOrThrow()
    }

    private suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T {
        if (status.isSuccess()) return body()
        throw IllegalStateException(errorMessage())
    }
}
