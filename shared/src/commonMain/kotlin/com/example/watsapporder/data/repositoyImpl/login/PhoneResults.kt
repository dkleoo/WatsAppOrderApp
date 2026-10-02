package com.example.watsapporder.data.repositoyImpl.login

import com.example.watsapporder.data.mappers.LoggedUser

sealed class PhoneResults {
    data class CodeSent(val verificationId: String) : PhoneResults()

    data class Success(val user: LoggedUser) : PhoneResults()

    data class MessageError(val message: String) : PhoneResults()
}
