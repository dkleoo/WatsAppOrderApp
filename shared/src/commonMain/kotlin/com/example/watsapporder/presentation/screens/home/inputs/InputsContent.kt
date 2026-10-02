package com.example.watsapporder.presentation.screens.home.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.watsapporder.data.mappers.InputResponse
import com.example.watsapporder.data.params.AppTextFieldParams
import com.example.watsapporder.data.params.InputCardParams
import com.example.watsapporder.presentation.screens.components.AppTextField
import com.example.watsapporder.presentation.screens.components.ButtonContainerGreen
import com.example.watsapporder.presentation.theme.ColorApp
import com.example.watsapporder.presentation.theme.plazaOnTextStyle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.ic_add
import watsapporder.shared.generated.resources.ic_inventory
import watsapporder.shared.generated.resources.ic_money
import watsapporder.shared.generated.resources.inputs_bank_subtitle
import watsapporder.shared.generated.resources.inputs_bank_title
import watsapporder.shared.generated.resources.inputs_category_extra
import watsapporder.shared.generated.resources.inputs_category_main
import watsapporder.shared.generated.resources.inputs_category_other
import watsapporder.shared.generated.resources.inputs_category_protein
import watsapporder.shared.generated.resources.inputs_category_topping
import watsapporder.shared.generated.resources.inputs_created
import watsapporder.shared.generated.resources.inputs_empty
import watsapporder.shared.generated.resources.inputs_extra_price
import watsapporder.shared.generated.resources.inputs_form_title
import watsapporder.shared.generated.resources.inputs_name_placeholder
import watsapporder.shared.generated.resources.inputs_no_price
import watsapporder.shared.generated.resources.inputs_price_placeholder
import watsapporder.shared.generated.resources.inputs_save
import watsapporder.shared.generated.resources.inputs_saved_title

@Composable
fun InputsContent(
    state: InputsScreenState,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onSave: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        BankInfoCard()
        Spacer(Modifier.height(16.dp))
        InputFormCard(
            state = state,
            onNameChange = onNameChange,
            onPriceChange = onPriceChange,
            onSave = onSave,
        )
        Spacer(Modifier.height(16.dp))
        SavedInputsCard(inputs = state.inputs, isLoading = state.isLoading)
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
        if (state.successMessage != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.inputs_created, state.successMessage),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.bodySmall,
                    color = ColorApp.primaryDark,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}

@Composable
private fun SavedInputsCard(inputs: List<InputResponse>, isLoading: Boolean) {

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = stringResource(Res.string.inputs_saved_title).uppercase(),
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.labelLarge,
                color = ColorApp.textGray,
                fontWeight = FontWeight.Bold,
            ),
        )
        Spacer(Modifier.height(12.dp))
        when {
            isLoading && inputs.isEmpty() -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = ColorApp.primary)
            }

            inputs.isEmpty() -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.inputs_empty),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.bodyMedium,
                        color = ColorApp.textGray,
                    ),
                )
            }

            else -> LazyColumn(
                modifier = Modifier.heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(inputs, key = { it.id }) { input ->
                    InputCard(input = input)
                }
            }
        }
    }
}

@Composable
private fun InputCard(input: InputResponse) {
    val category = inputCategory(input.name)
    val hasExtraPrice = input.price > 0
    val cardShape = RoundedCornerShape(16.dp)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ColorApp.stone200, cardShape),
        shape = cardShape,
        color = ColorApp.white,
        shadowElevation = 2.dp,
    ) {
        InputCardContent(
            InputCardParams(
                input = input,
                noPriceText = stringResource(Res.string.inputs_no_price),
                extraPriceText = stringResource(Res.string.inputs_extra_price, input.price),
                extraPriceAmount = if (hasExtraPrice) input.price.toString() else null,
                categoryText = category.label,
                categoryBackground = category.background,
                categoryContent = category.content,
            ),
        )
    }
}

@Composable
private fun InputCardContent(params: InputCardParams) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(ColorApp.amberGold),
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = params.input.name,
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.titleSmall,
                    color = ColorApp.textColor,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Text(
                text = params.extraPriceText.takeIf { params.extraPriceAmount != null } ?: params.noPriceText,
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.labelMedium,
                    color = if (params.extraPriceAmount != null) ColorApp.amberGoldDark else ColorApp.textGray,
                    fontWeight = if (params.extraPriceAmount != null) FontWeight.Bold else FontWeight.Normal,
                ),
            )
        }
        Spacer(Modifier.width(12.dp))
        CategoryBadge(
            text = params.categoryText,
            background = params.categoryBackground,
            content = params.categoryContent,
        )
    }
}

