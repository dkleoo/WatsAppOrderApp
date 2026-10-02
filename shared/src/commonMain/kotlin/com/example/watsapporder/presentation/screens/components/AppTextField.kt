package com.example.watsapporder.presentation.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.watsapporder.data.params.AppTextFieldParams
import com.example.watsapporder.presentation.theme.ColorApp
import com.example.watsapporder.presentation.theme.plazaOnTextStyle

@Composable
fun AppTextField(params: AppTextFieldParams) {
    OutlinedTextField(
        value = params.value,
        onValueChange = params.onValueChange,
        modifier = params.modifier.fillMaxWidth(),
        singleLine = true,
        isError = params.isError,
        placeholder = {
            Text(
                text = params.placeholder,
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.bodyMedium,
                    color = ColorApp.hintText,
                ),
            )
        },
        leadingIcon = {
            Icon(
                painter = params.leadingIcon,
                contentDescription = null,
                tint = ColorApp.textGray,
            )
        },
        supportingText = params.errorMessage?.let { message ->
            {
                Text(
                    text = message,
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.bodySmall,
                        color = ColorApp.errorColor,
                    ),
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = params.keyboardType,
            imeAction = ImeAction.Next,
        ),
        shape = RoundedCornerShape(12.dp),
        textStyle = plazaOnTextStyle(
            base = MaterialTheme.typography.bodyMedium,
            color = ColorApp.textColor,
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ColorApp.primary,
            unfocusedBorderColor = ColorApp.cardBackGroundGray,
            errorBorderColor = ColorApp.errorColor,
            cursorColor = ColorApp.primary,
            focusedContainerColor = ColorApp.cardBackGroundGray,
            unfocusedContainerColor = ColorApp.cardBackGroundGray,
            errorContainerColor = ColorApp.cardBackGroundGray,
            focusedTextColor = ColorApp.textColor,
            unfocusedTextColor = ColorApp.textColor,
        ),
    )
}
