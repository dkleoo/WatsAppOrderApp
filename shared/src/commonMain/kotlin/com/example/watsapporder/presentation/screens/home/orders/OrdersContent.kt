package com.example.watsapporder.presentation.screens.home.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.example.watsapporder.data.mappers.OrderResponse
import com.example.watsapporder.data.mappers.OrderStatus
import com.example.watsapporder.presentation.theme.ColorApp
import com.example.watsapporder.presentation.theme.plazaOnTextStyle
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import watsapporder.shared.generated.resources.Res
import watsapporder.shared.generated.resources.ic_person
import watsapporder.shared.generated.resources.ic_phone
import watsapporder.shared.generated.resources.ic_receipt
import watsapporder.shared.generated.resources.orders_address_format
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(Res.string.orders_order_number, order.id),
                        style = plazaOnTextStyle(
                            base = MaterialTheme.typography.titleSmall,
                            color = ColorApp.textColor,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
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
                icon = Res.drawable.ic_receipt,
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
        OrderStatus.KITCHEN -> ColorApp.googleRedSoft to ColorApp.googleRedDark
        OrderStatus.ON_ROUTE -> ColorApp.amberSoft to ColorApp.amberGoldDark
        OrderStatus.DELIVERED -> ColorApp.stone100 to ColorApp.stone500
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
private fun OrderStatus.label(): String = when (this) {
    OrderStatus.PENDING -> stringResource(Res.string.orders_status_pending)
    OrderStatus.KITCHEN -> stringResource(Res.string.orders_status_kitchen)
    OrderStatus.ON_ROUTE -> stringResource(Res.string.orders_status_on_route)
    OrderStatus.DELIVERED -> stringResource(Res.string.orders_status_delivered)
}

@Composable
private fun OrderFilter.label(): String = when (this) {
    OrderFilter.ALL -> stringResource(Res.string.orders_filter_all)
    OrderFilter.PENDING -> stringResource(Res.string.orders_filter_pending)
    OrderFilter.KITCHEN -> stringResource(Res.string.orders_filter_kitchen)
    OrderFilter.ON_ROUTE -> stringResource(Res.string.orders_filter_on_route)
    OrderFilter.DELIVERED -> stringResource(Res.string.orders_filter_delivered)
}
