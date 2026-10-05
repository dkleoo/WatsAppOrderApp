package com.example.watsapporder.data.remote.order

import com.example.watsapporder.data.mappers.OrderResponse
import com.example.watsapporder.data.mappers.OrderStatus

interface OrdersServices {
    suspend fun getOrders(token: String): List<OrderResponse>
    suspend fun getOrdersSince(token: String, sequence: Long): List<OrderResponse>
    suspend fun getSequence(token: String): Long
    suspend fun updateStatus(token: String, id: Int, status: OrderStatus): OrderResponse
}
