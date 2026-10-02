package com.example.watsapporder.presentation.screens.home.inputs

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.watsapporder.data.mappers.InputCreateRequest
import com.example.watsapporder.data.mappers.InputResponse
import com.example.watsapporder.data.repositoyImpl.input.InputResults
import com.example.watsapporder.domain.useCase.input.InputsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.inputs_error_name
import watsapporder.shared.generated.resources.inputs_error_price

data class InputsScreenState(
    val inputs: List<InputResponse> = emptyList(),
    val name: String = "",
    val price: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val nameError: StringResource? = null,
    val priceError: StringResource? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)

class InputsViewModel(private val inputsUseCases: InputsUseCases) : ScreenModel {

    private val _uiState = MutableStateFlow(InputsScreenState())
    val uiState = _uiState.asStateFlow()

    fun loadInputs() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = inputsUseCases.getInputs()) {
                is InputResults.Inputs -> _uiState.update {
                    it.copy(isLoading = false, inputs = result.items)
                }

                is InputResults.Input -> _uiState.update { it.copy(isLoading = false) }

                is InputResults.MessageError -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message.ifBlank { null })
                }
            }
        }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, nameError = null, errorMessage = null) }
    }

    fun onPriceChange(value: String) {
        _uiState.update { it.copy(price = value, priceError = null, errorMessage = null) }
    }

    fun saveInput(onSaved: () -> Unit = {}) {
        if (_uiState.value.isSaving) return

        val current = _uiState.value
        val nameError = if (current.name.isBlank()) Res.string.inputs_error_name else null
        val parsedPrice = if (current.price.isBlank()) {
            0.0
        } else {
            current.price.replace(",", ".").toDoubleOrNull()
        }
        val priceError = if (current.price.isNotBlank() && parsedPrice == null) {
            Res.string.inputs_error_price
        } else {
            null
        }

        if (nameError != null || priceError != null) {
            _uiState.update { it.copy(nameError = nameError, priceError = priceError) }
            return
        }

        screenModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, successMessage = null) }
            val request = InputCreateRequest(
                name = current.name.trim(),
                price = parsedPrice ?: 0.0,
                cost = 0.0,
                quantity = 0,
            )
            when (val result = inputsUseCases.createInput(request)) {
                is InputResults.Input -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            name = "",
                            price = "",
                            successMessage = result.item.name,
                            inputs = it.inputs + result.item,
                        )
                    }
                    onSaved()
                }

                is InputResults.Inputs -> _uiState.update { it.copy(isSaving = false) }

                is InputResults.MessageError -> _uiState.update {
                    it.copy(isSaving = false, errorMessage = result.message.ifBlank { null })
                }
            }
        }
    }
}
