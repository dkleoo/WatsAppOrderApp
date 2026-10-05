package com.example.watsapporder.data.remote.order

import com.example.watsapporder.data.mappers.OrderResponse
import com.example.watsapporder.domain.util.EndPoints
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json

class OrdersSocketDataSource(
    private val httpClient: HttpClient,
    private val json: Json,
) {

    fun streamOrders(
        token: String,
        startSequence: Long,
        onSequence: (Long) -> Unit,
    ): Flow<OrderResponse> = flow {
        var lastSequence = startSequence
        while (currentCoroutineContext().isActive) {
            try {
                ordersSince(token, lastSequence)
                    .sortedBy { it.sequence }
                    .forEach { order ->
                        lastSequence = maxOf(lastSequence, order.sequence)
                        onSequence(lastSequence)
                        emit(order)
                    }
                val session = httpClient.webSocketSession(urlString = socketUrl(token))
                try {
                    for (frame in session.incoming) {
                        if (frame !is Frame.Text) continue
                        val order = runCatching {
                            json.decodeFromString<OrderResponse>(frame.readText())
                        }.getOrNull() ?: continue
                        if (order.sequence > lastSequence) {
                            lastSequence = order.sequence
                            onSequence(lastSequence)
                            emit(order)
                        }
                    }
                } finally {
                    session.close()
                }
            } catch (exception: Exception) {
                delay(RECONNECT_DELAY_MS)
            }
        }
    }

    private fun socketUrl(token: String): String {
        val base = EndPoints.BASE_URL
            .replace("https://", "wss://")
            .replace("http://", "ws://")
        return base + EndPoints.ORDERS_WS + "?token=$token"
    }

    private suspend fun ordersSince(token: String, since: Long): List<OrderResponse> {
        val response = httpClient.get(EndPoints.BASE_URL + EndPoints.ORDERS + "/since/$since") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        if (!response.status.isSuccess()) return emptyList()
        return runCatching { response.body<List<OrderResponse>>() }.getOrDefault(emptyList())
    }

    private companion object {
        const val RECONNECT_DELAY_MS = 3000L
    }
}
