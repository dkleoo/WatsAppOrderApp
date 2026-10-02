package com.example.watsapporder.data.params

import androidx.compose.ui.Modifier

data class PasswordTextFieldParams(
    val value: String,
    val onValueChange: (String) -> Unit,
    val placeholder: String,
    val isPasswordVisible: Boolean,
    val onToggleVisibility: () -> Unit,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val modifier: Modifier = Modifier,
)
