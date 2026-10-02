package com.example.watsapporder.domain.repository.product

import com.example.watsapporder.data.mappers.ProductRequest
import com.example.watsapporder.data.repositoyImpl.product.ProductResults

interface ProductsRepository {
    suspend fun getProducts(): ProductResults
    suspend fun getProduct(id: Int): ProductResults
    suspend fun saveProduct(request: ProductRequest): ProductResults
    suspend fun updateProduct(id: Int, request: ProductRequest): ProductResults
    suspend fun deleteProduct(id: Int): ProductResults
}
