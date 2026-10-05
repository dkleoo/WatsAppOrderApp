package com.example.watsapporder.data.mappers

import kotlinx.serialization.Serializable

@Serializable
data class UpdateOrderStatusRequest(
    val status: OrderStatus,
)
