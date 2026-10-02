package com.example.watsapporder.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.watsapporder.data.mappers.AuthProvider
import com.example.watsapporder.data.mappers.LoggedUser
import com.example.watsapporder.data.mappers.ProductResponse
import com.example.watsapporder.data.mappers.ProductType
import com.example.watsapporder.domain.useCase.login.AuthUseCases
import com.example.watsapporder.presentation.screens.components.ButtonContainerGreen
import com.example.watsapporder.presentation.screens.components.ButtonTransparentCustom
import com.example.watsapporder.presentation.screens.home.create.CreateProductContent
import com.example.watsapporder.presentation.screens.home.create.CreateProductViewModel
import com.example.watsapporder.presentation.screens.home.inputs.InputsContent
import com.example.watsapporder.presentation.screens.home.inputs.InputsViewModel
import com.example.watsapporder.presentation.screens.login.LoginScreen
import com.example.watsapporder.presentation.theme.ColorApp
import com.example.watsapporder.presentation.theme.plazaOnTextStyle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.edit_product_close
import watsapporder.shared.generated.resources.edit_product_delete
import watsapporder.shared.generated.resources.edit_product_inputs_hint
import watsapporder.shared.generated.resources.edit_product_inputs_title
import watsapporder.shared.generated.resources.edit_product_name_label
import watsapporder.shared.generated.resources.edit_product_price_label
import watsapporder.shared.generated.resources.edit_product_save
import watsapporder.shared.generated.resources.edit_product_subtitle
import watsapporder.shared.generated.resources.edit_product_title
import watsapporder.shared.generated.resources.home_edit_mode
import watsapporder.shared.generated.resources.home_edit_product_action
import watsapporder.shared.generated.resources.home_empty
import watsapporder.shared.generated.resources.home_logout
import watsapporder.shared.generated.resources.home_menu_subtitle
import watsapporder.shared.generated.resources.home_menu_title
import watsapporder.shared.generated.resources.home_orders
import watsapporder.shared.generated.resources.home_product_ready
import watsapporder.shared.generated.resources.home_product_steps
import watsapporder.shared.generated.resources.home_sso_badge
import watsapporder.shared.generated.resources.home_tab_create
import watsapporder.shared.generated.resources.home_tab_inputs
import watsapporder.shared.generated.resources.home_tab_menu_count
import watsapporder.shared.generated.resources.ic_add
import watsapporder.shared.generated.resources.ic_bottle
import watsapporder.shared.generated.resources.ic_close
import watsapporder.shared.generated.resources.ic_edit
import watsapporder.shared.generated.resources.ic_inventory
import watsapporder.shared.generated.resources.ic_kitchen
import watsapporder.shared.generated.resources.ic_logout
import watsapporder.shared.generated.resources.ic_menu_book
import watsapporder.shared.generated.resources.ic_person
import watsapporder.shared.generated.resources.ic_receipt
import watsapporder.shared.generated.resources.input_extra_price_format
import watsapporder.shared.generated.resources.login_provider_email
import watsapporder.shared.generated.resources.login_provider_google
import watsapporder.shared.generated.resources.login_provider_phone
import watsapporder.shared.generated.resources.price_format
import kotlin.jvm.Transient

