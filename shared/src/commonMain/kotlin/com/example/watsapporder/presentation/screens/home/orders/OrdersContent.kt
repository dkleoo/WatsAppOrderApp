package com.example.watsapporder.presentation.screens.home.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import com.example.watsapporder.data.mappers.OrderItemStep
import com.example.watsapporder.data.mappers.OrderResponse
import com.example.watsapporder.data.mappers.OrderStatus
import com.example.watsapporder.data.params.OrderDetailDialogParams
import com.example.watsapporder.presentation.screens.components.ButtonContainerGreen
import com.example.watsapporder.presentation.screens.components.ButtonTransparentCustom
import com.example.watsapporder.presentation.theme.ColorApp
import com.example.watsapporder.presentation.theme.plazaOnTextStyle
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.ic_check
import watsapporder.shared.generated.resources.ic_close
import watsapporder.shared.generated.resources.ic_money
import watsapporder.shared.generated.resources.ic_person
import watsapporder.shared.generated.resources.ic_phone
import watsapporder.shared.generated.resources.ic_receipt
import watsapporder.shared.generated.resources.orders_accept
import watsapporder.shared.generated.resources.orders_address_format
import watsapporder.shared.generated.resources.orders_customer_data
import watsapporder.shared.generated.resources.orders_detail_title
import watsapporder.shared.generated.resources.orders_empty
import watsapporder.shared.generated.resources.orders_filter_all
import watsapporder.shared.generated.resources.orders_filter_delivered
import watsapporder.shared.generated.resources.orders_filter_kitchen
import watsapporder.shared.generated.resources.orders_filter_on_route
import watsapporder.shared.generated.resources.orders_filter_pending
import watsapporder.shared.generated.resources.orders_order_number
import watsapporder.shared.generated.resources.orders_payment_default
import watsapporder.shared.generated.resources.orders_payment_format
import watsapporder.shared.generated.resources.orders_phone_format
import watsapporder.shared.generated.resources.orders_products_title
import watsapporder.shared.generated.resources.orders_reject
import watsapporder.shared.generated.resources.orders_send_on_way
import watsapporder.shared.generated.resources.orders_status_cancelled
import watsapporder.shared.generated.resources.orders_status_delivered
import watsapporder.shared.generated.resources.orders_status_kitchen
import watsapporder.shared.generated.resources.orders_status_on_route
import watsapporder.shared.generated.resources.orders_status_pending
import watsapporder.shared.generated.resources.orders_subtitle
import watsapporder.shared.generated.resources.orders_title
import watsapporder.shared.generated.resources.orders_view_products
import watsapporder.shared.generated.resources.price_format

@Composable
fun OrdersContent(
    state: OrdersScreenState,
    onFilterSelected: (OrderFilter) -> Unit,
    onOrderClick: (OrderResponse) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_receipt),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = ColorApp.amberGoldDark,
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.orders_title).uppercase(),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.titleSmall,
                        color = ColorApp.textColor,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Text(
                    text = stringResource(Res.string.orders_subtitle),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.labelSmall,
                        color = ColorApp.textGray,
                    ),
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        OrderFilters(selected = state.filter, onSelect = onFilterSelected)
        Spacer(Modifier.height(12.dp))

        when {
            state.isLoading && state.orders.isEmpty() -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = ColorApp.primary)
            }

            state.visibleOrders.isEmpty() -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.orders_empty),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.bodyMedium,
                        color = ColorApp.textGray,
                    ),
                )
            }

            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.visibleOrders, key = { it.id }) { order ->
                    OrderCard(order = order, onClick = onOrderClick)
                }
            }
        }
    }
}

@Composable
private fun OrderFilters(selected: OrderFilter, onSelect: (OrderFilter) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OrderFilter.entries.forEach { filter ->
            FilterChip(
                label = filter.label(),
                selected = filter == selected,
                onClick = { onSelect(filter) },
            )
        }
    }
}

@Composable
private fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val container = if (selected) ColorApp.amberGold else ColorApp.white
    val content = if (selected) ColorApp.white else ColorApp.textGray
    Text(
        text = label,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(1.dp, if (selected) ColorApp.amberGold else ColorApp.stone200, RoundedCornerShape(50))
            .background(container)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        style = plazaOnTextStyle(
            base = MaterialTheme.typography.labelMedium,
            color = content,
            fontWeight = FontWeight.SemiBold,
        ),
    )
}

@Composable
private fun OrderCard(order: OrderResponse, onClick: (OrderResponse) -> Unit) {
    val cardShape = RoundedCornerShape(16.dp)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ColorApp.stone200, cardShape)
            .clip(cardShape)
            .clickable { onClick(order) },
        shape = cardShape,
        color = ColorApp.white,
        shadowElevation = 4.dp,
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(Res.string.orders_order_number, order.id),
                    modifier = Modifier.weight(1f),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.titleSmall,
                        color = ColorApp.textColor,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                StatusBadge(status = order.status)
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Res.drawable.ic_person),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = ColorApp.amberGoldDark,
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = order.customerName.orEmpty(),
                    modifier = Modifier.weight(1f),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.titleSmall,
                        color = ColorApp.textColor,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Text(
                    text = stringResource(Res.string.price_format, order.total ?: 0.0),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.titleMedium,
                        color = ColorApp.amberGoldDark,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
            Spacer(Modifier.height(6.dp))
            InfoRow(
                icon = Res.drawable.ic_phone,
                text = stringResource(Res.string.orders_phone_format, order.customerPhone),
            )
            order.deliveryAddress?.takeIf { it.isNotBlank() }?.let { address ->
                InfoRow(
                    icon = Res.drawable.ic_receipt,
                    text = stringResource(Res.string.orders_address_format, address),
                )
            }
            InfoRow(
                icon = Res.drawable.ic_money,
                text = stringResource(
                    Res.string.orders_payment_format,
                    order.paymentType ?: stringResource(Res.string.orders_payment_default),
                ),
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Res.drawable.ic_receipt),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = ColorApp.amberGoldDark,
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = order.items.firstOrNull()?.productName.orEmpty(),
                    modifier = Modifier.weight(1f),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.labelMedium,
                        color = ColorApp.textColor,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                Text(
                    text = stringResource(Res.string.orders_view_products),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.labelSmall,
                        color = ColorApp.amberGoldDark,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
        }
    }
}

@Composable
private fun InfoRow(icon: DrawableResource, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = ColorApp.textGray,
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.labelMedium,
                color = ColorApp.textColor,
            ),
        )
    }
}

@Composable
private fun StatusBadge(status: OrderStatus) {
    val (background, content) = when (status) {
        OrderStatus.PENDING -> ColorApp.amberSoft to ColorApp.amberGoldDark
        OrderStatus.IN_KITCHEN -> ColorApp.googleRedSoft to ColorApp.googleRedDark
        OrderStatus.ON_THE_WAY -> ColorApp.amberSoft to ColorApp.amberGoldDark
        OrderStatus.DELIVERED -> ColorApp.stone100 to ColorApp.stone500
        OrderStatus.DRAFT -> ColorApp.stone100 to ColorApp.stone500
        OrderStatus.CANCELLED -> ColorApp.googleRedSoft to ColorApp.googleRedDark
    }
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(content),
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = status.label(),
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.labelSmall,
                color = content,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}

@Composable
fun OrderDetailDialog(params: OrderDetailDialogParams) {
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
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_receipt),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = ColorApp.amberGoldDark,
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = stringResource(Res.string.orders_detail_title).uppercase(),
                                style = plazaOnTextStyle(
                                    base = MaterialTheme.typography.labelSmall,
                                    color = ColorApp.amberGoldDark,
                                    fontWeight = FontWeight.Bold,
                                ),
                            )
                        }
                        Text(
                            text = stringResource(Res.string.orders_order_number, params.order.id),
                            style = plazaOnTextStyle(
                                base = MaterialTheme.typography.headlineSmall,
                                color = ColorApp.textColor,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                    }
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(ColorApp.stone100)
                            .clickable { params.onDismiss() }
                            .padding(5.dp),
                        tint = ColorApp.textGray,
                    )
                }
                Spacer(Modifier.height(12.dp))
                CustomerCard(params.order)
                Spacer(Modifier.height(14.dp))
                Text(
                    text = stringResource(Res.string.orders_products_title),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.labelSmall,
                        color = ColorApp.textGray,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Spacer(Modifier.height(8.dp))
                params.order.items.forEach { item ->
                    ProductBlock(name = item.productName)
                    item.steps.sortedBy { it.position }.forEach { step ->
                        StepBlock(step = step)
                    }
                }
                Spacer(Modifier.height(14.dp))
                PaymentRow(order = params.order)
                Spacer(Modifier.height(18.dp))
                OrderActions(params)
            }
        }
    }
}

