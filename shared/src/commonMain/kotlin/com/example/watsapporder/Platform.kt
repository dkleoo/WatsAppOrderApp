package com.example.watsapporder

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform