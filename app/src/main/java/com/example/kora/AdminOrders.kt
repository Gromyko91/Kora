package com.example.kora

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrdersScreen() {
    val context = LocalContext.current
    val filters = listOf("All", "Pending", "Completed", "Delivered", "Cancelled")
    var selectedFilter by remember { mutableStateOf("All") }

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedOrderForUpdate by remember { mutableStateOf<OrderData?>(null) }
    val sheetState = rememberModalBottomSheetState()

    fun openUpdateSheet(orderId: String, name: String, status: String) {
        selectedOrderForUpdate = OrderData(orderId, name, status)
        showBottomSheet = true
    }

    if (showBottomSheet && selectedOrderForUpdate != null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            UpdateStatusSheetContent(
                orderId = selectedOrderForUpdate!!.id,
                customerName = selectedOrderForUpdate!!.name,
                currentStatus = selectedOrderForUpdate!!.status,
                onConfirm = { newStatus ->
                    // 1. Perform logic to update backend (simulated here)
                    // 2. Show Toast
                    Toast.makeText(
                        context,
                        "Order #${selectedOrderForUpdate!!.id} updated to $newStatus",
                        Toast.LENGTH_SHORT
                    ).show()

                    // 3. Close Sheet
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
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF3E2C22),
                        selectedLabelColor = KoraBackground,
                        containerColor = KoraBackground,
                        labelColor = KoraText
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = KoraText,
                        borderWidth = 1.dp,
                        enabled = true,
                        selected = true
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                AdminOrderCard(
                    orderId = "67",
                    name = "Caroline Ndeti",
                    restaurant = "Test Restaurant",
                    items = "1 Item",
                    time = "11:11 AM",
                    amount = "Kes 3,600.00",
                    status = "Processing",
                    onActionClick = {
                        openUpdateSheet("67", "Caroline Ndeti", "Pending")
                    }
                )
            }
            item {
                AdminOrderCard(
                    orderId = "67",
                    name = "Solomon Sahur",
                    restaurant = "Test Restaurant",
                    items = "1 Item",
                    time = "11:11 AM",
                    amount = "Kes 3,600.00",
                    status = "Pending",
                    onActionClick = {
                        openUpdateSheet("68", "Solomon Sahur", "Pending")
                    }
                )
            }
            item {
                AdminOrderCard(
                    orderId = "911",
                    name = "Tralalelo Tralala",
                    restaurant = "Test Restaurant",
                    items = "1 Item",
                    time = "11:11 AM",
                    amount = "Kes 3,600.00",
                    status = "Completed",
                    onActionClick = {
                        openUpdateSheet("911", "Tralalelo Tralala", "Completed")
                    }
                )
            }
            item {
                AdminOrderCard(
                    orderId = "69",
                    name = "Charlie Kirk",
                    restaurant = "Test Restaurant",
                    items = "1 Item",
                    time = "11:11 AM",
                    amount = "Kes 7,600.00",
                    status = "Pending",
                    onActionClick = {
                        openUpdateSheet("68", "Solomon Sahur", "Pending")
                    }
                )
            }
        }
    }
}