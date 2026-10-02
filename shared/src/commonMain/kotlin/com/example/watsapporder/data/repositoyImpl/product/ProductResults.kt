package com.example.watsapporder.data.repositoyImpl.product

import com.example.watsapporder.data.mappers.ProductResponse

sealed class ProductResults {
    data class Products(val items: List<ProductResponse>) : ProductResults()

    data class Product(val item: ProductResponse) : ProductResults()

    data class Deleted(val id: Int) : ProductResults()

    data class MessageError(val message: String) : ProductResults()
}
