package com.example.kora

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kora.data.restaurants.RestaurantViewModel
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraText

@Composable
fun FavouriteScreen(
    onRestaurantClick: (String) -> Unit
) {
    val viewModel: RestaurantViewModel = viewModel()

    val favouriteRestaurants by viewModel.favouriteRestaurants.collectAsState(initial = emptyList())

    val restaurants by viewModel.restaurants.collectAsState()
    val favouriteIds by viewModel.favouriteIds.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KoraBackground)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = "Favourites", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = KoraText)
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (favouriteRestaurants.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
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
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "No Love Yet", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = KoraText)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap the heart Icon on any restaurant to save it here for later.",
                    fontSize = 16.sp,
                    color = KoraText.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(favouriteRestaurants) { restaurant ->
                    FavouriteRestaurantCard(
                        restaurant = restaurant,
                        onNavigateToDetails = { onRestaurantClick(restaurant.id) },
                        onRemoveClick = {
                            viewModel.removeFromFavourites(restaurant.id)
                        }
                    )
                }
            }
        }
    }
}