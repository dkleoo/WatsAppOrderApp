package com.example.watsapporder.presentation.screens.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.watsapporder.data.params.TextIconButtonParams
import com.example.watsapporder.presentation.theme.plazaOnTextStyle

@Composable
fun TextIconButton(params: TextIconButtonParams) {
    Button(
        onClick = params.onClick,
        modifier = params.modifier
            .fillMaxWidth()
            .height(46.dp),
        enabled = params.enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = params.containerColor,
            contentColor = params.contentColor,
            disabledContainerColor = params.containerColor,
            disabledContentColor = params.contentColor,
        ),
        contentPadding = PaddingValues(horizontal = 12.dp),
    ) {
        Icon(
            painter = params.icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = params.iconTint,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = params.text,
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.labelLarge,
                color = params.contentColor,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}
