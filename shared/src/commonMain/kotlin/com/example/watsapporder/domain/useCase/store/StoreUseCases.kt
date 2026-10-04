package com.example.watsapporder.domain.useCase.store

import com.example.watsapporder.data.mappers.StoreRequest
import com.example.watsapporder.data.repositoyImpl.store.StoreResults
import com.example.watsapporder.domain.repository.store.StoreRepository

class StoreUseCases(private val storeRepository: StoreRepository) {
    suspend fun getStore(): StoreResults = storeRepository.getStore()

    suspend fun getStoreForUser(userId: Int): StoreResults = storeRepository.getStoreForUser(userId)

    suspend fun updateStore(id: Int, request: StoreRequest): StoreResults =
        storeRepository.updateStore(id, request)
}
