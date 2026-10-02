package com.example.watsapporder.data.params

import com.example.watsapporder.data.mappers.InputResponse

data class InputCardParams(
    val input: InputResponse,
    val noPriceText: String,
    val extraPriceText: String,
    val extraPriceAmount: String?,
    val categoryText: String,
    val categoryBackground: androidx.compose.ui.graphics.Color,
    val categoryContent: androidx.compose.ui.graphics.Color,
)
