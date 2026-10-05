package com.example.watsapporder.data.repositoyImpl.order

import com.example.watsapporder.data.local.SessionStore
import com.example.watsapporder.data.mappers.OrderResponse
import com.example.watsapporder.data.mappers.OrderStatus
import com.example.watsapporder.data.remote.order.OrdersServices
import com.example.watsapporder.data.remote.order.OrdersSocketDataSource
import com.example.watsapporder.domain.repository.order.OrdersRepository
import kotlinx.coroutines.flow.Flow

class OrdersRepositoryImpl(
    private val services: OrdersServices,
    private val socket: OrdersSocketDataSource,
    private val sessionStore: SessionStore,
) : OrdersRepository {

    override suspend fun getOrders(): OrderResults = try {
        OrderResults.Orders(services.getOrders(token()))
    } catch (exception: Exception) {
        OrderResults.MessageError(exception.message.orEmpty())
    }

    override suspend fun getSequence(): Long = runCatching {
        services.getSequence(token())
    }.getOrDefault(0L)

    override suspend fun updateStatus(id: Int, status: OrderStatus): OrderResults = try {
        OrderResults.Order(services.updateStatus(token(), id, status))
    } catch (exception: Exception) {
        OrderResults.MessageError(exception.message.orEmpty())
    }

    override fun streamOrders(startSequence: Long, onSequence: (Long) -> Unit): Flow<OrderResponse> =
        socket.streamOrders(token(), startSequence, onSequence)

    override fun lastSequence(): Long = sessionStore.getLastOrderSequence()

    override fun saveLastSequence(sequence: Long) {
        sessionStore.saveLastOrderSequence(sequence)
    }

    private fun token(): String = sessionStore.get()?.token.orEmpty()
}