@Composable
private fun CustomerCard(order: OrderResponse) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(ColorApp.amberSoft)
            .border(1.dp, ColorApp.amberSoftBorder, shape)
            .padding(14.dp),
    ) {
        Text(
            text = stringResource(Res.string.orders_customer_data),
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.labelSmall,
                color = ColorApp.textGray,
                fontWeight = FontWeight.Bold,
            ),
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = order.customerName.orEmpty(),
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.titleMedium,
                color = ColorApp.textColor,
                fontWeight = FontWeight.Bold,
            ),
        )
        Spacer(Modifier.height(4.dp))
        InfoRow(
            icon = Res.drawable.ic_phone,
            text = stringResource(Res.string.orders_phone_format, order.customerPhone),
        )
        order.deliveryAddress?.takeIf { it.isNotBlank() }?.let { address ->
            InfoRow(
                icon = Res.drawable.ic_receipt,
                text = stringResource(Res.string.orders_address_format, address),
            )
        }
    }
}

@Composable
private fun ProductBlock(name: String) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(ColorApp.amberSoft)
            .border(1.dp, ColorApp.amberSoftBorder, shape)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_receipt),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = ColorApp.amberGoldDark,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = name,
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.titleSmall,
                color = ColorApp.textColor,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}

@Composable
private fun StepBlock(step: OrderItemStep) {
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(shape)
            .background(ColorApp.cardBackGroundGray)
            .border(1.dp, ColorApp.stone200, shape)
            .padding(12.dp),
    ) {
        Text(
            text = step.name.uppercase(),
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.labelSmall,
                color = ColorApp.textGray,
                fontWeight = FontWeight.Bold,
            ),
        )
        step.inputs.forEach { input ->
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = input.name,
                    modifier = Modifier.weight(1f),
                    style = plazaOnTextStyle(
                        base = MaterialTheme.typography.bodyMedium,
                        color = ColorApp.textColor,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                if (input.price > 0) {
                    Text(
                        text = stringResource(Res.string.price_format, input.price),
                        style = plazaOnTextStyle(
                            base = MaterialTheme.typography.labelMedium,
                            color = ColorApp.amberGoldDark,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentRow(order: OrderResponse) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(ColorApp.stone100)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_money),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = ColorApp.textGray,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = order.paymentType ?: stringResource(Res.string.orders_payment_default),
            modifier = Modifier.weight(1f),
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.bodyMedium,
                color = ColorApp.textColor,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        Text(
            text = stringResource(Res.string.price_format, order.total ?: 0.0),
            style = plazaOnTextStyle(
                base = MaterialTheme.typography.titleMedium,
                color = ColorApp.textColor,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}

@Composable
private fun OrderActions(params: OrderDetailDialogParams) {
    if (params.isUpdating || params.isLoading) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = ColorApp.primary)
        }
        return
    }

    when (params.order.status) {
        OrderStatus.PENDING -> {
            ButtonContainerGreen(
                onClick = { params.onAccept(params.order.id) },
                text = stringResource(Res.string.orders_accept),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            )
            Spacer(Modifier.height(10.dp))
            RejectButton(onClick = { params.onReject(params.order.id) })
        }

        OrderStatus.IN_KITCHEN -> {
            ButtonContainerGreen(
                onClick = { params.onSendOnTheWay(params.order.id) },
                text = stringResource(Res.string.orders_send_on_way),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            )
            Spacer(Modifier.height(10.dp))
            RejectButton(onClick = { params.onReject(params.order.id) })
        }

        else -> Unit
    }
}

@Composable
private fun RejectButton(onClick: () -> Unit) {
    Text(
        text = stringResource(Res.string.orders_reject),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ColorApp.googleRedSoft)
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        style = plazaOnTextStyle(
            base = MaterialTheme.typography.labelLarge,
            color = ColorApp.googleRedDark,
            fontWeight = FontWeight.Bold,
        ),
    )
}

@Composable
private fun OrderStatus.label(): String = when (this) {
    OrderStatus.DRAFT -> stringResource(Res.string.orders_status_pending)
    OrderStatus.PENDING -> stringResource(Res.string.orders_status_pending)
    OrderStatus.IN_KITCHEN -> stringResource(Res.string.orders_status_kitchen)
    OrderStatus.ON_THE_WAY -> stringResource(Res.string.orders_status_on_route)
    OrderStatus.DELIVERED -> stringResource(Res.string.orders_status_delivered)
    OrderStatus.CANCELLED -> stringResource(Res.string.orders_status_cancelled)
}

@Composable
private fun OrderFilter.label(): String = when (this) {
    OrderFilter.ALL -> stringResource(Res.string.orders_filter_all)
    OrderFilter.PENDING -> stringResource(Res.string.orders_filter_pending)
    OrderFilter.KITCHEN -> stringResource(Res.string.orders_filter_kitchen)
    OrderFilter.ON_THE_WAY -> stringResource(Res.string.orders_filter_on_route)
    OrderFilter.DELIVERED -> stringResource(Res.string.orders_filter_delivered)
}
