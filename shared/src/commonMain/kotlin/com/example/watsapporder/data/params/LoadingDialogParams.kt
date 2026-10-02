package com.example.watsapporder.data.params

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

data class LoadingDialogParams(
    val visible: Boolean,
    val title: String,
    val subtitle: String,
    val badgeText: String,
    val icon: Painter,
    val iconContainerColor: Color,
    val iconTint: Color,
)
