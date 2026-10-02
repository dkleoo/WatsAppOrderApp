package com.example.watsapporder.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.jakarta_bold
import watsapporder.shared.generated.resources.jakarta_medium
import watsapporder.shared.generated.resources.jakarta_regular
import watsapporder.shared.generated.resources.jakarta_semibold
import watsapporder.shared.generated.resources.outfit_bold
import watsapporder.shared.generated.resources.outfit_extrabold
import watsapporder.shared.generated.resources.outfit_regular
import watsapporder.shared.generated.resources.outfit_semibold

@Composable
fun appTypography(): Typography {
    val outfit = FontFamily(
        Font(Res.font.outfit_regular, FontWeight.Normal),
        Font(Res.font.outfit_semibold, FontWeight.SemiBold),
        Font(Res.font.outfit_bold, FontWeight.Bold),
        Font(Res.font.outfit_extrabold, FontWeight.ExtraBold),
    )
    val jakarta = FontFamily(
        Font(Res.font.jakarta_regular, FontWeight.Normal),
        Font(Res.font.jakarta_medium, FontWeight.Medium),
        Font(Res.font.jakarta_semibold, FontWeight.SemiBold),
        Font(Res.font.jakarta_bold, FontWeight.Bold),
    )
    val base = Typography()

    return base.copy(
        displayLarge = base.displayLarge.copy(fontFamily = outfit),
        displayMedium = base.displayMedium.copy(fontFamily = outfit),
        displaySmall = base.displaySmall.copy(fontFamily = outfit),
        headlineLarge = base.headlineLarge.copy(fontFamily = outfit),
        headlineMedium = base.headlineMedium.copy(fontFamily = outfit),
        headlineSmall = base.headlineSmall.copy(fontFamily = outfit),
        titleLarge = base.titleLarge.copy(fontFamily = outfit),
        titleMedium = base.titleMedium.copy(fontFamily = outfit),
        bodyLarge = base.bodyLarge.copy(fontFamily = jakarta),
        bodyMedium = base.bodyMedium.copy(fontFamily = jakarta),
        bodySmall = base.bodySmall.copy(fontFamily = jakarta),
        labelLarge = base.labelLarge.copy(fontFamily = jakarta),
        labelMedium = base.labelMedium.copy(fontFamily = jakarta),
        labelSmall = base.labelSmall.copy(fontFamily = jakarta),
    )
}
