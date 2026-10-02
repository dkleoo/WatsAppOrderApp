package com.example.watsapporder.presentation.screens.home.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.watsapporder.data.mappers.CreateStepForm
import com.example.watsapporder.data.mappers.InputResponse
import com.example.watsapporder.data.mappers.ProductType
import com.example.watsapporder.data.params.AppTextFieldParams
import com.example.watsapporder.data.params.StepEditorParams
import com.example.watsapporder.presentation.screens.components.AppTextField
import com.example.watsapporder.presentation.screens.components.ButtonContainerGreen
import com.example.watsapporder.presentation.screens.components.ButtonTransparentCustom
import com.example.watsapporder.presentation.theme.ColorApp
import com.example.watsapporder.presentation.theme.plazaOnTextStyle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.create_add_step
import watsapporder.shared.generated.resources.create_error_step_inputs
import watsapporder.shared.generated.resources.create_error_step_name
import watsapporder.shared.generated.resources.create_ingredients_title
import watsapporder.shared.generated.resources.create_input_extra_price
import watsapporder.shared.generated.resources.create_modal_title
import watsapporder.shared.generated.resources.create_name_label
import watsapporder.shared.generated.resources.create_name_placeholder
import watsapporder.shared.generated.resources.create_no_inputs
import watsapporder.shared.generated.resources.create_prepare_description
import watsapporder.shared.generated.resources.create_prepare_title
import watsapporder.shared.generated.resources.create_price_label
import watsapporder.shared.generated.resources.create_price_placeholder
import watsapporder.shared.generated.resources.create_product_created
import watsapporder.shared.generated.resources.create_ready_description
import watsapporder.shared.generated.resources.create_ready_title
import watsapporder.shared.generated.resources.create_save
import watsapporder.shared.generated.resources.create_step_default_name
import watsapporder.shared.generated.resources.create_step_name_placeholder
import watsapporder.shared.generated.resources.create_steps_hint
import watsapporder.shared.generated.resources.create_steps_title
import watsapporder.shared.generated.resources.ic_add
import watsapporder.shared.generated.resources.ic_check
import watsapporder.shared.generated.resources.ic_delete
import watsapporder.shared.generated.resources.ic_kitchen
import watsapporder.shared.generated.resources.ic_layers
import watsapporder.shared.generated.resources.ic_menu_book
import watsapporder.shared.generated.resources.ic_money

@Composable
fun CreateProductContent(
    state: CreateProductScreenState,
    onSelectType: (ProductType) -> Unit,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onAddStep: () -> Unit,
    onRemoveStep: (Int) -> Unit,
    onStepNameChange: (Int, String) -> Unit,
    onToggleInput: (Int, Int) -> Unit,
    onSave: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(Res.string.create_modal_title).uppercase(),
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.labelSmall,
                color = ColorApp.textGray,
                fontWeight = FontWeight.Bold,
            ),
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ProductTypeCard(
                type = ProductType.CREATED,
                selected = state.form.type == ProductType.CREATED,
                title = stringResource(Res.string.create_ready_title),
                description = stringResource(Res.string.create_ready_description),
                icon = painterResource(Res.drawable.ic_menu_book),
                onSelect = onSelectType,
                modifier = Modifier.weight(1f),
            )
            ProductTypeCard(
                type = ProductType.WITH_INPUTS,
                selected = state.form.type == ProductType.WITH_INPUTS,
                title = stringResource(Res.string.create_prepare_title),
                description = stringResource(Res.string.create_prepare_description),
                icon = painterResource(Res.drawable.ic_kitchen),
                onSelect = onSelectType,
                modifier = Modifier.weight(1f),
            )
        }
        if (state.typeError != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(state.typeError),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.bodySmall,
                    color = ColorApp.errorColor,
                ),
            )
        }
        Spacer(Modifier.height(16.dp))
        ProductDataCard(
            name = state.form.name,
            price = state.form.price,
            nameError = state.nameError,
            priceError = state.priceError,
            onNameChange = onNameChange,
            onPriceChange = onPriceChange,
        )

        if (state.form.type == ProductType.WITH_INPUTS) {
            Spacer(Modifier.height(16.dp))
            StepsCard(
                state = state,
                onAddStep = onAddStep,
                onRemoveStep = onRemoveStep,
                onStepNameChange = onStepNameChange,
                onToggleInput = onToggleInput,
            )
        }

        Spacer(Modifier.height(16.dp))
        ButtonContainerGreen(
            onClick = onSave,
            text = stringResource(Res.string.create_save),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            isLoading = state.isSaving,
        )

        if (state.createdProductName != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.create_product_created, state.createdProductName),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.bodySmall,
                    color = ColorApp.primaryDark,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }

        if (state.errorMessage != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = state.errorMessage,
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.bodySmall,
                    color = ColorApp.errorColor,
                ),
            )
        }
    }
}

