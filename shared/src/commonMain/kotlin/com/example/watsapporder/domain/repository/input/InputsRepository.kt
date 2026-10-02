package com.example.watsapporder.domain.repository.input

import com.example.watsapporder.data.mappers.InputCreateRequest
import com.example.watsapporder.data.repositoyImpl.input.InputResults

interface InputsRepository {
    suspend fun createInput(request: InputCreateRequest): InputResults
}
