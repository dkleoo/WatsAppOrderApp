package com.example.watsapporder.data.mappers

import kotlinx.serialization.Serializable

enum class OrderStatus {
    DRAFT,
    PENDING,
    IN_KITCHEN,
    ON_THE_WAY,
    DELIVERED,
    CANCELLED,
}

@Serializable
data class OrderItemStepInput(
    val id: Int,
    val name: String,
    val price: Double = 0.0,
)

@Serializable
data class OrderItemStep(
    val stepId: Int,
    val name: String,
    val position: Int = 0,
    val inputs: List<OrderItemStepInput> = emptyList(),
)

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
    val steps: List<OrderItemStep> = emptyList(),
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
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
    val items: List<OrderItemResponse> = emptyList(),
)

@Serializable
data class SequenceResponse(
    val sequence: Long,
)
