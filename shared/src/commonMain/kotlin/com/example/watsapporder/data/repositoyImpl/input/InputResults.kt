package com.example.watsapporder.data.repositoyImpl.input

import com.example.watsapporder.data.mappers.InputResponse

sealed class InputResults {
    data class Inputs(val items: List<InputResponse>) : InputResults()

    data class Input(val item: InputResponse) : InputResults()

    data class MessageError(val message: String) : InputResults()
}
