package com.example.watsapporder.data.repositoyImpl.input

import com.example.watsapporder.data.local.SessionStore
import com.example.watsapporder.data.mappers.InputCreateRequest
import com.example.watsapporder.data.remote.input.InputsServices
import com.example.watsapporder.domain.repository.input.InputsRepository

class InputsRepositoryImpl(
    private val services: InputsServices,
    private val sessionStore: SessionStore,
) : InputsRepository {

    override suspend fun getInputs(): InputResults = try {
        InputResults.Inputs(services.getInputs(token()))
    } catch (exception: Exception) {
        InputResults.MessageError(exception.message.orEmpty())
    }

    override suspend fun createInput(request: InputCreateRequest): InputResults = try {
        InputResults.Input(services.createInput(token(), request))
    } catch (exception: Exception) {
        InputResults.MessageError(exception.message.orEmpty())
    }

    private fun token(): String = sessionStore.get()?.token.orEmpty()
}
