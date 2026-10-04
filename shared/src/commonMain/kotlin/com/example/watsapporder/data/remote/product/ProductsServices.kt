package com.example.watsapporder.data.remote.product

import com.example.watsapporder.data.mappers.ProductRequest
import com.example.watsapporder.data.mappers.ProductResponse

interface ProductsServices {
    suspend fun getProducts(token: String): List<ProductResponse>
    suspend fun getProduct(token: String, id: Int): ProductResponse
    suspend fun saveProduct(token: String, request: ProductRequest): ProductResponse
    suspend fun updateProduct(token: String, id: Int, request: ProductRequest): ProductResponse
    suspend fun deleteProduct(token: String, id: Int)
}
