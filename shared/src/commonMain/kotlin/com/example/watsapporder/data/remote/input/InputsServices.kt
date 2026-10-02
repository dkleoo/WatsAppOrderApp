package com.example.watsapporder.data.remote.input

import com.example.watsapporder.data.mappers.InputCreateRequest
import com.example.watsapporder.data.mappers.InputResponse

interface InputsServices {
    suspend fun getInputs(): List<InputResponse>
    suspend fun createInput(request: InputCreateRequest): InputResponse
}
