package com.example.watsapporder.data.repositoyImpl.product

import com.example.watsapporder.data.mappers.ProductRequest
import com.example.watsapporder.data.remote.product.ProductsServices
import com.example.watsapporder.domain.repository.product.ProductsRepository

class ProductsRepositoryImpl(private val services: ProductsServices) : ProductsRepository {

    override suspend fun getProducts(): ProductResults = try {
        ProductResults.Products(services.getProducts())
    } catch (exception: Exception) {
        ProductResults.MessageError(exception.message.orEmpty())
    }

    override suspend fun getProduct(id: Int): ProductResults = try {
        ProductResults.Product(services.getProduct(id))
    } catch (exception: Exception) {
        ProductResults.MessageError(exception.message.orEmpty())
    }

    override suspend fun saveProduct(request: ProductRequest): ProductResults = try {
        ProductResults.Product(services.saveProduct(request))
    } catch (exception: Exception) {
        ProductResults.MessageError(exception.message.orEmpty())
    }

    override suspend fun updateProduct(id: Int, request: ProductRequest): ProductResults = try {
        ProductResults.Product(services.updateProduct(id, request))
    } catch (exception: Exception) {
        ProductResults.MessageError(exception.message.orEmpty())
    }

    override suspend fun deleteProduct(id: Int): ProductResults = try {
        services.deleteProduct(id)
        ProductResults.Deleted(id)
    } catch (exception: Exception) {
        ProductResults.MessageError(exception.message.orEmpty())
    }
}
