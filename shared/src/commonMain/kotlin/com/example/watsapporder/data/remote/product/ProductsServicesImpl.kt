package com.example.watsapporder.data.remote.product

import com.example.watsapporder.data.mappers.ProductRequest
import com.example.watsapporder.data.mappers.ProductResponse
import com.example.watsapporder.data.remote.errorMessage
import com.example.watsapporder.domain.util.EndPoints
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class ProductsServicesImpl(private val httpClient: HttpClient) : ProductsServices {

    override suspend fun getProducts(): List<ProductResponse> {
        val response = httpClient.get(EndPoints.BASE_URL + EndPoints.PRODUCTS)
        return response.bodyOrThrow()
    }

    override suspend fun getProduct(id: Int): ProductResponse {
        val response = httpClient.get(EndPoints.BASE_URL + EndPoints.PRODUCTS + "/$id")
        return response.bodyOrThrow()
    }

    override suspend fun saveProduct(request: ProductRequest): ProductResponse {
        val response = httpClient.post(EndPoints.BASE_URL + EndPoints.PRODUCTS) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.bodyOrThrow()
    }

    override suspend fun updateProduct(id: Int, request: ProductRequest): ProductResponse {
        val response = httpClient.put(EndPoints.BASE_URL + EndPoints.PRODUCTS + "/$id") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.bodyOrThrow()
    }

    override suspend fun deleteProduct(id: Int) {
        val response = httpClient.delete(EndPoints.BASE_URL + EndPoints.PRODUCTS + "/$id")
        if (!response.status.isSuccess()) {
            throw IllegalStateException(response.errorMessage())
        }
    }

    private suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T {
        if (status.isSuccess()) return body()
        throw IllegalStateException(errorMessage())
    }
}
