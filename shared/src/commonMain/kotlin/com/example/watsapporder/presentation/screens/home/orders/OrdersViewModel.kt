package com.example.watsapporder.presentation.screens.home.orders

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.watsapporder.data.mappers.OrderResponse
import com.example.watsapporder.data.mappers.OrderStatus
import com.example.watsapporder.data.repositoyImpl.order.OrderResults
import com.example.watsapporder.domain.useCase.order.OrdersUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class OrderFilter {
    ALL,
    PENDING,
    KITCHEN,
    ON_ROUTE,
    DELIVERED,
}

data class OrdersScreenState(
    val orders: List<OrderResponse> = emptyList(),
    val filter: OrderFilter = OrderFilter.ALL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val visibleOrders: List<OrderResponse>
        get() = when (filter) {
            OrderFilter.ALL -> orders
            OrderFilter.PENDING -> orders.filter { it.status == OrderStatus.PENDING }
            OrderFilter.KITCHEN -> orders.filter { it.status == OrderStatus.KITCHEN }
            OrderFilter.ON_ROUTE -> orders.filter { it.status == OrderStatus.ON_ROUTE }
            OrderFilter.DELIVERED -> orders.filter { it.status == OrderStatus.DELIVERED }
        }

    val pendingCount: Int
        get() = orders.count { it.status == OrderStatus.PENDING }
}

class OrdersViewModel(
    private val ordersUseCases: OrdersUseCases,
) : ScreenModel {

    private val _uiState = MutableStateFlow(OrdersScreenState())
    val uiState = _uiState.asStateFlow()

    private var streamJob: Job? = null

    fun start() {
        loadOrders()
        if (streamJob != null) return
        streamJob = screenModelScope.launch {
            ordersUseCases.streamOrders(
                startSequence = ordersUseCases.lastSequence(),
                onSequence = ordersUseCases::saveLastSequence,
            ).collect { order ->
                mergeOrder(order)
            }
        }
    }

    fun selectFilter(filter: OrderFilter) {
        _uiState.update { it.copy(filter = filter) }
    }

    fun updateStatus(orderId: Int, status: OrderStatus) {
        screenModelScope.launch {
            when (val result = ordersUseCases.updateStatus(orderId, status)) {
                is OrderResults.Order -> mergeOrder(result.item)
                is OrderResults.Orders -> Unit
                is OrderResults.Sequence -> Unit
                is OrderResults.MessageError -> _uiState.update {
                    it.copy(errorMessage = result.message.ifBlank { null })
                }
            }
        }
    }

    private fun loadOrders() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = ordersUseCases.getOrders()) {
                is OrderResults.Orders -> {
                    val sorted = result.items.sortedBy { order -> order.sequence }
                    sorted.maxOfOrNull { it.sequence }?.let(ordersUseCases::saveLastSequence)
                    _uiState.update { it.copy(isLoading = false, orders = sorted) }
                }

                is OrderResults.Order -> _uiState.update { it.copy(isLoading = false) }

                is OrderResults.Sequence -> _uiState.update { it.copy(isLoading = false) }

                is OrderResults.MessageError -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message.ifBlank { null })
                }
            }
        }
    }

    private fun mergeOrder(order: OrderResponse) {
        _uiState.update { state ->
            val existing = state.orders.any { it.id == order.id }
            val updated = if (existing) {
                state.orders.map { if (it.id == order.id) order else it }
            } else {
                state.orders + order
            }
            state.copy(orders = updated.sortedBy { it.sequence })
        }
    }
}
