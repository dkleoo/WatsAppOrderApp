package com.example.watsapporder.presentation.screens.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watsapporder.data.mappers.AuthProvider
import com.example.watsapporder.data.params.LoadingDialogParams
import com.example.watsapporder.data.params.ProviderButtonParams
import com.example.watsapporder.presentation.screens.components.LoadingDialog
import com.example.watsapporder.presentation.screens.components.ProviderButton
import com.example.watsapporder.presentation.theme.ColorApp
import com.example.watsapporder.presentation.theme.plazaOnTextStyle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.brand_name
import watsapporder.shared.generated.resources.food_pasta
import watsapporder.shared.generated.resources.food_salad
import watsapporder.shared.generated.resources.ic_email
import watsapporder.shared.generated.resources.ic_google
import watsapporder.shared.generated.resources.ic_phone
import watsapporder.shared.generated.resources.login_authenticating_subtitle
import watsapporder.shared.generated.resources.login_authenticating_with
import watsapporder.shared.generated.resources.login_brand_tagline
import watsapporder.shared.generated.resources.login_provider_email
import watsapporder.shared.generated.resources.login_provider_google
import watsapporder.shared.generated.resources.login_provider_phone
import watsapporder.shared.generated.resources.login_quick_access_subtitle
import watsapporder.shared.generated.resources.login_quick_access_title
import watsapporder.shared.generated.resources.login_sso_verified
import watsapporder.shared.generated.resources.login_title

private data class ProviderUi(
    val label: String,
    val icon: Painter,
    val containerColor: Color,
    val contentColor: Color,
    val iconContainerColor: Color,
    val iconTint: Color,
)

@Composable
private fun providerUi(provider: AuthProvider): ProviderUi = when (provider) {
    AuthProvider.GOOGLE -> ProviderUi(
        label = stringResource(Res.string.login_provider_google),
        icon = painterResource(Res.drawable.ic_google),
        containerColor = ColorApp.white,
        contentColor = ColorApp.stone800,
        iconContainerColor = ColorApp.googleRedSoft,
        iconTint = Color.Unspecified,
    )

    AuthProvider.EMAIL -> ProviderUi(
        label = stringResource(Res.string.login_provider_email),
        icon = painterResource(Res.drawable.ic_email),
        containerColor = ColorApp.espressoDark,
        contentColor = ColorApp.white,
        iconContainerColor = ColorApp.whiteOverlay10,
        iconTint = ColorApp.white,
    )

    AuthProvider.PHONE -> ProviderUi(
        label = stringResource(Res.string.login_provider_phone),
        icon = painterResource(Res.drawable.ic_phone),
        containerColor = ColorApp.amberGold,
        contentColor = ColorApp.white,
        iconContainerColor = ColorApp.whiteOverlay20,
        iconTint = ColorApp.white,
    )
}

@Composable
fun FederatedLoginContent(
    isLoading: Boolean,
    authenticatingProvider: AuthProvider?,
    errorMessage: String?,
    onProviderSelected: (AuthProvider) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorApp.amberGold),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 420.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(48.dp))
            Text(
                text = stringResource(Res.string.brand_name),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.headlineSmall,
                    color = ColorApp.white,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(Res.string.login_brand_tagline).uppercase(),
                textAlign = TextAlign.Center,
                letterSpacing = 3.sp,
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.labelSmall,
                    color = ColorApp.whiteOverlay85,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = stringResource(Res.string.login_title),
                textAlign = TextAlign.Center,
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.headlineMedium,
                    color = ColorApp.white,
                    fontWeight = FontWeight.ExtraBold,
                ),
            )
            Spacer(Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                FoodPlates()
                LoginCard(
                    modifier = Modifier.padding(top = 118.dp),
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onProviderSelected = onProviderSelected,
                )
            }
            Spacer(Modifier.height(24.dp))
            HomeIndicator()
            Spacer(Modifier.height(16.dp))
        }
    }

    if (authenticatingProvider != null) {
        val ui = providerUi(authenticatingProvider)
        LoadingDialog(
            LoadingDialogParams(
                visible = isLoading,
                title = stringResource(Res.string.login_authenticating_with, ui.label),
                subtitle = stringResource(Res.string.login_authenticating_subtitle),
                badgeText = stringResource(Res.string.login_sso_verified),
                icon = ui.icon,
                iconContainerColor = ui.iconContainerColor,
                iconTint = ui.iconTint,
            ),
        )
    }
}

@Composable
private fun FoodPlates() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
    ) {
        Image(
            painter = painterResource(Res.drawable.food_salad),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterStart)
                .offset(x = (-36).dp, y = (-8).dp)
                .rotate(-12f)
                .clip(CircleShape)
                .border(4.dp, ColorApp.whiteOverlay40, CircleShape),
        )
        Image(
            painter = painterResource(Res.drawable.food_pasta),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 40.dp, y = 8.dp)
                .rotate(6f)
                .clip(CircleShape)
                .border(4.dp, ColorApp.whiteOverlay40, CircleShape),
        )
    }
}

@Composable
private fun LoginCard(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    errorMessage: String?,
    onProviderSelected: (AuthProvider) -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = 34.dp,
            topEnd = 34.dp,
            bottomStart = 26.dp,
            bottomEnd = 26.dp,
        ),
        color = ColorApp.glassWhite,
        shadowElevation = 24.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(ColorApp.stone300),
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.login_quick_access_title),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.titleMedium,
                    color = ColorApp.textColor,
                    fontWeight = FontWeight.ExtraBold,
                ),
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = stringResource(Res.string.login_quick_access_subtitle),
                textAlign = TextAlign.Center,
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.bodySmall,
                    color = ColorApp.textGray,
                ),
            )
            Spacer(Modifier.height(16.dp))

            val providers = listOf(
                AuthProvider.GOOGLE,
                AuthProvider.EMAIL,
                AuthProvider.PHONE,
            )
            providers.forEach { provider ->
                val ui = providerUi(provider)
                ProviderButton(
                    ProviderButtonParams(
                        text = ui.label,
                        icon = ui.icon,
                        onClick = { onProviderSelected(provider) },
                        containerColor = ui.containerColor,
                        contentColor = ui.contentColor,
                        iconContainerColor = ui.iconContainerColor,
                        iconTint = ui.iconTint,
                        enabled = !isLoading,
                    ),
                )
                Spacer(Modifier.height(10.dp))
            }

            if (errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    textAlign = TextAlign.Center,
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.bodySmall,
                        color = ColorApp.errorColor,
                    ),
                )
            }
        }
    }
}

@Composable
private fun HomeIndicator() {
    Box(
        modifier = Modifier
            .width(112.dp)
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(ColorApp.whiteOverlay60),
    )
}