@Composable
private fun CategoryBadge(text: String, background: Color, content: Color) {
    Text(
        text = text,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        style = plazaOnTextStyle(
            base = MaterialTheme.typography.labelMedium,
            color = content,
            fontWeight = FontWeight.Bold,
        ),
    )
}

@Composable
private fun BankInfoCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ColorApp.amberSoft)
            .border(1.dp, ColorApp.amberSoftBorder, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_inventory),
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = ColorApp.amberGoldDark,
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                text = stringResource(Res.string.inputs_bank_title),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.titleSmall,
                    color = ColorApp.textColor,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = stringResource(Res.string.inputs_bank_subtitle),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.bodySmall,
                    color = ColorApp.textGray,
                ),
            )
        }
    }
}

@Composable
private fun InputFormCard(
    state: InputsScreenState,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onSave: () -> Unit,
) {
    val cardShape = RoundedCornerShape(16.dp)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ColorApp.stone200, cardShape),
        shape = cardShape,
        color = ColorApp.white,
        shadowElevation = 4.dp,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(ColorApp.amberGold),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = ColorApp.white,
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(Res.string.inputs_form_title).uppercase(),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.titleSmall,
                        color = ColorApp.textColor,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
            Spacer(Modifier.height(16.dp))
            AppTextField(
                AppTextFieldParams(
                    value = state.name,
                    onValueChange = onNameChange,
                    placeholder = stringResource(Res.string.inputs_name_placeholder),
                    leadingIcon = painterResource(Res.drawable.ic_inventory),
                    keyboardType = KeyboardType.Text,
                    isError = state.nameError != null,
                    errorMessage = state.nameError?.let { stringResource(it) },
                ),
            )
            Spacer(Modifier.height(12.dp))
            AppTextField(
                AppTextFieldParams(
                    value = state.price,
                    onValueChange = onPriceChange,
                    placeholder = stringResource(Res.string.inputs_price_placeholder),
                    leadingIcon = painterResource(Res.drawable.ic_money),
                    keyboardType = KeyboardType.Decimal,
                    isError = state.priceError != null,
                    errorMessage = state.priceError?.let { stringResource(it) },
                ),
            )
            Spacer(Modifier.height(16.dp))
            ButtonContainerGreen(
                onClick = onSave,
                text = stringResource(Res.string.inputs_save),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                isLoading = state.isSaving,
            )
        }
    }
}

private data class InputCategory(
    val label: String,
    val background: Color,
    val content: Color,
)

@Composable
private fun inputCategory(name: String): InputCategory {
    val normalized = name.lowercase()
    return when {
        PROTEIN_KEYWORDS.any { normalized.contains(it) } -> InputCategory(
            label = stringResource(Res.string.inputs_category_protein),
            background = ColorApp.googleRedSoft,
            content = ColorApp.googleRedDark,
        )

        SOUP_KEYWORDS.any { normalized.contains(it) } -> InputCategory(
            label = stringResource(Res.string.inputs_category_main),
            background = ColorApp.amberSoft,
            content = ColorApp.amberGoldDark,
        )

        TOPPING_KEYWORDS.any { normalized.contains(it) } -> InputCategory(
            label = stringResource(Res.string.inputs_category_topping),
            background = ColorApp.amberSoft,
            content = ColorApp.amberGoldDark,
        )

        EXTRA_KEYWORDS.any { normalized.contains(it) } -> InputCategory(
            label = stringResource(Res.string.inputs_category_extra),
            background = ColorApp.categoryNeutralSoft,
            content = ColorApp.categoryNeutralText,
        )

        else -> InputCategory(
            label = stringResource(Res.string.inputs_category_other),
            background = ColorApp.stone100,
            content = ColorApp.stone500,
        )
    }
}

private val PROTEIN_KEYWORDS = listOf("carne", "pollo", "res", "cerdo", "pescado", "pechuga", "parrilla")
private val SOUP_KEYWORDS = listOf("sopa", "crema", "caldo")
private val TOPPING_KEYWORDS = listOf("queso", "salsa", "tocino", "verdura", "lechuga", "tomate")
private val EXTRA_KEYWORDS = listOf("extra", "adicional")
