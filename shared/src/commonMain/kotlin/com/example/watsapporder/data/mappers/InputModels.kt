package com.example.watsapporder.data.mappers

import kotlinx.serialization.Serializable

@Serializable
data class InputCreateRequest(
    val name: String,
    val price: Double,
    val cost: Double,
    val quantity: Int,
)

@Serializable
data class InputResponse(
    val id: Int,
    val name: String,
    val price: Double,
    val cost: Double,
    val quantity: Int,
)
