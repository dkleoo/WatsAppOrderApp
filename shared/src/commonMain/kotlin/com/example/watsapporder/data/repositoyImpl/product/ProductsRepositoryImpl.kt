package com.example.watsapporder.data.repositoyImpl.product

import com.example.watsapporder.data.local.SessionStore
import com.example.watsapporder.data.mappers.ProductRequest
import com.example.watsapporder.data.remote.product.ProductsServices
import com.example.watsapporder.domain.repository.product.ProductsRepository

class ProductsRepositoryImpl(
    private val services: ProductsServices,
    private val sessionStore: SessionStore,
) : ProductsRepository {

    override suspend fun getProducts(): ProductResults = try {
        ProductResults.Products(services.getProducts(token()))
    } catch (exception: Exception) {
        ProductResults.MessageError(exception.message.orEmpty())
    }

    override suspend fun getProduct(id: Int): ProductResults = try {
        ProductResults.Product(services.getProduct(token(), id))
    } catch (exception: Exception) {
        ProductResults.MessageError(exception.message.orEmpty())
    }

    override suspend fun saveProduct(request: ProductRequest): ProductResults = try {
        ProductResults.Product(services.saveProduct(token(), request))
    } catch (exception: Exception) {
        ProductResults.MessageError(exception.message.orEmpty())
    }

    override suspend fun updateProduct(id: Int, request: ProductRequest): ProductResults = try {
        ProductResults.Product(services.updateProduct(token(), id, request))
    } catch (exception: Exception) {
        ProductResults.MessageError(exception.message.orEmpty())
    }

    override suspend fun deleteProduct(id: Int): ProductResults = try {
        services.deleteProduct(token(), id)
        ProductResults.Deleted(id)
    } catch (exception: Exception) {
        ProductResults.MessageError(exception.message.orEmpty())
    }

    private fun token(): String = sessionStore.get()?.token.orEmpty()
}
