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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kora.data.cart.CartViewModel
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraCard
import com.example.kora.ui.theme.KoraText

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onCheckoutClick: () -> Unit
) {
//    val cartViewModel: CartViewModel = viewModel()
    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartTotal by cartViewModel.cartTotal.collectAsState(initial = 0.0)

    var showClearDialog by remember {  mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog },
            title = { Text(text = "Clear Cart", color = KoraText, fontWeight = FontWeight.Bold) },
            text = { Text(text = "Are you sure you want to remove all items from your cart?", color = KoraText) },
            confirmButton = {
                TextButton(
                    onClick = {
                        cartViewModel.clearCart()
                        showClearDialog = false
                    }
                ) {
                    Text("Clear", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showClearDialog = false }
                ) {
                    Text("Cancel", color = KoraButton)
                }
            },
            containerColor = KoraCard
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KoraBackground)
            .padding(15.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Text(
                text = "Cart",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = KoraText,
                modifier = Modifier.align(Alignment.Center)
            )
            if (cartItems.isNotEmpty()) {
                Text(
                    text = "Clear all",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { showClearDialog = true }
                )
            }
        }
        if (cartItems.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(KoraButton, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SentimentDissatisfied,
                        contentDescription = null,
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(60.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Where's the food?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = KoraText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You can’t eat air! Go add some delicious items to your cart",
                    fontSize = 16.sp,
                    color = KoraText,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(modifier = Modifier.weight(1f)) {
                LazyColumn {
                    items(cartItems.entries.toList()) { (dish, quantity) ->
                        CartItemRow(
                            dish = dish,
                            quantity = quantity,
                            onIncrease = { cartViewModel.incrementQuantity(dish) },
                            onDecrease = { cartViewModel.decrementQuantity(dish) },
                            onRemove = { cartViewModel.removeFromCart(dish) }
                        )
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onCheckoutClick,
                    colors = ButtonDefaults.buttonColors(containerColor = KoraButton),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Checkout", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = cartViewModel.formatTotal(cartTotal), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}