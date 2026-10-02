package com.example.watsapporder.domain.util

fun String.isValidEmail(): Boolean =
    Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(this)

fun String.isValidPhone(): Boolean =
    Regex("^\\+?[0-9]{8,15}$").matches(this.trim())
