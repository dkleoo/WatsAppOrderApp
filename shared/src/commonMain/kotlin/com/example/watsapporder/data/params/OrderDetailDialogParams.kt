package com.example.watsapporder.data.params

import com.example.watsapporder.data.mappers.OrderResponse

data class OrderDetailDialogParams(
    val order: OrderResponse,
    val isLoading: Boolean,
    val isUpdating: Boolean,
    val onDismiss: () -> Unit,
    val onAccept: (Int) -> Unit,
    val onReject: (Int) -> Unit,
    val onSendOnTheWay: (Int) -> Unit,
)
