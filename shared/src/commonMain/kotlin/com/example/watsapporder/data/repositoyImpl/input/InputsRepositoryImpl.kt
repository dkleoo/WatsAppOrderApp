package com.example.watsapporder.data.repositoyImpl.input

import com.example.watsapporder.data.mappers.InputCreateRequest
import com.example.watsapporder.data.remote.input.InputsServices
import com.example.watsapporder.domain.repository.input.InputsRepository

class InputsRepositoryImpl(private val services: InputsServices) : InputsRepository {

    override suspend fun getInputs(): InputResults = try {
        InputResults.Inputs(services.getInputs())
    } catch (exception: Exception) {
        InputResults.MessageError(exception.message.orEmpty())
    }

    override suspend fun createInput(request: InputCreateRequest): InputResults = try {
        InputResults.Input(services.createInput(request))
    } catch (exception: Exception) {
        InputResults.MessageError(exception.message.orEmpty())
    }
}
