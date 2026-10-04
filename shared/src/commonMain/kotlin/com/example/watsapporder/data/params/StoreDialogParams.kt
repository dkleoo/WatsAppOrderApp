package com.example.watsapporder.data.params

data class StoreDialogParams(
    val welcomeMessage: String,
    val address: String,
    val phone: String,
    val whatsappBusinessPhone: String,
    val idWhatsApp: String,
    val addressError: String?,
    val phoneError: String?,
    val whatsappError: String?,
    val idWhatsAppError: String?,
    val errorMessage: String?,
    val isSaving: Boolean,
    val onWelcomeMessageChange: (String) -> Unit,
    val onAddressChange: (String) -> Unit,
    val onPhoneChange: (String) -> Unit,
    val onWhatsappChange: (String) -> Unit,
    val onIdWhatsAppChange: (String) -> Unit,
    val onSave: () -> Unit,
    val onDismiss: () -> Unit,
)
