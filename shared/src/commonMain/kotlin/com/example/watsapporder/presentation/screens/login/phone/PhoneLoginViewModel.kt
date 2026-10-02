package com.example.watsapporder.presentation.screens.login.phone

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.watsapporder.data.mappers.LoggedUser
import com.example.watsapporder.data.repositoyImpl.login.LoginResults
import com.example.watsapporder.data.repositoyImpl.login.PhoneResults
import com.example.watsapporder.domain.useCase.login.AuthUseCases
import com.example.watsapporder.domain.util.isValidPhone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.code_error_length
import watsapporder.shared.generated.resources.code_error_required
import watsapporder.shared.generated.resources.phone_error_invalid
import watsapporder.shared.generated.resources.phone_error_required

data class PhoneLoginScreenState(
    val phone: String = "",
    val code: String = "",
    val verificationId: String? = null,
    val isLoading: Boolean = false,
    val phoneError: StringResource? = null,
    val codeError: StringResource? = null,
    val errorMessage: String? = null,
    val loggedUser: LoggedUser? = null,
)

class PhoneLoginViewModel(private val authUseCases: AuthUseCases) : ScreenModel {

    private val _uiState = MutableStateFlow(PhoneLoginScreenState())
    val uiState = _uiState.asStateFlow()

    fun onPhoneChange(value: String) {
        _uiState.update { it.copy(phone = value, phoneError = null, errorMessage = null) }
    }

    fun onCodeChange(value: String) {
        _uiState.update { it.copy(code = value.filter(Char::isDigit).take(6), codeError = null, errorMessage = null) }
    }

    fun changeNumber() {
        _uiState.update { it.copy(verificationId = null, code = "", codeError = null, errorMessage = null) }
    }

    fun sendCode() {
        if (_uiState.value.isLoading) return

        val phone = _uiState.value.phone.trim()
        val phoneError = when {
            phone.isBlank() -> Res.string.phone_error_required
            !phone.isValidPhone() -> Res.string.phone_error_invalid
            else -> null
        }
        if (phoneError != null) {
            _uiState.update { it.copy(phoneError = phoneError) }
            return
        }

        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = authUseCases.sendPhoneCode(phone)) {
                is PhoneResults.CodeSent -> _uiState.update {
                    it.copy(isLoading = false, verificationId = result.verificationId)
                }

                is PhoneResults.Success -> _uiState.update {
                    it.copy(isLoading = false, loggedUser = result.user)
                }

                is PhoneResults.MessageError -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message.ifBlank { null })
                }
            }
        }
    }

    fun confirmCode() {
        if (_uiState.value.isLoading) return

        val current = _uiState.value
        val verificationId = current.verificationId
        if (verificationId == null) {
            _uiState.update { it.copy(errorMessage = null) }
            return
        }
        val codeError = when {
            current.code.isBlank() -> Res.string.code_error_required
            current.code.length < 6 -> Res.string.code_error_length
            else -> null
        }
        if (codeError != null) {
            _uiState.update { it.copy(codeError = codeError) }
            return
        }

        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = authUseCases.confirmPhoneCode(verificationId, current.code)) {
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