data class HomeScreen(
    @Transient val loggedUser: LoggedUser
) : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<HomeViewModel>()
        val state by viewModel.uiState.collectAsState()
        val inputsViewModel = koinScreenModel<InputsViewModel>()
        val inputsState by inputsViewModel.uiState.collectAsState()
        val createViewModel = koinScreenModel<CreateProductViewModel>()
        val createState by createViewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val authUseCases = koinInject<AuthUseCases>()

        LaunchedEffect(Unit) {
            viewModel.loadProducts()
        }

        LaunchedEffect(state.selectedTab) {
            if (state.selectedTab == HomeTab.INPUTS) {
                inputsViewModel.loadInputs()
            }
            if (state.selectedTab == HomeTab.CREATE) {
                createViewModel.loadInputs()
            }
        }

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            containerColor = ColorApp.amberGold,
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorApp.amberGold)
                    .padding(innerPadding),
            ) {
                HomeHeader(
                    loggedUser = loggedUser,
                    ordersCount = 0,
                    onLogout = {
                        authUseCases.signOut()
                        navigator.replaceAll(LoginScreen())
                    },
                )
                HomeTabs(
                    selected = state.selectedTab,
                    menuCount = state.products.size,
                    onSelect = viewModel::selectTab,
                )
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    color = ColorApp.background,
                ) {
                    when (state.selectedTab) {
                        HomeTab.MENU -> MenuContent(
                            state = state,
                            onEdit = viewModel::openEditor,
                        )

                        HomeTab.INPUTS -> InputsContent(
                            state = inputsState,
                            onNameChange = inputsViewModel::onNameChange,
                            onPriceChange = inputsViewModel::onPriceChange,
                            onSave = inputsViewModel::saveInput,
                        )

                        HomeTab.CREATE -> CreateProductContent(
                            state = createState,
                            onSelectType = createViewModel::selectType,
                            onNameChange = createViewModel::onNameChange,
                            onPriceChange = createViewModel::onPriceChange,
                            onAddStep = createViewModel::addStep,
                            onRemoveStep = createViewModel::removeStep,
                            onStepNameChange = createViewModel::onStepNameChange,
                            onToggleInput = createViewModel::toggleInput,
                            onSave = { createViewModel.saveProduct(onCreated = viewModel::loadProducts) },
                        )
                    }
                }
            }
        }

        state.editing?.let { editing ->
            EditProductDialog(
                params = EditDialogParams(
                    editing = editing,
                    isSaving = state.isSaving,
                    errorMessage = state.errorMessage,
                    priceError = state.priceError?.let { stringResource(it) },
                    onNameChange = viewModel::onNameChange,
                    onPriceChange = viewModel::onPriceChange,
                    onSave = viewModel::save,
                    onDelete = viewModel::delete,
                    onDismiss = viewModel::closeEditor,
                ),
            )
        }
    }
}

@Composable
private fun providerLabel(provider: AuthProvider): String = when (provider) {
    AuthProvider.GOOGLE -> stringResource(Res.string.login_provider_google)
    AuthProvider.EMAIL -> stringResource(Res.string.login_provider_email)
    AuthProvider.PHONE -> stringResource(Res.string.login_provider_phone)
}

@Composable
private fun HomeHeader(
    loggedUser: LoggedUser,
    ordersCount: Int,
    onLogout: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(ColorApp.whiteOverlay20),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_person),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = ColorApp.white,
            )
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = loggedUser.name,
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.titleSmall,
                    color = ColorApp.white,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Text(
                text = stringResource(Res.string.home_sso_badge, providerLabel(loggedUser.provider)),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.labelSmall,
                    color = ColorApp.whiteOverlay85,
                ),
            )
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(ColorApp.espressoDark)
                .clickable {}
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_receipt),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = ColorApp.amberGold,
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = stringResource(Res.string.home_orders),
                style = plazaOnTextStyle(
                    base = MaterialTheme.typography.labelSmall,
                    color = ColorApp.white,
                    fontWeight = FontWeight.Bold,
                ),
            )
            if (ordersCount > 0) {
                Spacer(Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(ColorApp.errorColor)
                        .padding(horizontal = 6.dp, vertical = 1.dp),
                ) {
                    Text(
                        text = ordersCount.toString(),
                        style = plazaOnTextStyle(
                            base = MaterialTheme.typography.labelSmall,
                            color = ColorApp.white,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
            }
        }
        Spacer(Modifier.width(12.dp))
        Icon(
            painter = painterResource(Res.drawable.ic_logout),
            contentDescription = stringResource(Res.string.home_logout),
            modifier = Modifier
                .size(22.dp)
                .clickable { onLogout() },
            tint = ColorApp.white,
        )
    }
}

