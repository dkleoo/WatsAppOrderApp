package com.example.watsapporder.data.remote.product

import com.example.watsapporder.data.mappers.ProductRequest
import com.example.watsapporder.data.mappers.ProductResponse

interface ProductsServices {
    suspend fun getProducts(): List<ProductResponse>
    suspend fun getProduct(id: Int): ProductResponse
    suspend fun saveProduct(request: ProductRequest): ProductResponse
    suspend fun updateProduct(id: Int, request: ProductRequest): ProductResponse
    suspend fun deleteProduct(id: Int)
}
