package com.example.watsapporder.presentation.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.watsapporder.data.mappers.AuthProvider
import com.example.watsapporder.presentation.screens.login.components.FederatedLoginContent
import com.example.watsapporder.presentation.screens.routes.Routes

class LoginScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<LoginViewModel>()
        val state by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            viewModel.restoreSession()
        }

        LaunchedEffect(state.loggedUser) {
            state.loggedUser?.let { loggedUser ->
                navigator.push(Routes.HOME_SCREEN(loggedUser))
            }
        }

        FederatedLoginContent(
            isLoading = state.isLoading,
            authenticatingProvider = state.authenticatingProvider,
            errorMessage = state.errorMessage,
            onProviderSelected = { provider ->
                when (provider) {
                    AuthProvider.GOOGLE -> viewModel.signInWithGoogle()
                    AuthProvider.EMAIL -> navigator.push(Routes.EMAIL_LOGIN_SCREEN)
                    AuthProvider.PHONE -> navigator.push(Routes.PHONE_LOGIN_SCREEN)
                }
            },
        )
    }
}
