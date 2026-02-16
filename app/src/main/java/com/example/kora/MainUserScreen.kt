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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
        currentRoute?.startsWith("order_success") == true -> false
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
                    cartViewModel = sharedCartViewModel,
                    onBackClick = { bottomNavController.popBackStack() },
                    onPlaceOrderClick = {  orderId, estTime ->
                        bottomNavController.navigate("order_success/$orderId/$estTime")
                    }
                )
            }
            composable(
                route = "order_success/{orderId}/{estTime}",
                arguments = listOf(
                    navArgument("orderId") { type = NavType.StringType },
                    navArgument("estTime") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId") ?: "Unknown"
                val estTime = backStackEntry.arguments?.getString("estTime") ?: "N/A"
                OrderSuccessScreen(
                    orderId = orderId,
                    estTime = estTime,
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