@Composable
private fun HomeTabs(
    selected: HomeTab,
    menuCount: Int,
    onSelect: (HomeTab) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TabItem(
            tab = HomeTab.INPUTS,
            icon = painterResource(Res.drawable.ic_inventory),
            label = stringResource(Res.string.home_tab_inputs),
            selected = selected == HomeTab.INPUTS,
            onSelect = onSelect,
            modifier = Modifier.weight(1f),
        )
        TabItem(
            tab = HomeTab.CREATE,
            icon = painterResource(Res.drawable.ic_add),
            label = stringResource(Res.string.home_tab_create),
            selected = selected == HomeTab.CREATE,
            onSelect = onSelect,
            modifier = Modifier.weight(1f),
        )
        TabItem(
            tab = HomeTab.MENU,
            icon = painterResource(Res.drawable.ic_menu_book),
            label = stringResource(Res.string.home_tab_menu_count, menuCount),
            selected = selected == HomeTab.MENU,
            onSelect = onSelect,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun TabItem(
    tab: HomeTab,
    icon: Painter,
    label: String,
    selected: Boolean,
    onSelect: (HomeTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (selected) ColorApp.white else ColorApp.whiteOverlay20
    val contentColor = if (selected) ColorApp.espressoDark else ColorApp.white

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(containerColor)
            .clickable { onSelect(tab) }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = contentColor,
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = label,
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.labelSmall,
                color = contentColor,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
            ),
        )
    }
}

@Composable
private fun MenuContent(
    state: HomeScreenState,
    onEdit: (ProductResponse) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.home_menu_title),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.titleSmall,
                        color = ColorApp.textColor,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = stringResource(Res.string.home_menu_subtitle),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.bodySmall,
                        color = ColorApp.textGray,
                    ),
                )
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(ColorApp.amberGoldLight)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_edit),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = ColorApp.amberGoldDark,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(Res.string.home_edit_mode),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.labelSmall,
                        color = ColorApp.amberGoldDark,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        when {
            state.isLoading && state.products.isEmpty() -> Box(
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = ColorApp.primary)
            }

            state.products.isEmpty() -> Box(
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.home_empty),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.bodyMedium,
                        color = ColorApp.textGray,
                    ),
                )
            }

            else -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(state.products, key = { it.id }) { product ->
                    ProductCard(product = product, onEdit = onEdit)
                }
            }
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
private fun ProductCard(
    product: ProductResponse,
    onEdit: (ProductResponse) -> Unit,
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
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ColorApp.stone100),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(
                            if (product.type == ProductType.WITH_INPUTS) {
                                Res.drawable.ic_kitchen
                            } else {
                                Res.drawable.ic_bottle
                            },
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = ColorApp.amberGoldDark,
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = plazaOnTextStyle(
                            base = MaterialTheme.typography.titleSmall,
                            color = ColorApp.textColor,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                    Text(
                        text = if (product.type == ProductType.WITH_INPUTS) {
                            stringResource(Res.string.home_product_steps, product.steps.size)
                        } else {
                            stringResource(Res.string.home_product_ready)
                        },
                        style = plazaOnTextStyle(
                            base = MaterialTheme.typography.labelSmall,
                            color = ColorApp.textGray,
                        ),
                    )
                }
                Text(
                    text = stringResource(Res.string.price_format, product.price),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.titleMedium,
                        color = ColorApp.amberGoldDark,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
            Spacer(Modifier.height(12.dp))
            ButtonTransparentCustom(
                onClick = { onEdit(product) },
                text = stringResource(Res.string.home_edit_product_action),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                painter = painterResource(Res.drawable.ic_edit),
            )
        }
    }
}

