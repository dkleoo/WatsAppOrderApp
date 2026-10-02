package com.example.watsapporder.domain.useCase.product

import com.example.watsapporder.data.mappers.ProductRequest
import com.example.watsapporder.data.repositoyImpl.product.ProductResults
import com.example.watsapporder.domain.repository.product.ProductsRepository

class ProductsUseCases(private val productsRepository: ProductsRepository) {
    suspend fun getProducts(): ProductResults = productsRepository.getProducts()

    suspend fun getProduct(id: Int): ProductResults = productsRepository.getProduct(id)

    suspend fun saveProduct(request: ProductRequest): ProductResults =
        productsRepository.saveProduct(request)

    suspend fun updateProduct(id: Int, request: ProductRequest): ProductResults =
        productsRepository.updateProduct(id, request)

    suspend fun deleteProduct(id: Int): ProductResults =
        productsRepository.deleteProduct(id)
}
