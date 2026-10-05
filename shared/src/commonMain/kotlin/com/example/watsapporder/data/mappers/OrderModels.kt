package com.example.watsapporder.data.mappers

import kotlinx.serialization.Serializable

enum class OrderStatus {
    PENDING,
    KITCHEN,
    ON_ROUTE,
    DELIVERED,
}

@Serializable
data class OrderItemResponse(
    val id: Int,
    val productId: Int? = null,
    val productName: String,
    val unitPrice: Double,
    val stepName: String? = null,
    val selectedInputIds: List<Int> = emptyList(),
    val quantity: Int,
    val subtotal: Double,
)

@Serializable
data class OrderResponse(
    val id: Int,
    val sequence: Long,
    val storeId: Int? = null,
    val customerPhone: String,
    val customerName: String? = null,
    val deliveryAddress: String? = null,
    val paymentType: String? = null,
    val total: Double? = null,
    val status: OrderStatus,
    val items: List<OrderItemResponse> = emptyList(),
)

@Serializable
data class SequenceResponse(
    val sequence: Long,
)
