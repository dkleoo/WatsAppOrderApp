package com.example.watsapporder.presentation.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.watsapporder.data.params.PasswordTextFieldParams
import com.example.watsapporder.presentation.theme.ColorApp
import com.example.watsapporder.presentation.theme.plazaOnTextStyle
import org.jetbrains.compose.resources.painterResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.ic_eye
import watsapporder.shared.generated.resources.ic_eye_off
import watsapporder.shared.generated.resources.ic_lock

@Composable
fun PasswordTextField(params: PasswordTextFieldParams) {
    Column(modifier = params.modifier) {
        OutlinedTextField(
            value = params.value,
            onValueChange = params.onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = params.isError,
            placeholder = {
                Text(
                    text = params.placeholder,
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.bodyMedium,
                        color = ColorApp.hintText,
                    ),
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_lock),
                    contentDescription = null,
                    tint = ColorApp.textGray,
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(
                        if (params.isPasswordVisible) Res.drawable.ic_eye_off else Res.drawable.ic_eye,
                    ),
                    contentDescription = null,
                    modifier = Modifier.clickable { params.onToggleVisibility() },
                    tint = ColorApp.textGray,
                )
            },
            visualTransformation = if (params.isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            shape = RoundedCornerShape(12.dp),
            textStyle = plazaOnTextStyle(
                base = MaterialTheme.typography.bodyMedium,
                color = ColorApp.textColor,
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ColorApp.primary,
                unfocusedBorderColor = ColorApp.cardBackGroundGray,
                errorBorderColor = ColorApp.errorColor,
                cursorColor = ColorApp.primary,
                focusedContainerColor = ColorApp.cardBackGroundGray,
                unfocusedContainerColor = ColorApp.cardBackGroundGray,
                errorContainerColor = ColorApp.cardBackGroundGray,
                focusedTextColor = ColorApp.textColor,
                unfocusedTextColor = ColorApp.textColor,
            ),
        )
        if (params.isError && params.errorMessage != null) {
            Text(
                text = params.errorMessage,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.bodySmall,
                    color = ColorApp.errorColor,
                ),
            )
        }
    }
}
