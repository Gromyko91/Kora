package com.example.kora

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kora.ui.theme.KoraBackground

@Composable
fun MainScreenUser(
    rootNavController: NavHostController
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val userNavItems = listOf(
        BottomNavItemData("home", Icons.Filled.Home, "Home", activeColor = Color(0xFF3C2A21)),
        BottomNavItemData("favourites", Icons.Outlined.Favorite, "Favourites", activeColor = Color(0xFF3C2A21)),
        BottomNavItemData("cart", Icons.Outlined.ShoppingCart, "Cart", activeColor = Color(0xFF3C2A21)),
        BottomNavItemData("profile", Icons.Rounded.Person, "Profile", activeColor = Color(0xFF3C2A21))
    )

    Scaffold(
        containerColor = KoraBackground,
        bottomBar = {
            KoraBottomBar(
                navController = bottomNavController,
                currentRoute = currentRoute,
                items = userNavItems
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                UserHomeScreen(onNotificationClick = { rootNavController.navigate("notifications") })
            }
            composable("favourites") {
                FavouriteScreen()
            }
            composable("cart") {
                CartScreen(
                    onCheckoutClick =  { bottomNavController.navigate("checkout") }
                )
            }
            composable("profile") {
                ProfileScreen()
            }
            composable("checkout") {}
            composable("order_success") {
                OrderSuccessScreen(
                    onHomeClick = {
                        bottomNavController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}