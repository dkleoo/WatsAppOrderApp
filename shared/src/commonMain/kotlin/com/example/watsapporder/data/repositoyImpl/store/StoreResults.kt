package com.example.watsapporder.data.repositoyImpl.store

import com.example.watsapporder.data.mappers.StoreResponse

sealed class StoreResults {
    data class Store(val item: StoreResponse) : StoreResults()

    data class MessageError(val message: String) : StoreResults()
}
