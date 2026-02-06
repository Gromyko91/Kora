package com.example.kora

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kora.ui.theme.KoraAccent
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit,
    onPlaceOrderClick: () -> Unit
) {
    // State for Sheets
    var showAddressSheet by remember { mutableStateOf(false) }
    var showCardSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // State for Data
    var deliveryAddress by remember { mutableStateOf("123 Earthy Lane, Foodie City, CA") }
    var addressLabel by remember { mutableStateOf("Home") }

    var selectedPaymentMethod by remember { mutableStateOf("Card") } // "Card", "Mpesa", "Cash"

    // Saved Cards (Mock)
    val savedCards = remember { mutableStateListOf(Pair("Visa", "4242")) }
    var selectedCardIndex by remember { mutableStateOf(0) }

    // --- BOTTOM SHEETS ---
    if (showAddressSheet) {
        ModalBottomSheet(onDismissRequest = { showAddressSheet = false }, sheetState = sheetState, containerColor = Color.White) {
            AddressSheetContent(
                onSave = { nick, addr ->
                    addressLabel = nick.ifEmpty { "New Address" }
                    deliveryAddress = addr
                    showAddressSheet = false
                },
                onCancel = { showAddressSheet = false }
            )
        }
    }

    if (showCardSheet) {
        ModalBottomSheet(onDismissRequest = { showCardSheet = false }, sheetState = sheetState, containerColor = Color.White) {
            AddCardSheetContent(
                onSave = { number, type ->
                    val last4 = if(number.length >=4) number.takeLast(4) else number
                    savedCards.add(Pair(type, last4))
                    selectedCardIndex = savedCards.lastIndex // Auto select new card
                    selectedPaymentMethod = "Card"
                    showCardSheet = false
                },
                onCancel = { showCardSheet = false }
            )
        }
    }

    Scaffold(
        containerColor = KoraBackground,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(KoraBackground)
                    .padding(24.dp)
            ) {
                Button(
                    onClick = onPlaceOrderClick,
                    colors = ButtonDefaults.buttonColors(containerColor = KoraButton),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Place Order", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("$17.70", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .padding(innerPadding)
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
//            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
//                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back", tint = KoraText)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text("Checkout", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = KoraText)
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.size(48.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp)
            ) {
                item {
                    Text("Delivery Address", fontWeight = FontWeight.Bold, color = KoraText)
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(40.dp).background(Color(0xFFFDE8E4), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = KoraButton)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(addressLabel, fontWeight = FontWeight.Bold, color = KoraText)
                                Text(deliveryAddress, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                            }
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = KoraButton,
                                modifier = Modifier.clickable { showAddressSheet = true }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Text("Order Summary", fontWeight = FontWeight.Bold, color = KoraText)
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Item 1
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)).background(Color.Gray.copy(alpha=0.3f)))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Spicy Pumpkin Soup", fontWeight = FontWeight.Bold, color = KoraText)
                                    Text("2x", fontSize = 12.sp, color = Color.Gray)
                                }
                                Text("$12.00", fontWeight = FontWeight.Bold, color = KoraText)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            // Item 2
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)).background(Color.Gray.copy(alpha=0.3f)))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Sourdough Bread", fontWeight = FontWeight.Bold, color = KoraText)
                                    Text("1x", fontSize = 12.sp, color = Color.Gray)
                                }
                                Text("$4.50", fontWeight = FontWeight.Bold, color = KoraText)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // --- PAYMENT METHOD ---
                item {
                    Text("Payment Method", fontWeight = FontWeight.Bold, color = KoraText)
                    Spacer(modifier = Modifier.height(12.dp))

                    savedCards.forEachIndexed { index,  (type, last4) ->
                        PaymentMethodOption(
                            icon = Icons.Outlined.CreditCard,
                            title = "$type ending in $last4",
                            isSelected = selectedPaymentMethod == "Card" && selectedCardIndex == index,
                            onSelect = {
                                selectedPaymentMethod = "Card"
                                selectedCardIndex = index
                            }
                        )
                    }

                    TextButton(
                        onClick = { showCardSheet = true },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = KoraButton)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add New Card", color = KoraText)
                    }

                    PaymentMethodOption(
                        icon = Icons.Outlined.Smartphone,
                        title = "M-Pesa",
                        isSelected = selectedPaymentMethod == "Mpesa",
                        onSelect = { selectedPaymentMethod = "Mpesa" }
                    )

                    PaymentMethodOption(
                        icon = Icons.Outlined.Money,
                        title = "Cash on Delivery",
                        isSelected = selectedPaymentMethod == "Cash",
                        onSelect = { selectedPaymentMethod = "Cash" }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // --- Totals ---
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Subtotal", color = Color.Gray)
                                Text("$16.50", fontWeight = FontWeight.Bold, color = KoraText)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Delivery Fee", color = Color.Gray)
                                Text("Free", fontWeight = FontWeight.Bold, color = KoraAccent)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Tax", color = Color.Gray)
                                Text("$1.20", fontWeight = FontWeight.Bold, color = KoraText)
                            }
                            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha=0.3f))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = KoraText)
                                Text("$17.70", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = KoraButton)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(100.dp)) // Space for sticky button
                }
            }
        }

    }
}