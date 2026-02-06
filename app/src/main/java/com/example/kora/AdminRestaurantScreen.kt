package com.example.kora

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kora.ui.theme.KoraAccent
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraText

@Composable
fun AdminRestaurantScreen(
    onAddRestaurantClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KoraBackground)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Restaurants",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = KoraText
            )
            IconButton(
                onClick = onAddRestaurantClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(KoraAccent, CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add restaurant", tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search by name...", color = KoraText.copy(alpha = 0.6f)) },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = KoraText.copy(alpha = 0.6f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                RestaurantCard(
                    name = "The Burger Joint",
                    cuisine = "American • Fast Food",
                    dishes = "24",
                    rating = "4.8",
                    onEditClick = {},
                    onViewMealsClick = {}
                )
            }
            item {
                RestaurantCard(
                    name = "Pasta Paradise",
                    cuisine = "Italian • Gourmet",
                    dishes = "12",
                    rating = "4.7",
                    onEditClick = {},
                    onViewMealsClick = {}
                )
            }
            item {
                RestaurantCard(
                    name = "Sushi Master",
                    cuisine = "Japanese • Sushi",
                    dishes = "10",
                    rating = "4.9",
                    onEditClick = {},
                    onViewMealsClick = {}
                )
            }
            item {
                RestaurantCard(
                    name = "The Burger Joint",
                    cuisine = "American • Fast Food",
                    dishes = "24",
                    rating = "4.8",
                    onEditClick = {},
                    onViewMealsClick = {}
                )
            }
            item {
                RestaurantCard(
                    name = "The Burger Joint",
                    cuisine = "American • Fast Food",
                    dishes = "24",
                    rating = "4.8",
                    onEditClick = {},
                    onViewMealsClick = {}
                )
            }
            item {
                RestaurantCard(
                    name = "The Burger Joint",
                    cuisine = "American • Fast Food",
                    dishes = "24",
                    rating = "4.8",
                    onEditClick = {},
                    onViewMealsClick = {}
                )
            }
        }
    }
}