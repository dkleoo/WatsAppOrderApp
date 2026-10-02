package com.example.watsapporder.presentation.screens.login.email

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.watsapporder.data.mappers.LoggedUser
import com.example.watsapporder.data.repositoyImpl.login.LoginResults
import com.example.watsapporder.domain.useCase.login.AuthUseCases
import com.example.watsapporder.domain.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.email_error_invalid
import watsapporder.shared.generated.resources.email_error_required
import watsapporder.shared.generated.resources.name_error_required
import watsapporder.shared.generated.resources.password_error_required

data class EmailLoginScreenState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isPasswordVisible: Boolean = false,
    val isRegisterMode: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: StringResource? = null,
    val passwordError: StringResource? = null,
    val nameError: StringResource? = null,
    val errorMessage: String? = null,
    val loggedUser: LoggedUser? = null,
)

class EmailLoginViewModel(private val authUseCases: AuthUseCases) : ScreenModel {

    private val _uiState = MutableStateFlow(EmailLoginScreenState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, errorMessage = null) }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, nameError = null, errorMessage = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onToggleMode() {
        _uiState.update {
            it.copy(isRegisterMode = !it.isRegisterMode, errorMessage = null)
        }
    }

    fun submit() {
        if (_uiState.value.isLoading) return

        val current = _uiState.value
        val emailError = when {
            current.email.isBlank() -> Res.string.email_error_required
            !current.email.isValidEmail() -> Res.string.email_error_invalid
            else -> null
        }
        val passwordError = if (current.password.isBlank()) {
            Res.string.password_error_required
        } else {
            null
        }
        val nameError = if (current.isRegisterMode && current.name.isBlank()) {
            Res.string.name_error_required
        } else {
            null
        }

        if (emailError != null || passwordError != null || nameError != null) {
            _uiState.update {
                it.copy(emailError = emailError, passwordError = passwordError, nameError = nameError)
            }
            return
        }

        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = if (current.isRegisterMode) {
                authUseCases.registerWithEmail(
                    email = current.email.trim(),
                    password = current.password,
                    name = current.name.trim(),
                )
            } else {
                authUseCases.signInWithEmail(current.email.trim(), current.password)
            }
            when (result) {
                is LoginResults.Success -> _uiState.update {
                    it.copy(isLoading = false, loggedUser = result.user)
                }

                is LoginResults.MessageError -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message.ifBlank { null })
                }
            }
        }
    }
}