@Composable
private fun ProductTypeCard(
    type: ProductType,
    selected: Boolean,
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.painter.Painter,
    onSelect: (ProductType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(16.dp)
    val borderColor = if (selected) ColorApp.primary else ColorApp.stone200

    Surface(
        modifier = modifier
            .border(if (selected) 2.dp else 1.dp, borderColor, cardShape)
            .clip(cardShape)
            .clickable { onSelect(type) },
        shape = cardShape,
        color = ColorApp.white,
        shadowElevation = if (selected) 4.dp else 1.dp,
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selected) ColorApp.primary else ColorApp.stone100),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (selected) ColorApp.white else ColorApp.textGray,
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = title,
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.titleSmall,
                        color = ColorApp.textColor,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = description,
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.labelSmall,
                    color = ColorApp.textGray,
                ),
            )
        }
    }
}

@Composable
private fun ProductDataCard(
    name: String,
    price: String,
    nameError: org.jetbrains.compose.resources.StringResource?,
    priceError: org.jetbrains.compose.resources.StringResource?,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
) {
    val cardShape = RoundedCornerShape(16.dp)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ColorApp.stone200, cardShape),
        shape = cardShape,
        color = ColorApp.white,
        shadowElevation = 2.dp,
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Column(modifier = Modifier.weight(2f)) {
                    FieldLabel(stringResource(Res.string.create_name_label))
                    Spacer(Modifier.height(6.dp))
                    AppTextField(
                        AppTextFieldParams(
                            value = name,
                            onValueChange = onNameChange,
                            placeholder = stringResource(Res.string.create_name_placeholder),
                            leadingIcon = painterResource(Res.drawable.ic_menu_book),
                            keyboardType = KeyboardType.Text,
                            isError = nameError != null,
                            errorMessage = nameError?.let { stringResource(it) },
                        ),
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    FieldLabel(stringResource(Res.string.create_price_label))
                    Spacer(Modifier.height(6.dp))
                    AppTextField(
                        AppTextFieldParams(
                            value = price,
                            onValueChange = onPriceChange,
                            placeholder = stringResource(Res.string.create_price_placeholder),
                            leadingIcon = painterResource(Res.drawable.ic_money),
                            keyboardType = KeyboardType.Decimal,
                            isError = priceError != null,
                            errorMessage = priceError?.let { stringResource(it) },
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = plazaOnTextStyle(
            base = MaterialTheme.typography.labelSmall,
            color = ColorApp.textGray,
            fontWeight = FontWeight.Bold,
        ),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StepsCard(
    state: CreateProductScreenState,
    onAddStep: () -> Unit,
    onRemoveStep: (Int) -> Unit,
    onStepNameChange: (Int, String) -> Unit,
    onToggleInput: (Int, Int) -> Unit,
) {
    val cardShape = RoundedCornerShape(16.dp)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ColorApp.stone200, cardShape),
        shape = cardShape,
        color = ColorApp.white,
        shadowElevation = 2.dp,
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Res.drawable.ic_layers),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = ColorApp.amberGoldDark,
                )
                Spacer(Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(Res.string.create_steps_title).uppercase(),
                        style = plazaOnTextStyle(
                            base = MaterialTheme.typography.labelSmall,
                            color = ColorApp.textColor,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                    Text(
                        text = stringResource(Res.string.create_steps_hint),
                        style = plazaOnTextStyle(
                            base = MaterialTheme.typography.labelSmall,
                            color = ColorApp.textGray,
                        ),
                    )
                }
                ButtonTransparentCustom(
                    onClick = onAddStep,
                    text = stringResource(Res.string.create_add_step),
                    modifier = Modifier.height(38.dp),
                    painter = painterResource(Res.drawable.ic_add),
                )
            }
            if (state.stepsError != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(state.stepsError),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.bodySmall,
                        color = ColorApp.errorColor,
                    ),
                )
            }
            Spacer(Modifier.height(12.dp))
            if (state.isLoadingInputs) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = ColorApp.primary)
                }
            } else {
                state.form.steps.forEachIndexed { index, step ->
                    StepEditor(
                        StepEditorParams(
                            index = index,
                            step = step,
                            availableInputs = state.availableInputs,
                            stepNameError = state.stepErrors[index]?.let { stringResource(it) },
                            inputsError = state.inputErrors[index]?.let { stringResource(it) },
                            onNameChange = { value -> onStepNameChange(index, value) },
                            onRemove = { onRemoveStep(index) },
                            onToggleInput = { inputId -> onToggleInput(index, inputId) },
                        ),
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StepEditor(params: StepEditorParams) {
    val boxShape = RoundedCornerShape(14.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(boxShape)
            .background(ColorApp.cardBackGroundGray)
            .padding(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(ColorApp.amberGold)
                    .padding(0.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = (params.index + 1).toString(),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.labelSmall,
                        color = ColorApp.white,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
            Spacer(Modifier.width(10.dp))
            Box(modifier = Modifier.weight(1f)) {
                AppTextField(
                    AppTextFieldParams(
                        value = params.step.name,
                        onValueChange = params.onNameChange,
                        placeholder = stringResource(Res.string.create_step_default_name),
                        leadingIcon = painterResource(Res.drawable.ic_layers),
                        keyboardType = KeyboardType.Text,
                        isError = params.stepNameError != null,
                        errorMessage = params.stepNameError,
                        modifier = Modifier.fillMaxWidth(),
                    ),
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ColorApp.googleRedSoft)
                    .clickable { params.onRemove() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = ColorApp.googleRedDark,
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(Res.string.create_ingredients_title),
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.labelSmall,
                color = ColorApp.textGray,
                fontWeight = FontWeight.Bold,
            ),
        )
        Spacer(Modifier.height(8.dp))
        if (params.availableInputs.isEmpty()) {
            Text(
                text = stringResource(Res.string.create_no_inputs),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.bodySmall,
                    color = ColorApp.textGray,
                ),
            )
        } else {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                params.availableInputs.forEach { input ->
                    IngredientChip(
                        input = input,
                        selected = params.step.selectedInputIds.contains(input.id),
                        onClick = { params.onToggleInput(input.id) },
                    )
                }
            }
        }
        if (params.inputsError != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = params.inputsError,
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.bodySmall,
                    color = ColorApp.errorColor,
                ),
            )
        }
    }
}

@Composable
private fun IngredientChip(
    input: InputResponse,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val containerColor = if (selected) ColorApp.amberGold else ColorApp.white
    val contentColor = if (selected) ColorApp.white else ColorApp.textColor
    val borderColor = if (selected) ColorApp.amberGold else ColorApp.stone300

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(1.dp, borderColor, RoundedCornerShape(50))
            .background(containerColor)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (selected) {
            Icon(
                painter = painterResource(Res.drawable.ic_check),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = contentColor,
            )
            Spacer(Modifier.width(4.dp))
        }
        Text(
            text = input.name,
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.labelSmall,
                color = contentColor,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        if (input.price > 0) {
            Text(
                text = stringResource(Res.string.create_input_extra_price, input.price),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.labelSmall,
                    color = if (selected) ColorApp.white else ColorApp.amberGoldDark,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}
