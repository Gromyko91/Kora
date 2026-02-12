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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import com.example.kora.data.dishes.DishViewModel
import com.example.kora.data.model.Restaurant
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraCard
import com.example.kora.ui.theme.KoraText
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RestaurantDetailsScreen(
    restaurantId: String,
    onBackClick: () -> Unit,
    onAddDishClick: () -> Unit
) {
    val dishViewModel: DishViewModel = viewModel()
    val dishes by dishViewModel.dishes.collectAsState()

    var restaurant by remember { mutableStateOf<Restaurant?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(restaurantId) {
        if (restaurantId.isNotEmpty()) {
            FirebaseFirestore.getInstance()
                .collection("restaurants")
                .document(restaurantId)
                .get()
                .addOnSuccessListener { document ->
                    restaurant = document.toObject(Restaurant::class.java)
                    isLoading = false
                }
                .addOnFailureListener {
                    isLoading = false
                }
        }
        dishViewModel.fetchDishes(restaurantId)
    }

    Scaffold(
        containerColor = KoraBackground,
        topBar = {
            if (restaurant != null) {
                Column(modifier = Modifier.background(KoraBackground)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "back", tint = KoraText)
                        }
                        Text(
                            text = restaurant!!.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = KoraText
                        )
//                        IconButton(onClick = {}) {
//                            Icon(Icons.Rounded.Search, contentDescription = "Search", tint = KoraText)
//                        }
                    }
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 24.dp, vertical = 8.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(text = "24 Items Listed", fontSize = 14.sp, color = Color.Gray)
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier.clickable { /* Filter */ }
//                        ) {
//                            Text(text = "Filter", fontSize = 14.sp, color = KoraButton, fontWeight = FontWeight.Bold)
//                            Spacer(modifier = Modifier.width(4.dp))
//                            Icon(Icons.Default.FilterList, contentDescription = null, tint = KoraButton, modifier = Modifier.size(16.dp))
//                        }
//                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddDishClick,
                containerColor = KoraButton,
                contentColor = KoraBackground
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add Dish")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = KoraButton)
            } else if (restaurant == null) {
                Text("Restaurant not found", color = KoraText)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                ) {
                    item {
                        Text("Cuisine: ${restaurant!!.cuisine}", fontSize = 16.sp, color = KoraText)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Operating Hours: ${restaurant!!.openingTime} - ${restaurant!!.closingTime}", fontSize = 14.sp, color = KoraText)
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Menu", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = KoraText)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    if (dishes.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No dishes added yet.", color = KoraText.copy(alpha = 0.5f), fontSize = 14.sp)
                            }
                        }
                    } else {
                        items(dishes.size) { index ->
                            val dish = dishes[index]
                            DishItemRow(
                                name = dish.name,
                                category = dish.category,
                                price = dish.price,
                                description = dish.description,
                                imageUrl = dish.imageUrl,
                                allergens = dish.allergens,
                                isAvailable = dish.isAvailable,
                                onToggleAvailability = {},
                                onEditClick = {}
                            )
                        }
                    }
                }
            }
        }
    }
}