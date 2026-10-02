package com.example.watsapporder.data.params

import com.example.watsapporder.data.mappers.CreateStepForm
import com.example.watsapporder.data.mappers.InputResponse

data class StepEditorParams(
    val index: Int,
    val step: CreateStepForm,
    val availableInputs: List<InputResponse>,
    val stepNameError: String?,
    val inputsError: String?,
    val onNameChange: (String) -> Unit,
    val onRemove: () -> Unit,
    val onToggleInput: (Int) -> Unit,
)
