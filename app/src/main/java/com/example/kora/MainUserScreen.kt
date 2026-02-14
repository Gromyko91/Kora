package com.example.kora

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kora.data.cart.CartViewModel
import com.example.kora.ui.theme.KoraBackground

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreenUser(
    rootNavController: NavHostController
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val sharedCartViewModel: CartViewModel = viewModel()

    val userNavItems = listOf(
        BottomNavItemData("home", Icons.Filled.Home, "Home", activeColor = Color(0xFF3C2A21)),
        BottomNavItemData("favourites", Icons.Outlined.Favorite, "Favourites", activeColor = Color(0xFF3C2A21)),
        BottomNavItemData("cart", Icons.Rounded.ShoppingCart, "Cart", activeColor = Color(0xFF3C2A21)),
        BottomNavItemData("profile", Icons.Rounded.Person, "Profile", activeColor = Color(0xFF3C2A21))
    )

    // hide Bottom Bar Logic
    val showBottomBar = when {
        currentRoute == "checkout" -> false
        currentRoute == "order_success" -> false
        currentRoute?.startsWith("restaurant_details_user") == true -> false
        else -> true
    }

    Scaffold(
        containerColor = KoraBackground,
        bottomBar = {
            if (showBottomBar) {
                KoraBottomBar(
                    navController = bottomNavController,
                    currentRoute = currentRoute,
                    items = userNavItems
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                UserHomeScreen(
                    onNotificationClick = { rootNavController.navigate("notifications") },
                    onRestaurantClick = { restaurantId ->
                        bottomNavController.navigate("restaurant_details_user/$restaurantId")
                    }
                )
            }
            composable("favourites") {
                FavouriteScreen(
                    onRestaurantClick = { restaurantId ->
                        bottomNavController.navigate("restaurant_details_user/$restaurantId")
                    }
                )
            }

            composable("restaurant_details_user/{restaurantId}") { backStackEntry ->
                val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
                RestaurantDetailsUserScreen(
                    restaurantId = restaurantId,
                    onBackClick = { bottomNavController.popBackStack() },
                    cartViewModel = sharedCartViewModel,
                    onViewCartClick = { bottomNavController.navigate("cart") }
                )
            }
            composable("cart") {
                CartScreen(
                    cartViewModel = sharedCartViewModel,
                    onCheckoutClick =  { bottomNavController.navigate("checkout") }
                )
            }
            composable("profile") {
                ProfileScreen(
                    onLogout = {
                        rootNavController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable("checkout") {
                CheckoutScreen(
                    onBackClick = { bottomNavController.popBackStack() },
                    onPlaceOrderClick = { bottomNavController.navigate("order_success") }
                )
            }
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