package com.example.watsapporder.presentation.screens.home.create

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.watsapporder.data.mappers.CreateProductForm
import com.example.watsapporder.data.mappers.CreateStepForm
import com.example.watsapporder.data.mappers.InputResponse
import com.example.watsapporder.data.mappers.ProductType
import com.example.watsapporder.data.mappers.toRequest
import com.example.watsapporder.data.repositoyImpl.input.InputResults
import com.example.watsapporder.data.repositoyImpl.product.ProductResults
import com.example.watsapporder.domain.useCase.input.InputsUseCases
import com.example.watsapporder.domain.useCase.product.ProductsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.create_error_inputs_required
import watsapporder.shared.generated.resources.create_error_name
import watsapporder.shared.generated.resources.create_error_price
import watsapporder.shared.generated.resources.create_error_step_inputs
import watsapporder.shared.generated.resources.create_error_step_name
import watsapporder.shared.generated.resources.create_error_type

data class CreateProductScreenState(
    val availableInputs: List<InputResponse> = emptyList(),
    val form: CreateProductForm = CreateProductForm(),
    val isLoadingInputs: Boolean = false,
    val isSaving: Boolean = false,
    val typeError: StringResource? = null,
    val nameError: StringResource? = null,
    val priceError: StringResource? = null,
    val stepsError: StringResource? = null,
    val stepErrors: Map<Int, StringResource> = emptyMap(),
    val inputErrors: Map<Int, StringResource> = emptyMap(),
    val errorMessage: String? = null,
    val createdProductName: String? = null,
)

class CreateProductViewModel(
    private val productsUseCases: ProductsUseCases,
    private val inputsUseCases: InputsUseCases,
) : ScreenModel {

    private val _uiState = MutableStateFlow(CreateProductScreenState())
    val uiState = _uiState.asStateFlow()

    fun loadInputs() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoadingInputs = true) }
            when (val result = inputsUseCases.getInputs()) {
                is InputResults.Inputs -> _uiState.update {
                    it.copy(isLoadingInputs = false, availableInputs = result.items)
                }

                is InputResults.Input -> _uiState.update { it.copy(isLoadingInputs = false) }

                is InputResults.MessageError -> _uiState.update {
                    it.copy(isLoadingInputs = false, errorMessage = result.message.ifBlank { null })
                }
            }
        }
    }

    fun selectType(type: ProductType) {
        _uiState.update { state ->
            state.copy(
                form = state.form.copy(
                    type = type,
                    steps = if (type == ProductType.WITH_INPUTS) {
                        state.form.steps.ifEmpty { listOf(CreateStepForm()) }
                    } else {
                        emptyList()
                    },
                ),
                typeError = null,
                stepsError = null,
                stepErrors = emptyMap(),
                inputErrors = emptyMap(),
            )
        }
    }

    fun onNameChange(value: String) {
        _uiState.update {
            it.copy(form = it.form.copy(name = value), nameError = null, errorMessage = null)
        }
    }

    fun onPriceChange(value: String) {
        _uiState.update {
            it.copy(form = it.form.copy(price = value), priceError = null, errorMessage = null)
        }
    }

    fun addStep() {
        _uiState.update {
            it.copy(
                form = it.form.copy(steps = it.form.steps + CreateStepForm()),
                stepsError = null,
            )
        }
    }

    fun removeStep(index: Int) {
        _uiState.update { state ->
            state.copy(
                form = state.form.copy(
                    steps = state.form.steps.filterIndexed { i, _ -> i != index },
                ),
                stepErrors = state.stepErrors.minus(index).mapKeys { (key, _) ->
                    if (key > index) key - 1 else key
                },
            )
        }
    }

    fun onStepNameChange(index: Int, value: String) {
        _uiState.update { state ->
            state.copy(
                form = state.form.copy(
                    steps = state.form.steps.mapIndexed { i, step ->
                        if (i == index) step.copy(name = value) else step
                    },
                ),
                stepErrors = state.stepErrors.minus(index),
            )
        }
    }

    fun toggleInput(stepIndex: Int, inputId: Int) {
        _uiState.update { state ->
            state.copy(
                form = state.form.copy(
                    steps = state.form.steps.mapIndexed { i, step ->
                        if (i != stepIndex) {
                            step
                        } else {
                            val ids = if (step.selectedInputIds.contains(inputId)) {
                                step.selectedInputIds - inputId
                            } else {
                                step.selectedInputIds + inputId
                            }
                            step.copy(selectedInputIds = ids)
                        }
                    },
                ),
                inputErrors = state.inputErrors.minus(stepIndex),
            )
        }
    }

    fun saveProduct(onCreated: () -> Unit = {}) {
        if (_uiState.value.isSaving) return

        val form = _uiState.value.form
        val typeError = if (form.type == null) Res.string.create_error_type else null
        val nameError = if (form.name.isBlank()) Res.string.create_error_name else null
        val parsedPrice = form.price.replace(",", ".").toDoubleOrNull()
        val priceError = if (parsedPrice == null) Res.string.create_error_price else null

        val isPrepare = form.type == ProductType.WITH_INPUTS
        val stepErrors = if (isPrepare) {
            form.steps.mapIndexedNotNull { index, step ->
                if (step.name.isBlank()) index to Res.string.create_error_step_name else null
            }.toMap()
        } else {
            emptyMap()
        }
        val inputErrors = if (isPrepare) {
            form.steps.mapIndexedNotNull { index, step ->
                if (step.selectedInputIds.isEmpty()) {
                    index to Res.string.create_error_step_inputs
                } else {
                    null
                }
            }.toMap()
        } else {
            emptyMap()
        }
        val stepsError = if (isPrepare && form.steps.isEmpty()) {
            Res.string.create_error_inputs_required
        } else {
            null
        }

        val hasErrors = typeError != null ||
            nameError != null ||
            priceError != null ||
            stepsError != null ||
            stepErrors.isNotEmpty() ||
            inputErrors.isNotEmpty()

        if (hasErrors) {
            _uiState.update {
                it.copy(
                    typeError = typeError,
                    nameError = nameError,
                    priceError = priceError,
                    stepsError = stepsError,
                    stepErrors = stepErrors,
                    inputErrors = inputErrors,
                )
            }
            return
        }

        screenModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, createdProductName = null) }
            when (val result = productsUseCases.saveProduct(form.toRequest())) {
                is ProductResults.Product -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            createdProductName = result.item.name,
                            form = CreateProductForm(
                                type = it.form.type,
                                steps = if (it.form.type == ProductType.WITH_INPUTS) {
                                    listOf(CreateStepForm())
                                } else {
                                    emptyList()
                                },
                            ),
                        )
                    }
                    onCreated()
                }

                is ProductResults.Products -> _uiState.update { it.copy(isSaving = false) }

                is ProductResults.Deleted -> _uiState.update { it.copy(isSaving = false) }

                is ProductResults.MessageError -> _uiState.update {
                    it.copy(isSaving = false, errorMessage = result.message.ifBlank { null })
                }
            }
        }
    }

    fun clearCreatedMessage() {
        _uiState.update { it.copy(createdProductName = null) }
    }
}
