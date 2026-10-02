package com.example.watsapporder.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.takeOrElse

@Composable
fun plazaOnTextStyle(
    base: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
): TextStyle = base.copy(
    color = color.takeOrElse { base.color },
    fontSize = fontSize.takeOrElse { base.fontSize },
    lineHeight = lineHeight.takeOrElse { base.lineHeight },
    fontWeight = fontWeight ?: base.fontWeight,
    textAlign = textAlign ?: base.textAlign,
    textDecoration = textDecoration ?: base.textDecoration,
)
