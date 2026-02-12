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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraText

@Composable
fun RestaurantDetailsScreen(
    restaurantId: String,
    onBackClick: () -> Unit,
    onAddDishClick: () -> Unit
) {
//    Mock Data
    val dishes = listOf(
        Triple("Spicy Arrabbiata", "Ksh 1400", "Fresh tomato sauce with garlic, dried red chili peppers."),
        Triple("Truffle Risotto", "Ksh 1850", "Creamy arborio rice with black truffle oil and parmesan."),
        Triple("Classic Tiramisu", "Ksh 900", "Coffee-soaked ladyfingers with mascarpone cream."),
        Triple("Caprese Salad", "Ksh 1200", "Sliced fresh mozzarella, tomatoes, and sweet basil."),
        Triple("Caprese Salad", "Ksh 1200", "Sliced fresh mozzarella, tomatoes, and sweet basil."),
        Triple("Caprese Salad", "Ksh 1200", "Sliced fresh mozzarella, tomatoes, and sweet basil."),
        Triple("Caprese Salad", "Ksh 1200", "Sliced fresh mozzarella, tomatoes, and sweet basil."),
        Triple("Caprese Salad", "Ksh 1200", "Sliced fresh mozzarella, tomatoes, and sweet basil.")
    )

    Scaffold(
        containerColor = KoraBackground,
        topBar = {
            Column(modifier = Modifier.background(KoraBackground)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "back", tint = KoraText)
                    }
                    Text(
                        text = "Ndeti's Kibanda",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = KoraText
                    )
                    IconButton(onClick = {}) {
                        Icon(Icons.Rounded.Search, contentDescription = "Search", tint = KoraText)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "24 Items Listed", fontSize = 14.sp, color = Color.Gray)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { /* Filter */ }
                    ) {
                        Text(text = "Filter", fontSize = 14.sp, color = KoraButton, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.FilterList, contentDescription = null, tint = KoraButton, modifier = Modifier.size(16.dp))
                    }
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .background(Color.Transparent)
            ) {
                Button(
                    onClick = onAddDishClick,
                    colors = ButtonDefaults.buttonColors(containerColor = KoraButton),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Add New Meal", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            items(dishes.size) { index ->
                val (name, price, desc) = dishes[index]
                var isAvailable by remember { mutableStateOf(index != 1) }

                DishItemRow(
                    name = name,
                    price = price,
                    description = desc,
                    isAvailable = isAvailable,
                    onToggleAvailability = { isAvailable = it },
                    onEditClick = {}
                )
            }
        }
    }
}