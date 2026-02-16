package com.example.kora

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kora.data.admin.AdminOrderUiState
import com.example.kora.data.admin.AdminOrderViewModel
import com.example.kora.data.model.OrderData
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraCard
import com.example.kora.ui.theme.KoraText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrdersScreen() {
    val viewModel: AdminOrderViewModel = viewModel()

    // State
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val sheetItems by viewModel.selectedOrderItems.collectAsState()

    val context = LocalContext.current
    val filters = listOf("All", "Pending", "Preparing", "Completed", "Delivered", "Cancelled")
    var selectedFilter by remember { mutableStateOf("All") }

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedOrderForSheet by remember { mutableStateOf<AdminOrderUiState?>(null) }
    val sheetState = rememberModalBottomSheetState()

    var showCancelDialog by remember { mutableStateOf(false) }
    var orderToCancel by remember { mutableStateOf<AdminOrderUiState?>(null) }

    fun openSheet(order: AdminOrderUiState) {
        selectedOrderForSheet = order
        viewModel.fetchOrderItems(order.order.id)
        showBottomSheet = true
    }

    fun promptCancel(order: AdminOrderUiState) {
        orderToCancel = order
        showCancelDialog = true
    }

    if (showCancelDialog && orderToCancel != null) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Cancel Order?", fontWeight = FontWeight.Bold, color = KoraText) },
            text = { Text("Are you sure you want to cancel Order #${orderToCancel!!.order.id}? This action cannot be undone.", color = Color.Gray) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateStatus(orderToCancel!!.order.id, "Cancelled")
                        showCancelDialog = false
                        orderToCancel = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KoraButton)
                ) {
                    Text("Yes, Cancel", color = KoraBackground)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("No, Keep", color = KoraText)
                }
            },
            containerColor = KoraCard
        )
    }

//    fun openUpdateSheet(orderId: String, name: String, status: String) {
//        selectedOrderForUpdate = OrderData(orderId, name, status)
//        showBottomSheet = true
//    }

    if (showBottomSheet && selectedOrderForSheet != null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            OrderDetailsSheetContent(
                order = selectedOrderForSheet!!.order,
//                currentStatus = selectedOrderForSheet!!.order.status,
                items = sheetItems,
                onUpdateStatus = { newStatus ->
                    viewModel.updateStatus(selectedOrderForSheet!!.order.id, newStatus)
                    showBottomSheet = false
                },
                onDismiss = { showBottomSheet = false }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KoraBackground)
            .padding(top = 15.dp, start = 20.dp, end = 24.dp)
    ) {
        Text(
            text = "Orders Overview",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = KoraText,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filters) { filter ->
                val isSelected = selectedFilter == filter
                FilterChip(
                    selected =isSelected,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF3E2C22),
                        selectedLabelColor = KoraBackground,
                        containerColor = KoraBackground,
                        labelColor = KoraText
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if(isSelected) Color.Transparent else KoraText,
                        borderWidth = 1.dp,
                        enabled = true,
                        selected = true
                    )
                )
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = KoraButton)
            }
        } else {
            val filteredOrders = if (selectedFilter == "All") {
                orders
            } else {
                orders.filter { it.order.status.equals(selectedFilter, ignoreCase = true) }
            }
            LazyColumn(
                contentPadding = PaddingValues(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredOrders.isEmpty()) {
                    item {
                        Text(
                            text = "No orders found.",
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 20.dp).fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                items(filteredOrders) { uiOrder ->
                    AdminOrderCard(
                        orderData = uiOrder,
                        onAccept = {
                            viewModel.updateStatus(uiOrder.order.id, "Preparing")
                        },
                        onReject = {
                            promptCancel(uiOrder)
                        },
                        onViewDetails = {
                            openSheet(uiOrder)
                        }
                    )
                }
            }
        }

    }
}