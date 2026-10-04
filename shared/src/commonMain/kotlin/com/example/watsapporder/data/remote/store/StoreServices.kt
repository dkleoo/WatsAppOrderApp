package com.example.watsapporder.data.remote.store

import com.example.watsapporder.data.mappers.StoreRequest
import com.example.watsapporder.data.mappers.StoreResponse

interface StoreServices {
    suspend fun getStore(token: String): StoreResponse?
    suspend fun createStore(token: String, request: StoreRequest): StoreResponse
    suspend fun updateStore(token: String, id: Int, request: StoreRequest): StoreResponse
}
