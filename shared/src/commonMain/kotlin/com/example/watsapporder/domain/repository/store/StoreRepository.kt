package com.example.watsapporder.domain.repository.store

import com.example.watsapporder.data.mappers.StoreRequest
import com.example.watsapporder.data.repositoyImpl.store.StoreResults

interface StoreRepository {
    suspend fun getStore(): StoreResults
    suspend fun createStore(request: StoreRequest): StoreResults
    suspend fun updateStore(id: Int, request: StoreRequest): StoreResults
}
