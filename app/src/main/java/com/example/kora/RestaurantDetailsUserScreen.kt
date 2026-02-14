package com.example.kora

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kora.data.cart.CartViewModel
import com.example.kora.data.dishes.DishViewModel
import com.example.kora.data.model.Restaurant
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraCard
import com.example.kora.ui.theme.KoraPrimary
import com.example.kora.ui.theme.KoraText
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RestaurantDetailsUserScreen(
    restaurantId: String,
    cartViewModel: CartViewModel,
    onBackClick: () -> Unit,
    onViewCartClick: () -> Unit
) {
    val dishViewModel: DishViewModel = viewModel()
//    val cartViewModel: CartViewModel = viewModel()

    val dishes by dishViewModel.dishes.collectAsState()
    val cartCount by cartViewModel.cartItemCount.collectAsState(initial = 0)
    val cartTotal by cartViewModel.cartTotal.collectAsState(initial = 0.0)

    var restaurant by remember { mutableStateOf<Restaurant?>(null) }
    var selectedCategory by remember { mutableStateOf("Popular") }

    val categories = listOf("Popular", "Main Course", "Appetizer", "Drinks", "Dessert", "Beverage", "Side")

    LaunchedEffect(restaurantId) {
        FirebaseFirestore.getInstance().collection("restaurants").document(restaurantId).get()
            .addOnSuccessListener { restaurant = it.toObject(Restaurant::class.java) }

        dishViewModel.fetchDishes(restaurantId)
    }

    val filterDishes = if (selectedCategory == "Popular") {
        dishes
    } else {
        dishes.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Box(modifier = Modifier.fillMaxSize().background(KoraBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back", modifier = Modifier.size(32.dp))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = restaurant?.name ?: "Loading...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = KoraText
                    )
                    if (restaurant != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Star, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(14.dp))
                            Text("4.8 • ${restaurant!!.estTime} mins • ${restaurant!!.cuisine}", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.MoreHoriz, contentDescription = "More")
                }
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Button(
                        onClick = { selectedCategory = category },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) KoraPrimary else KoraCard,
                            contentColor = if (isSelected) KoraBackground else KoraText
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation =  0.dp),
                        border = if (isSelected) BorderStroke(1.dp, Color.LightGray.copy(0.5f)) else null,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(text = category, fontSize = 14.sp, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }

            Divider(color = Color.LightGray.copy(0.2f), thickness = 1.dp)

            LazyColumn(
                contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "$selectedCategory Items",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = KoraText,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                if (filterDishes.isEmpty()) {
                    item {
                        Text("No items found in this category", color = Color.Gray, modifier = Modifier.padding(vertical = 20.dp))
                    }
                }

                items(filterDishes) { dish ->
                    DishUserCard(
                        dish = dish,
                        onAddClick = { cartViewModel.addToCart((dish)) }
                    )
                }
            }
        }

        if (cartCount > 0) {
            Button(
                onClick = onViewCartClick,
                colors = ButtonDefaults.buttonColors(containerColor = KoraPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(0.2f), CircleShape)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = "$cartCount", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Text("View Cart", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = KoraCard)
                    Text(
                        text = cartViewModel.formatTotal(cartTotal),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = KoraCard
                    )
                }
            }
        }
    }
}