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
    ON_THE_WAY,
    DELIVERED,
}

data class OrdersScreenState(
    val orders: List<OrderResponse> = emptyList(),
    val filter: OrderFilter = OrderFilter.ALL,
    val isLoading: Boolean = false,
    val isDetailLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val detail: OrderResponse? = null,
    val errorMessage: String? = null,
) {
    val visibleOrders: List<OrderResponse>
        get() = when (filter) {
            OrderFilter.ALL -> orders
            OrderFilter.PENDING -> orders.filter { it.status == OrderStatus.PENDING }
            OrderFilter.KITCHEN -> orders.filter { it.status == OrderStatus.IN_KITCHEN }
            OrderFilter.ON_THE_WAY -> orders.filter { it.status == OrderStatus.ON_THE_WAY }
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

    fun openDetail(order: OrderResponse) {
        _uiState.update { it.copy(detail = order, isDetailLoading = true, errorMessage = null) }
        screenModelScope.launch {
            when (val result = ordersUseCases.getOrderDetail(order.id)) {
                is OrderResults.Order -> _uiState.update {
                    it.copy(detail = result.item, isDetailLoading = false)
                }

                is OrderResults.Orders -> _uiState.update { it.copy(isDetailLoading = false) }

                is OrderResults.Sequence -> _uiState.update { it.copy(isDetailLoading = false) }

                is OrderResults.MessageError -> _uiState.update {
                    it.copy(isDetailLoading = false, errorMessage = result.message.ifBlank { null })
                }
            }
        }
    }

    fun closeDetail() {
        _uiState.update { it.copy(detail = null, errorMessage = null) }
    }

    fun acceptOrder(orderId: Int) = changeStatus(orderId, OrderStatus.IN_KITCHEN)

    fun sendOnTheWay(orderId: Int) = changeStatus(orderId, OrderStatus.ON_THE_WAY)

    fun rejectOrder(orderId: Int) = changeStatus(orderId, OrderStatus.CANCELLED)

    fun markDelivered(orderId: Int) = changeStatus(orderId, OrderStatus.DELIVERED)

    private fun changeStatus(orderId: Int, status: OrderStatus) {
        if (_uiState.value.isUpdating) return
        screenModelScope.launch {
            _uiState.update { it.copy(isUpdating = true, errorMessage = null) }
            when (val result = ordersUseCases.updateStatus(orderId, status)) {
                is OrderResults.Order -> {
                    mergeOrder(result.item)
                    _uiState.update { it.copy(isUpdating = false, detail = null) }
                }

                is OrderResults.Orders -> _uiState.update { it.copy(isUpdating = false) }

                is OrderResults.Sequence -> _uiState.update { it.copy(isUpdating = false) }

                is OrderResults.MessageError -> _uiState.update {
                    it.copy(isUpdating = false, errorMessage = result.message.ifBlank { null })
                }
            }
        }
    }

    private fun loadOrders() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = ordersUseCases.getOrders()) {
                is OrderResults.Orders -> {
                    val sorted = result.items
                        .filter { it.status != OrderStatus.CANCELLED }
                        .sortedByDescending { it.sequence }
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
            if (order.status == OrderStatus.CANCELLED) {
                state.copy(orders = state.orders.filterNot { it.id == order.id })
            } else {
                val existing = state.orders.any { it.id == order.id }
                val updated = if (existing) {
                    state.orders.map { if (it.id == order.id) order else it }
                } else {
                    state.orders + order
                }
                state.copy(orders = updated.sortedByDescending { it.sequence })
            }
        }
    }
}
