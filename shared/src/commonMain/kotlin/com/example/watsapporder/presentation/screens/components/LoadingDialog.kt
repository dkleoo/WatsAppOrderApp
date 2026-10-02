package com.example.watsapporder.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.watsapporder.data.params.LoadingDialogParams
import com.example.watsapporder.presentation.theme.ColorApp
import com.example.watsapporder.presentation.theme.plazaOnTextStyle

@Composable
fun LoadingDialog(params: LoadingDialogParams) {
    if (!params.visible) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorApp.scrim),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .padding(24.dp)
                .widthIn(max = 300.dp),
            shape = RoundedCornerShape(24.dp),
            color = ColorApp.white,
            shadowElevation = 24.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(params.iconContainerColor),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = params.icon,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = params.iconTint,
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = params.title,
                    textAlign = TextAlign.Center,
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.titleMedium,
                        color = ColorApp.textColor,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = params.subtitle,
                    textAlign = TextAlign.Center,
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.bodySmall,
                        color = ColorApp.textGray,
                    ),
                )
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    color = ColorApp.primary,
                    strokeWidth = 3.dp,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = params.badgeText,
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.labelSmall,
                        color = ColorApp.amberGoldDark,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
        }
    }
}
