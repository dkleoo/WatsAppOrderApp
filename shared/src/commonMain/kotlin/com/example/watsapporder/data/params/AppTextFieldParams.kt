package com.example.watsapporder.data.params

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType

data class AppTextFieldParams(
    val value: String,
    val onValueChange: (String) -> Unit,
    val placeholder: String,
    val leadingIcon: Painter,
    val keyboardType: KeyboardType,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val modifier: Modifier = Modifier,
)
