package com.example.watsapporder.presentation.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watsapporder.presentation.theme.ColorApp
import com.example.watsapporder.presentation.theme.plazaOnTextStyle

@Composable
fun VerificationCodeInput(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input -> onValueChange(input.filter(Char::isDigit).take(6)) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        isError = isError,
        supportingText = errorMessage?.let { message ->
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
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        shape = RoundedCornerShape(12.dp),
        textStyle = plazaOnTextStyle(
            base = MaterialTheme.typography.headlineSmall,
            color = ColorApp.textColor,
            textAlign = TextAlign.Center,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
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
