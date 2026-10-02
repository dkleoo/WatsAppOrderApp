package com.example.watsapporder.domain.useCase.input

import com.example.watsapporder.data.mappers.InputCreateRequest
import com.example.watsapporder.data.repositoyImpl.input.InputResults
import com.example.watsapporder.domain.repository.input.InputsRepository

class InputsUseCases(private val inputsRepository: InputsRepository) {
    suspend fun createInput(request: InputCreateRequest): InputResults =
        inputsRepository.createInput(request)
}
