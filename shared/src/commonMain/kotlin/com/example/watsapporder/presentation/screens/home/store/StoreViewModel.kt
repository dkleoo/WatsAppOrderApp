package com.example.watsapporder.presentation.screens.home.store

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.watsapporder.data.mappers.StoreRequest
import com.example.watsapporder.data.mappers.StoreResponse
import com.example.watsapporder.data.repositoyImpl.store.StoreRepositoryImpl
import com.example.watsapporder.data.repositoyImpl.store.StoreResults
import com.example.watsapporder.domain.useCase.store.StoreUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.store_error_required

data class StoreForm(
    val welcomeMessage: String = "",
    val address: String = "",
    val phone: String = "",
    val whatsappBusinessPhone: String = "",
    val idWhatsApp: String = "",
)

data class StoreScreenState(
    val store: StoreResponse? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEditorVisible: Boolean = false,
    val form: StoreForm = StoreForm(),
    val addressError: StringResource? = null,
    val phoneError: StringResource? = null,
    val whatsappError: StringResource? = null,
    val idWhatsAppError: StringResource? = null,
    val errorMessage: String? = null,
)

class StoreViewModel(
    private val storeUseCases: StoreUseCases,
) : ScreenModel {

    private val _uiState = MutableStateFlow(StoreScreenState())
    val uiState = _uiState.asStateFlow()

    fun bootstrap() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = storeUseCases.getStore()) {
                is StoreResults.Store -> _uiState.update {
                    it.copy(isLoading = false, store = result.item, form = result.item.toForm())
                }

                is StoreResults.MessageError -> {
                    if (result.message == StoreRepositoryImpl.STORE_NOT_FOUND) {
                        _uiState.update { it.copy(isLoading = false) }
                    } else {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = result.message.ifBlank { null })
                        }
                    }
                }
            }
        }
    }

    fun openEditor() {
        _uiState.update {
            it.copy(isEditorVisible = true, form = it.store?.toForm() ?: it.form)
        }
    }

    fun closeEditor() {
        _uiState.update { it.copy(isEditorVisible = false, errorMessage = null) }
    }

    fun onWelcomeMessageChange(value: String) {
        _uiState.update { it.copy(form = it.form.copy(welcomeMessage = value)) }
    }

    fun onAddressChange(value: String) {
        _uiState.update { it.copy(form = it.form.copy(address = value), addressError = null) }
    }

    fun onPhoneChange(value: String) {
        _uiState.update { it.copy(form = it.form.copy(phone = value), phoneError = null) }
    }

    fun onWhatsappChange(value: String) {
        _uiState.update { it.copy(form = it.form.copy(whatsappBusinessPhone = value), whatsappError = null) }
    }

    fun onIdWhatsAppChange(value: String) {
        _uiState.update { it.copy(form = it.form.copy(idWhatsApp = value), idWhatsAppError = null) }
    }

    fun save() {
        if (_uiState.value.isSaving) return

        val current = _uiState.value
        val addressError = if (current.form.address.isBlank()) Res.string.store_error_required else null
        val phoneError = if (current.form.phone.isBlank()) Res.string.store_error_required else null
        val whatsappError =
            if (current.form.whatsappBusinessPhone.isBlank()) Res.string.store_error_required else null
        val idWhatsAppError = if (current.form.idWhatsApp.isBlank()) Res.string.store_error_required else null

        if (addressError != null || phoneError != null || whatsappError != null || idWhatsAppError != null) {
            _uiState.update {
                it.copy(
                    addressError = addressError,
                    phoneError = phoneError,
                    whatsappError = whatsappError,
                    idWhatsAppError = idWhatsAppError,
                )
            }
            return
        }

        screenModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            val existing = current.store
            if (existing == null) {
                _uiState.update {
                    it.copy(isSaving = false, errorMessage = StoreRepositoryImpl.STORE_NOT_FOUND)
                }
                return@launch
            }
            val request = current.form.toRequest()
            when (val result = storeUseCases.updateStore(existing.id, request)) {
                is StoreResults.Store -> _uiState.update {
                    it.copy(
                        isSaving = false,
                        isEditorVisible = false,
                        store = result.item,
                        form = result.item.toForm(),
                    )
                }

                is StoreResults.MessageError -> _uiState.update {
                    it.copy(isSaving = false, errorMessage = result.message.ifBlank { null })
                }
            }
        }
    }
}

private fun StoreResponse.toForm(): StoreForm = StoreForm(
    welcomeMessage = welcomeMessage,
    address = address,
    phone = phone,
    whatsappBusinessPhone = whatsappBusinessPhone,
    idWhatsApp = idWhatsApp,
)

private fun StoreForm.toRequest(): StoreRequest = StoreRequest(
    welcomeMessage = welcomeMessage.trim(),
    address = address.trim(),
    phone = phone.trim(),
    whatsappBusinessPhone = whatsappBusinessPhone.trim(),
    idWhatsApp = idWhatsApp.trim(),
)
