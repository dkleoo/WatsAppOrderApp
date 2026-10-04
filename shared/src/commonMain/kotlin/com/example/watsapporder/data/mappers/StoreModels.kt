package com.example.watsapporder.data.mappers

import kotlinx.serialization.Serializable

@Serializable
data class StoreRequest(
    val welcomeMessage: String,
    val address: String,
    val phone: String,
    val whatsappBusinessPhone: String,
    val idWhatsApp: String,
)

@Serializable
data class StoreResponse(
    val id: Int,
    val welcomeMessage: String,
    val address: String,
    val phone: String,
    val whatsappBusinessPhone: String,
    val idWhatsApp: String,
)
