package com.example.watsapporder.data.params

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

data class ProviderButtonParams(
    val text: String,
    val icon: Painter,
    val onClick: () -> Unit,
    val containerColor: Color,
    val contentColor: Color,
    val iconContainerColor: Color,
    val iconTint: Color,
    val modifier: Modifier = Modifier,
    val enabled: Boolean = true,
)
