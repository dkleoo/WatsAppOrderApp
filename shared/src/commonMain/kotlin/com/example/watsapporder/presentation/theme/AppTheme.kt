package com.example.watsapporder.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = ColorApp.primary,
            onPrimary = ColorApp.onPrimary,
            background = ColorApp.background,
            surface = ColorApp.white,
            error = ColorApp.errorColor,
        ),
        typography = appTypography(),
        content = content,
    )
}
