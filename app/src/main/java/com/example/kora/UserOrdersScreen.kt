package com.example.kora

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kora.data.order.UserOrderUiState
import com.example.kora.data.order.UserOrdersViewModel
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraCard
import com.example.kora.ui.theme.KoraPrimary
import com.example.kora.ui.theme.KoraText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserOrdersScreen(
    onBackClick: () -> Unit
) {
    val viewModel: UserOrdersViewModel = viewModel()
    val ordersState by viewModel.uiState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val sheetItems by viewModel.selectedOrderItems.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    val selectedOrder by remember { mutableStateOf<UserOrderUiState?>(null) }

    if (showBottomSheet && selectedOrder != null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = KoraCard
        ) {
            OrderDetailsSheetContent(
                order = selectedOrder!!.order,
                items = sheetItems,
                onUpdateStatus = {},
                onDismiss = { showBottomSheet = false }
            )
        }
    }

    Scaffold(
        containerColor = KoraBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Your Orders", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = KoraText) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Back", tint = KoraText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KoraBackground,
                )
            )
        }
    ) { innerpadding ->
        Box(modifier = Modifier.padding(innerpadding).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = KoraPrimary)
            } else if (ordersState.isEmpty()) {
                Text(
                    text = "You haven't placed any orders yet.",
                    color = KoraText.copy(0.5f),
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {  }
            }
        }
    }
}

@Composable
fun UserOrderCard(
    orderState: UserOrderUiState,
    onClick: () -> Unit
) {
    val order = orderState.order
    val isDelivered = order.status == "Delivered" || order.status == "Completed"
    val isCancelled = order.status == "Cancelled"

    val statusColor = when {
        isDelivered -> KoraPrimary
        isCancelled -> Color(0xFFE53935) // Red
        else -> KoraButton
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = KoraCard),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = orderState.restaurantName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = KoraText
                )
                Text(
                    text = "#${order.id.takeLast(6)}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${orderState.formattedDate}  •  ${orderState.itemCount} Items",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF5F5F5))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Total", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = "Ksh ${String.format("%,.0f", order.total)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = KoraText
                    )
                }

                Surface(
                    color = statusColor.copy(0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = order.status,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun OrderScreenPreview() {
    UserOrdersScreen( onBackClick = {})
}