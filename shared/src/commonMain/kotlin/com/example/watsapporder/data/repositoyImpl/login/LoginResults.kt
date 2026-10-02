package com.example.watsapporder.data.repositoyImpl.login

import com.example.watsapporder.data.mappers.LoggedUser

sealed class LoginResults {
    data class Success(val user: LoggedUser) : LoginResults()

    data class MessageError(val message: String) : LoginResults()
}
