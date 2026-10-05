package com.example.watsapporder.data.repositoyImpl.store

import com.example.watsapporder.data.local.SessionStore
import com.example.watsapporder.data.mappers.StoreRequest
import com.example.watsapporder.data.remote.store.StoreServices
import com.example.watsapporder.domain.repository.store.StoreRepository

class StoreRepositoryImpl(
    private val services: StoreServices,
    private val sessionStore: SessionStore,
) : StoreRepository {

    override suspend fun getStore(): StoreResults = loadStore()

    override suspend fun getStoreForUser(userId: Int): StoreResults = loadStore()

    override suspend fun updateStore(id: Int, request: StoreRequest): StoreResults = try {
        val token = sessionStore.get()?.token.orEmpty()
        StoreResults.Store(services.updateStore(token, id, request))
    } catch (exception: Exception) {
        StoreResults.MessageError(exception.message.orEmpty())
    }

    private suspend fun loadStore(): StoreResults = try {
        val session = sessionStore.get()
        val storeId = session?.storeId
        if (storeId == null) {
            StoreResults.MessageError(STORE_NOT_FOUND)
        } else {
            val store = services.getStore(session.token, storeId)
            if (store == null) {
                StoreResults.MessageError(STORE_NOT_FOUND)
            } else {
                StoreResults.Store(store)
            }
        }
    } catch (exception: Exception) {
        StoreResults.MessageError(exception.message.orEmpty())
    }

    companion object {
        const val STORE_NOT_FOUND = "store_not_found"
    }
}