private data class EditDialogParams(
    val editing: EditingProduct,
    val isSaving: Boolean,
    val errorMessage: String?,
    val priceError: String?,
    val onNameChange: (String) -> Unit,
    val onPriceChange: (String) -> Unit,
    val onSave: () -> Unit,
    val onDelete: () -> Unit,
    val onDismiss: () -> Unit,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EditProductDialog(params: EditDialogParams) {
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
                .widthIn(max = 380.dp),
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
                            text = stringResource(Res.string.edit_product_title),
                            style = plazaOnTextStyle(
                                base = MaterialTheme.typography.titleMedium,
                                color = ColorApp.textColor,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                        Text(
                            text = stringResource(Res.string.edit_product_subtitle),
                            style = plazaOnTextStyle(
                                base = MaterialTheme.typography.bodySmall,
                                color = ColorApp.textGray,
                            ),
                        )
                    }
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = stringResource(Res.string.edit_product_close),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { params.onDismiss() },
                        tint = ColorApp.textGray,
                    )
                }
                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(Res.string.edit_product_name_label).uppercase(),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.labelSmall,
                        color = ColorApp.textGray,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = params.editing.name,
                    onValueChange = params.onNameChange,
                    enabled = params.editing.canEditName,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    textStyle = plazaOnTextStyle(
                        base = MaterialTheme.typography.bodyMedium,
                        color = ColorApp.textColor,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorApp.primary,
                        unfocusedBorderColor = ColorApp.stone200,
                        disabledBorderColor = ColorApp.stone200,
                        cursorColor = ColorApp.primary,
                        focusedTextColor = ColorApp.textColor,
                        unfocusedTextColor = ColorApp.textColor,
                        disabledTextColor = ColorApp.textGray,
                    ),
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.edit_product_price_label).uppercase(),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.labelSmall,
                        color = ColorApp.textGray,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = params.editing.price,
                    onValueChange = params.onPriceChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = params.priceError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp),
                    supportingText = params.priceError?.let { message ->
                        {
                            Text(
                                text = message,
                                style = plazaOnTextStyle(
                                    base = MaterialTheme.typography.bodySmall,
                                    color = ColorApp.errorColor,
                                ),
                            )
                        }
                    },
                    textStyle = plazaOnTextStyle(
                        base = MaterialTheme.typography.bodyMedium,
                        color = ColorApp.amberGoldDark,
                        fontWeight = FontWeight.Bold,
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorApp.primary,
                        unfocusedBorderColor = ColorApp.stone200,
                        errorBorderColor = ColorApp.errorColor,
                        cursorColor = ColorApp.primary,
                        focusedTextColor = ColorApp.amberGoldDark,
                        unfocusedTextColor = ColorApp.amberGoldDark,
                    ),
                )

                if (params.editing.original.type == ProductType.WITH_INPUTS) {
                    Spacer(Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(Res.string.edit_product_inputs_title).uppercase(),
                            style = plazaOnTextStyle(
                                base = MaterialTheme.typography.labelSmall,
                                color = ColorApp.textColor,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = stringResource(Res.string.edit_product_inputs_hint),
                        style = plazaOnTextStyle(
                            base = MaterialTheme.typography.labelSmall,
                            color = ColorApp.textGray,
                        ),
                    )
                    Spacer(Modifier.height(10.dp))
                    params.editing.original.steps
                        .sortedBy { it.position }
                        .forEach { step ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(ColorApp.stone100)
                                    .padding(10.dp),
                            ) {
                                Text(
                                    text = step.name,
                                    style = plazaOnTextStyle(
                                        base = MaterialTheme.typography.labelLarge,
                                        color = ColorApp.textColor,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                )
                                Spacer(Modifier.height(6.dp))
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    step.inputs.forEach { input ->
                                        InputChip(
                                            text = input.name + if (input.price > 0) {
                                                stringResource(Res.string.input_extra_price_format, input.price)
                                            } else {
                                                ""
                                            },
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                        }
                }

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
                        onClick = params.onDelete,
                        text = stringResource(Res.string.edit_product_delete),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        enabled = !params.isSaving,
                    )
                    ButtonContainerGreen(
                        onClick = params.onSave,
                        text = stringResource(Res.string.edit_product_save),
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

@Composable
private fun InputChip(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(1.dp, ColorApp.stone200, RoundedCornerShape(50))
            .background(ColorApp.white)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        style = plazaOnTextStyle(
            base = MaterialTheme.typography.labelSmall,
            color = ColorApp.textColor,
            fontWeight = FontWeight.SemiBold,
        ),
    )
}
