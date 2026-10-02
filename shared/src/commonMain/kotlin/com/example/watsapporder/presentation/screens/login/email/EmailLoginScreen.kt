package com.example.watsapporder.presentation.screens.login.email

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
import com.example.watsapporder.data.params.PasswordTextFieldParams
import com.example.watsapporder.presentation.screens.components.AppTextField
import com.example.watsapporder.presentation.screens.components.ButtonContainerGreen
import com.example.watsapporder.presentation.screens.components.PasswordTextField
import com.example.watsapporder.presentation.screens.routes.Routes
import com.example.watsapporder.presentation.theme.ColorApp
import com.example.watsapporder.presentation.theme.plazaOnTextStyle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.auth_back
import watsapporder.shared.generated.resources.email_button_register
import watsapporder.shared.generated.resources.email_button_sign_in
import watsapporder.shared.generated.resources.email_placeholder
import watsapporder.shared.generated.resources.email_screen_subtitle
import watsapporder.shared.generated.resources.email_screen_title
import watsapporder.shared.generated.resources.email_toggle_to_register
import watsapporder.shared.generated.resources.email_toggle_to_sign_in
import watsapporder.shared.generated.resources.ic_arrow_back
import watsapporder.shared.generated.resources.ic_email
import watsapporder.shared.generated.resources.ic_person
import watsapporder.shared.generated.resources.name_placeholder
import watsapporder.shared.generated.resources.password_placeholder

class EmailLoginScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<EmailLoginViewModel>()
        val state by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val emailError = state.emailError?.let { stringResource(it) }
        val passwordError = state.passwordError?.let { stringResource(it) }
        val nameError = state.nameError?.let { stringResource(it) }
        val errorMessage = state.errorMessage

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
                text = stringResource(Res.string.email_screen_title),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.headlineSmall,
                    color = ColorApp.textColor,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(Res.string.email_screen_subtitle),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.bodySmall,
                    color = ColorApp.textGray,
                ),
            )
            Spacer(Modifier.height(24.dp))
            if (state.isRegisterMode) {
                AppTextField(
                    AppTextFieldParams(
                        value = state.name,
                        onValueChange = viewModel::onNameChange,
                        placeholder = stringResource(Res.string.name_placeholder),
                        leadingIcon = painterResource(Res.drawable.ic_person),
                        keyboardType = KeyboardType.Text,
                        isError = state.nameError != null,
                        errorMessage = nameError,
                    ),
                )
                Spacer(Modifier.height(12.dp))
            }
            AppTextField(
                AppTextFieldParams(
                    value = state.email,
                    onValueChange = viewModel::onEmailChange,
                    placeholder = stringResource(Res.string.email_placeholder),
                    leadingIcon = painterResource(Res.drawable.ic_email),
                    keyboardType = KeyboardType.Email,
                    isError = state.emailError != null,
                    errorMessage = emailError,
                ),
            )
            Spacer(Modifier.height(12.dp))
            PasswordTextField(
                PasswordTextFieldParams(
                    value = state.password,
                    onValueChange = viewModel::onPasswordChange,
                    placeholder = stringResource(Res.string.password_placeholder),
                    isPasswordVisible = state.isPasswordVisible,
                    onToggleVisibility = viewModel::onTogglePasswordVisibility,
                    isError = state.passwordError != null,
                    errorMessage = passwordError,
                ),
            )
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
                onClick = viewModel::submit,
                text = stringResource(
                    if (state.isRegisterMode) Res.string.email_button_register else Res.string.email_button_sign_in,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                isLoading = state.isLoading,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(
                    if (state.isRegisterMode) {
                        Res.string.email_toggle_to_sign_in
                    } else {
                        Res.string.email_toggle_to_register
                    },
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { viewModel.onToggleMode() },
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.bodySmall,
                    color = ColorApp.primary,
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
    }
}
