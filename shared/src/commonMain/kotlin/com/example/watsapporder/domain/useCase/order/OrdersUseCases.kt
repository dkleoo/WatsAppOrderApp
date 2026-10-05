package com.example.watsapporder.domain.useCase.order

import com.example.watsapporder.data.mappers.OrderResponse
import com.example.watsapporder.data.mappers.OrderStatus
import com.example.watsapporder.data.repositoyImpl.order.OrderResults
import com.example.watsapporder.domain.repository.order.OrdersRepository
import kotlinx.coroutines.flow.Flow

class OrdersUseCases(private val ordersRepository: OrdersRepository) {
    suspend fun getOrders(): OrderResults = ordersRepository.getOrders()

    suspend fun getSequence(): Long = ordersRepository.getSequence()

    suspend fun updateStatus(id: Int, status: OrderStatus): OrderResults =
        ordersRepository.updateStatus(id, status)

    fun streamOrders(startSequence: Long, onSequence: (Long) -> Unit): Flow<OrderResponse> =
        ordersRepository.streamOrders(startSequence, onSequence)

    fun lastSequence(): Long = ordersRepository.lastSequence()

    fun saveLastSequence(sequence: Long) = ordersRepository.saveLastSequence(sequence)
}
