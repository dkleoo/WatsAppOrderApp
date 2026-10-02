package com.example.watsapporder.data.mappers

import kotlinx.serialization.Serializable

enum class ProductType {
    WITH_INPUTS,
    CREATED,
}

@Serializable
data class ProductRequest(
    val id: Int? = null,
    val name: String,
    val price: Double,
    val cost: Double,
    val quantity: Int,
    val type: ProductType,
    val steps: List<StepRequest> = emptyList(),
)

@Serializable
data class StepRequest(
    val name: String,
    val position: Int,
    val inputIds: List<Int> = emptyList(),
)

@Serializable
data class ProductResponse(
    val id: Int,
    val name: String,
    val price: Double,
    val cost: Double,
    val quantity: Int,
    val type: ProductType,
    val steps: List<StepResponse> = emptyList(),
)

@Serializable
data class StepResponse(
    val id: Int,
    val productId: Int,
    val name: String,
    val position: Int,
    val inputs: List<InputResponse> = emptyList(),
)

fun ProductResponse.toRequest(name: String = this.name, price: Double = this.price): ProductRequest =
    ProductRequest(
        id = id,
        name = name,
        price = price,
        cost = cost,
        quantity = quantity,
        type = type,
        steps = if (type == ProductType.WITH_INPUTS) steps.map { it.toRequest() } else emptyList(),
    )

fun StepResponse.toRequest(): StepRequest = StepRequest(
    name = name,
    position = position,
    inputIds = inputs.map { it.id },
)
