package com.example.watsapporder.presentation.screens.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.watsapporder.presentation.theme.ColorApp
import com.example.watsapporder.presentation.theme.plazaOnTextStyle

@Composable
fun ButtonContainerGreen(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ColorApp.primary,
            contentColor = ColorApp.onPrimary,
            disabledContainerColor = ColorApp.grayButton,
            disabledContentColor = ColorApp.onPrimary,
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = ColorApp.white,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = text,
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.labelLarge,
                    color = ColorApp.white,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        }
    }
}
