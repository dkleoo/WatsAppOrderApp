package com.example.watsapporder.presentation.screens.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.watsapporder.data.mappers.AuthProvider
import com.example.watsapporder.data.mappers.LoggedUser
import com.example.watsapporder.data.repositoyImpl.login.LoginResults
import com.example.watsapporder.domain.useCase.login.AuthUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginScreenState(
    val isLoading: Boolean = false,
    val authenticatingProvider: AuthProvider? = null,
    val errorMessage: String? = null,
    val loggedUser: LoggedUser? = null,
)

class LoginViewModel(private val authUseCases: AuthUseCases) : ScreenModel {

    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState = _uiState.asStateFlow()

    fun restoreSession() {
        screenModelScope.launch {
            authUseCases.restoreSession()?.let { user ->
                _uiState.update { it.copy(loggedUser = user) }
            }
        }
    }

    fun signInWithGoogle() {
        if (_uiState.value.isLoading) return

        screenModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    authenticatingProvider = AuthProvider.GOOGLE,
                    errorMessage = null,
                )
            }
            when (val result = authUseCases.signInWithGoogle()) {
                is LoginResults.Success -> _uiState.update {
                    it.copy(isLoading = false, authenticatingProvider = null, loggedUser = result.user)
                }

                is LoginResults.MessageError -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        authenticatingProvider = null,
                        errorMessage = result.message.ifBlank { null },
                    )
                }
            }
        }
    }
}
