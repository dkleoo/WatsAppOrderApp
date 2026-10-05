package com.example.watsapporder.data.repositoyImpl.order

import com.example.watsapporder.data.mappers.OrderResponse

sealed class OrderResults {
    data class Orders(val items: List<OrderResponse>) : OrderResults()

    data class Order(val item: OrderResponse) : OrderResults()

    data class Sequence(val value: Long) : OrderResults()

    data class MessageError(val message: String) : OrderResults()
}
