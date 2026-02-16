package com.example.kora

import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kora.data.cart.CartViewModel
import com.example.kora.data.model.Address
import com.example.kora.data.order.CheckoutUiState
import com.example.kora.data.order.CheckoutViewModel
import com.example.kora.ui.theme.KoraAccent
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraCard
import com.example.kora.ui.theme.KoraPrimary
import com.example.kora.ui.theme.KoraText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    onBackClick: () -> Unit,
    onPlaceOrderClick: (String, String) -> Unit
) {
    val checkoutViewModel: CheckoutViewModel = viewModel()
    val context = LocalContext.current

    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartTotal by cartViewModel.cartTotal.collectAsState(initial = 0.0)

    val checkoutState by checkoutViewModel.uiState.collectAsState()
    val estTime by checkoutViewModel.estTime.collectAsState()
    val fetchedDeliveryFee by checkoutViewModel.deliveryFee.collectAsState()
    val isFetchingFee by checkoutViewModel.isFetchingFee.collectAsState()

    LaunchedEffect(cartItems) {
        val restaurantId = cartItems.keys.firstOrNull()?.restaurantId
        if (restaurantId != null) {
            checkoutViewModel.fetchDeliveryFee(restaurantId)
        }
    }

    val taxRate = 0.16
    val taxAmount = cartTotal * taxRate
    val finalTotal = cartTotal + taxAmount + fetchedDeliveryFee

    // State for Sheets
    var showAddressSheet by remember { mutableStateOf(false) }
    var showCardSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // State for Data
    var deliveryAddress by remember {
        mutableStateOf(Address(nickname = "Home", street = "Kasarani", city = "Nairobi"))
    }
    var addressLabel by remember { mutableStateOf("Home") }

    var selectedPaymentMethod by remember { mutableStateOf("Card") } // "Card", "Mpesa", "Cash"

    // Saved Cards (Mock)
    val savedCards = remember { mutableStateListOf(Pair("Visa", "4242")) }
    var selectedCardIndex by remember { mutableStateOf(0) }

    LaunchedEffect(checkoutState) {
        when(checkoutState) {
            is CheckoutUiState.Success -> {
                cartViewModel.clearCart()
                val orderId = (checkoutState as CheckoutUiState.Success).orderId
                checkoutViewModel.resetState()
                onPlaceOrderClick(orderId, estTime)
            }
            is CheckoutUiState.Error -> {
                Toast.makeText(context, (checkoutState as CheckoutUiState.Error).message, Toast.LENGTH_LONG).show()
                checkoutViewModel.resetState()
            }
            else -> {}
        }
    }

    // --- BOTTOM SHEETS ---
    if (showAddressSheet) {
        ModalBottomSheet(onDismissRequest = { showAddressSheet = false }, sheetState = sheetState, containerColor = Color.White) {
            AddressSheetContent(
                onSave = { nick, addrString ->
                    deliveryAddress = deliveryAddress.copy(
                        nickname = nick.ifEmpty { "New Address" },
                        street = addrString,
                        city = "Nairobi"
                    )
                    showAddressSheet = false
//                    addressLabel = nick.ifEmpty { "New Address" }
//                    deliveryAddress = addr
//                    showAddressSheet = false
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
                    onClick = {
                        checkoutViewModel.submitOrder(
                            cartItems = cartItems,
                            subtotal = cartTotal,
                            tax = taxAmount,
                            total = finalTotal,
                            paymentMethod = selectedPaymentMethod,
                            address = deliveryAddress
                        )
                    },
                    enabled = checkoutState !is CheckoutUiState.Loading && !isFetchingFee && cartItems.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = KoraButton),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    if (checkoutState is CheckoutUiState.Loading) {
                        CircularProgressIndicator(color = KoraPrimary, modifier = Modifier.size(24.dp))
                    } else {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Place Order", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(cartViewModel.formatTotal(finalTotal), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
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
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFFFDE8E4), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = KoraButton)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(deliveryAddress.nickname, fontWeight = FontWeight.Bold, color = KoraText)
                                Text("${deliveryAddress.street}, ${deliveryAddress.city}", fontSize = 12.sp, color = Color.Gray, maxLines = 1)
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
//
//                            Spacer(modifier = Modifier.height(16.dp))
//                            // Item 2
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Box(modifier = Modifier
//                                    .size(50.dp)
//                                    .clip(RoundedCornerShape(8.dp))
//                                    .background(Color.Gray.copy(alpha = 0.3f)))
//                                Spacer(modifier = Modifier.width(12.dp))
//                                Column(modifier = Modifier.weight(1f)) {
//                                    Text("Sourdough Bread", fontWeight = FontWeight.Bold, color = KoraText)
//                                    Text("1x", fontSize = 12.sp, color = Color.Gray)
//                                }
//                                Text("$4.50", fontWeight = FontWeight.Bold, color = KoraText)
//                            }
                            cartItems.forEach { (dish, qty) ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(KoraCard.copy(alpha = 0.1f))
                                    ) {
                                        if (dish.imageUrl.isNotEmpty()) {
                                            AsyncImage(
                                                model = dish.imageUrl,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            dish.name,
                                            fontWeight = FontWeight.Bold,
                                            color = KoraText
                                        )
                                        Text("${qty}x", fontSize = 12.sp, color = KoraAccent)
                                    }
                                    Text(
                                        "ksh ${dish.price}",
                                        fontWeight = FontWeight.Bold,
                                        color = KoraText
                                    )
                                }
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
                                Text(cartViewModel.formatTotal(cartTotal), fontWeight = FontWeight.Bold, color = KoraText)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Delivery Fee", color = Color.Gray)
                                if (isFetchingFee) {
                                    Text("...", color = Color.Gray)
                                } else {
                                    val feeText = if (fetchedDeliveryFee == 0.0) "Free" else cartViewModel.formatTotal(fetchedDeliveryFee)
                                    Text(feeText, fontWeight = FontWeight.Bold, color = KoraAccent)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Tax (16%)", color = Color.Gray)
                                Text(cartViewModel.formatTotal(taxAmount), fontWeight = FontWeight.Bold, color = KoraText)
                            }
                            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha=0.3f))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = KoraText)
                                Text(cartViewModel.formatTotal(finalTotal), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = KoraButton)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(100.dp)) // Space for sticky button
                }
            }
        }

    }
}