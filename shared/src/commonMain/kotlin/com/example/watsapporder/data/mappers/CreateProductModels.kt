package com.example.watsapporder.data.mappers

import kotlinx.serialization.Serializable

@Serializable
data class CreateStepForm(
    val name: String = "",
    val selectedInputIds: List<Int> = emptyList(),
)

data class CreateProductForm(
    val type: ProductType? = null,
    val name: String = "",
    val price: String = "",
    val steps: List<CreateStepForm> = emptyList(),
)

fun CreateProductForm.toRequest(): ProductRequest = ProductRequest(
    name = name.trim(),
    price = price.replace(",", ".").toDoubleOrNull() ?: 0.0,
    cost = 0.0,
    quantity = 0,
    type = type ?: ProductType.CREATED,
    steps = if (type == ProductType.WITH_INPUTS) {
        steps.mapIndexed { index, step ->
            StepRequest(
                name = step.name.trim(),
                position = index + 1,
                inputIds = step.selectedInputIds,
            )
        }
    } else {
        emptyList()
    },
)
