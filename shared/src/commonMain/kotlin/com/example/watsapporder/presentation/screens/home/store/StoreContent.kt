package com.example.watsapporder.presentation.screens.home.store

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.watsapporder.data.params.AppTextFieldParams
import com.example.watsapporder.data.params.StoreDialogParams
import com.example.watsapporder.presentation.screens.components.AppTextField
import com.example.watsapporder.presentation.screens.components.ButtonContainerGreen
import com.example.watsapporder.presentation.screens.components.ButtonTransparentCustom
import com.example.watsapporder.presentation.theme.ColorApp
import com.example.watsapporder.presentation.theme.plazaOnTextStyle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.ic_close
import watsapporder.shared.generated.resources.ic_edit
import watsapporder.shared.generated.resources.ic_key
import watsapporder.shared.generated.resources.ic_phone
import watsapporder.shared.generated.resources.ic_receipt
import watsapporder.shared.generated.resources.store_address_placeholder
import watsapporder.shared.generated.resources.store_close
import watsapporder.shared.generated.resources.store_form_subtitle
import watsapporder.shared.generated.resources.store_form_title
import watsapporder.shared.generated.resources.store_id_whatsapp_placeholder
import watsapporder.shared.generated.resources.store_phone_placeholder
import watsapporder.shared.generated.resources.store_save
import watsapporder.shared.generated.resources.store_welcome_placeholder
import watsapporder.shared.generated.resources.store_whatsapp_placeholder

@Composable
fun StoreEditorDialog(params: StoreDialogParams) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorApp.scrim)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {},
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .padding(20.dp)
                .widthIn(max = 420.dp),
            shape = RoundedCornerShape(24.dp),
            color = ColorApp.white,
            shadowElevation = 24.dp,
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(ColorApp.amberGold),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_edit),
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = ColorApp.white,
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.store_form_title),
                            style = plazaOnTextStyle(
                                base = MaterialTheme.typography.titleMedium,
                                color = ColorApp.textColor,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                        Text(
                            text = stringResource(Res.string.store_form_subtitle),
                            style = plazaOnTextStyle(
                                base = MaterialTheme.typography.bodySmall,
                                color = ColorApp.textGray,
                            ),
                        )
                    }
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = stringResource(Res.string.store_close),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { params.onDismiss() },
                        tint = ColorApp.textGray,
                    )
                }
                Spacer(Modifier.height(20.dp))
                AppTextField(
                    AppTextFieldParams(
                        value = params.welcomeMessage,
                        onValueChange = params.onWelcomeMessageChange,
                        placeholder = stringResource(Res.string.store_welcome_placeholder),
                        leadingIcon = painterResource(Res.drawable.ic_receipt),
                        keyboardType = KeyboardType.Text,
                    ),
                )
                Spacer(Modifier.height(12.dp))
                AppTextField(
                    AppTextFieldParams(
                        value = params.address,
                        onValueChange = params.onAddressChange,
                        placeholder = stringResource(Res.string.store_address_placeholder),
                        leadingIcon = painterResource(Res.drawable.ic_edit),
                        keyboardType = KeyboardType.Text,
                        isError = params.addressError != null,
                        errorMessage = params.addressError,
                    ),
                )
                Spacer(Modifier.height(12.dp))
                AppTextField(
                    AppTextFieldParams(
                        value = params.phone,
                        onValueChange = params.onPhoneChange,
                        placeholder = stringResource(Res.string.store_phone_placeholder),
                        leadingIcon = painterResource(Res.drawable.ic_phone),
                        keyboardType = KeyboardType.Phone,
                        isError = params.phoneError != null,
                        errorMessage = params.phoneError,
                    ),
                )
                Spacer(Modifier.height(12.dp))
                AppTextField(
                    AppTextFieldParams(
                        value = params.whatsappBusinessPhone,
                        onValueChange = params.onWhatsappChange,
                        placeholder = stringResource(Res.string.store_whatsapp_placeholder),
                        leadingIcon = painterResource(Res.drawable.ic_phone),
                        keyboardType = KeyboardType.Phone,
                        isError = params.whatsappError != null,
                        errorMessage = params.whatsappError,
                    ),
                )
                Spacer(Modifier.height(12.dp))
                AppTextField(
                    AppTextFieldParams(
                        value = params.idWhatsApp,
                        onValueChange = params.onIdWhatsAppChange,
                        placeholder = stringResource(Res.string.store_id_whatsapp_placeholder),
                        leadingIcon = painterResource(Res.drawable.ic_key),
                        keyboardType = KeyboardType.Number,
                        isError = params.idWhatsAppError != null,
                        errorMessage = params.idWhatsAppError,
                    ),
                )

                if (params.errorMessage != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = params.errorMessage,
                        style = plazaOnTextStyle(
                            base = MaterialTheme.typography.bodySmall,
                            color = ColorApp.errorColor,
                        ),
                    )
                }

                Spacer(Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ButtonTransparentCustom(
                        onClick = params.onDismiss,
                        text = stringResource(Res.string.store_close),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        enabled = !params.isSaving,
                    )
                    ButtonContainerGreen(
                        onClick = params.onSave,
                        text = stringResource(Res.string.store_save),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        isLoading = params.isSaving,
                    )
                }
            }
        }
    }
}
