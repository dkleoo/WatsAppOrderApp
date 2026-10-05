package com.example.watsapporder.domain.repository.order

import com.example.watsapporder.data.mappers.OrderResponse
import com.example.watsapporder.data.mappers.OrderStatus
import com.example.watsapporder.data.repositoyImpl.order.OrderResults
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {
    suspend fun getOrders(): OrderResults
    suspend fun getOrderDetail(id: Int): OrderResults
    suspend fun getSequence(): Long
    suspend fun updateStatus(id: Int, status: OrderStatus): OrderResults
    fun streamOrders(startSequence: Long, onSequence: (Long) -> Unit): Flow<OrderResponse>
    fun lastSequence(): Long
    fun saveLastSequence(sequence: Long)
}
