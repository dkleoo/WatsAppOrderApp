package com.example.watsapporder.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.watsapporder.data.params.ProviderButtonParams
import com.example.watsapporder.presentation.theme.plazaOnTextStyle
import org.jetbrains.compose.resources.painterResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.ic_chevron_right

@Composable
fun ProviderButton(params: ProviderButtonParams) {
    Button(
        onClick = params.onClick,
        modifier = params.modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = params.enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = params.containerColor,
            contentColor = params.contentColor,
            disabledContainerColor = params.containerColor,
            disabledContentColor = params.contentColor,
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(params.iconContainerColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = params.icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = params.iconTint,
            )
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text = params.text,
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.labelLarge,
                color = params.contentColor,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        Spacer(Modifier.weight(1f))
        Icon(
            painter = painterResource(Res.drawable.ic_chevron_right),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = params.contentColor,
        )
    }
}
