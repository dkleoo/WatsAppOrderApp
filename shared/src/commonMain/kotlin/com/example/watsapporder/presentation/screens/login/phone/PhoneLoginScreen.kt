package com.example.watsapporder.presentation.screens.login.phone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.watsapporder.data.params.AppTextFieldParams
import com.example.watsapporder.presentation.screens.components.AppTextField
import com.example.watsapporder.presentation.screens.components.ButtonContainerGreen
import com.example.watsapporder.presentation.screens.components.VerificationCodeInput
import com.example.watsapporder.presentation.screens.routes.Routes
import com.example.watsapporder.presentation.theme.ColorApp
import com.example.watsapporder.presentation.theme.plazaOnTextStyle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.auth_back
import watsapporder.shared.generated.resources.code_placeholder
import watsapporder.shared.generated.resources.code_screen_subtitle
import watsapporder.shared.generated.resources.ic_arrow_back
import watsapporder.shared.generated.resources.ic_phone
import watsapporder.shared.generated.resources.phone_change_number
import watsapporder.shared.generated.resources.phone_placeholder
import watsapporder.shared.generated.resources.phone_screen_subtitle
import watsapporder.shared.generated.resources.phone_screen_title
import watsapporder.shared.generated.resources.phone_send_code
import watsapporder.shared.generated.resources.phone_verify

class PhoneLoginScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<PhoneLoginViewModel>()
        val state by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val phoneError = state.phoneError?.let { stringResource(it) }
        val codeError = state.codeError?.let { stringResource(it) }
        val errorMessage = state.errorMessage
        val hasVerification = state.verificationId != null

        LaunchedEffect(state.loggedUser) {
            state.loggedUser?.let { loggedUser ->
                navigator.push(Routes.HOME_SCREEN(loggedUser))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorApp.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = stringResource(Res.string.auth_back),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navigator.pop() },
                tint = ColorApp.textColor,
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(Res.string.phone_screen_title),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.headlineSmall,
                    color = ColorApp.textColor,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(
                    if (hasVerification) Res.string.code_screen_subtitle else Res.string.phone_screen_subtitle,
                ),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.bodySmall,
                    color = ColorApp.textGray,
                ),
            )
            Spacer(Modifier.height(24.dp))

            if (hasVerification) {
                VerificationCodeInput(
                    value = state.code,
                    onValueChange = viewModel::onCodeChange,
                    isError = state.codeError != null,
                    errorMessage = codeError,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                AppTextField(
                    AppTextFieldParams(
                        value = state.phone,
                        onValueChange = viewModel::onPhoneChange,
                        placeholder = stringResource(Res.string.phone_placeholder),
                        leadingIcon = painterResource(Res.drawable.ic_phone),
                        keyboardType = KeyboardType.Phone,
                        isError = state.phoneError != null,
                        errorMessage = phoneError,
                    ),
                )
            }

            if (errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.bodySmall,
                        color = ColorApp.errorColor,
                    ),
                )
            }
            Spacer(Modifier.height(24.dp))
            ButtonContainerGreen(
                onClick = if (hasVerification) viewModel::confirmCode else viewModel::sendCode,
                text = stringResource(
                    if (hasVerification) Res.string.phone_verify else Res.string.phone_send_code,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                isLoading = state.isLoading,
            )

            if (hasVerification) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.phone_change_number),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { viewModel.changeNumber() },
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.bodySmall,
                        color = ColorApp.primary,
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
        }
    }
}
