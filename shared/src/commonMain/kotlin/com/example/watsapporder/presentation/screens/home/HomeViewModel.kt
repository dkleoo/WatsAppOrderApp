package com.example.watsapporder.presentation.screens.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.watsapporder.data.mappers.ProductResponse
import com.example.watsapporder.data.mappers.ProductType
import com.example.watsapporder.data.mappers.toRequest
import com.example.watsapporder.data.repositoyImpl.product.ProductResults
import com.example.watsapporder.domain.useCase.product.ProductsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.edit_product_error_price

enum class HomeTab {
    INPUTS,
    CREATE,
    MENU,
}

data class EditingProduct(
    val original: ProductResponse,
    val name: String,
    val price: String,
) {
    val canEditName: Boolean get() = original.type == ProductType.WITH_INPUTS
}

data class HomeScreenState(
    val selectedTab: HomeTab = HomeTab.MENU,
    val products: List<ProductResponse> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val priceError: StringResource? = null,
    val editing: EditingProduct? = null,
)

class HomeViewModel(private val productsUseCases: ProductsUseCases) : ScreenModel {

    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState = _uiState.asStateFlow()

    fun selectTab(tab: HomeTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun loadProducts() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = productsUseCases.getProducts()) {
                is ProductResults.Products -> _uiState.update {
                    it.copy(isLoading = false, products = result.items)
                }

                is ProductResults.Product -> _uiState.update { it.copy(isLoading = false) }

                is ProductResults.Deleted -> _uiState.update { it.copy(isLoading = false) }

                is ProductResults.MessageError -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message.ifBlank { null })
                }
            }
        }
    }

    fun openEditor(product: ProductResponse) {
        _uiState.update {
            it.copy(
                editing = EditingProduct(
                    original = product,
                    name = product.name,
                    price = product.price.toString(),
                ),
                errorMessage = null,
                priceError = null,
            )
        }
    }

    fun closeEditor() {
        _uiState.update { it.copy(editing = null, errorMessage = null, priceError = null) }
    }

    fun onNameChange(value: String) {
        _uiState.update { state -> state.copy(editing = state.editing?.copy(name = value)) }
    }

    fun onPriceChange(value: String) {
        _uiState.update { state ->
            state.copy(editing = state.editing?.copy(price = value), priceError = null)
        }
    }

    fun save() {
        val editing = _uiState.value.editing ?: return
        val price = editing.price.replace(",", ".").toDoubleOrNull()
        if (price == null) {
            _uiState.update { it.copy(priceError = Res.string.edit_product_error_price) }
            return
        }

        screenModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            val request = editing.original.toRequest(name = editing.name, price = price)
            when (val result = productsUseCases.updateProduct(editing.original.id, request)) {
                is ProductResults.Product -> _uiState.update { state ->
                    state.copy(
                        isSaving = false,
                        editing = null,
                        products = state.products.map { product ->
                            if (product.id == result.item.id) result.item else product
                        },
                    )
                }

                is ProductResults.Products -> _uiState.update { it.copy(isSaving = false) }

                is ProductResults.Deleted -> _uiState.update { it.copy(isSaving = false) }

                is ProductResults.MessageError -> _uiState.update {
                    it.copy(isSaving = false, errorMessage = result.message.ifBlank { null })
                }
            }
        }
    }

    fun delete() {
        val editing = _uiState.value.editing ?: return
        val productId = editing.original.id

        screenModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            when (val result = productsUseCases.deleteProduct(productId)) {
                is ProductResults.Deleted -> _uiState.update { state ->
                    state.copy(
                        isSaving = false,
                        editing = null,
                        products = state.products.filterNot { it.id == result.id },
                    )
                }

                is ProductResults.Products -> _uiState.update { it.copy(isSaving = false) }

                is ProductResults.Product -> _uiState.update { it.copy(isSaving = false) }

                is ProductResults.MessageError -> _uiState.update {
                    it.copy(isSaving = false, errorMessage = result.message.ifBlank { null })
                }
            }
        }
    }
}
