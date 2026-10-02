package com.example.watsapporder.presentation.screens.home.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.watsapporder.data.params.AppTextFieldParams
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
import watsapporder.shared.generated.resources.inputs_created
import watsapporder.shared.generated.resources.inputs_form_title
import watsapporder.shared.generated.resources.inputs_name_placeholder
import watsapporder.shared.generated.resources.inputs_price_placeholder
import watsapporder.shared.generated.resources.inputs_save

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
