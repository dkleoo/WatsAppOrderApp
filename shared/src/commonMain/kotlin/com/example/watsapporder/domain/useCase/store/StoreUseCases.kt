package com.example.watsapporder.domain.useCase.store

import com.example.watsapporder.data.mappers.StoreRequest
import com.example.watsapporder.data.repositoyImpl.store.StoreResults
import com.example.watsapporder.domain.repository.store.StoreRepository

class StoreUseCases(private val storeRepository: StoreRepository) {
    suspend fun getStore(): StoreResults = storeRepository.getStore()

    suspend fun createStore(request: StoreRequest): StoreResults =
        storeRepository.createStore(request)

    suspend fun updateStore(id: Int, request: StoreRequest): StoreResults =
        storeRepository.updateStore(id, request)
}
