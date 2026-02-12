package com.example.kora

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kora.data.restaurants.RestaurantViewModel
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton

@Composable
fun AdminMainScreen(
    rootNavController: NavHostController
) {
    val adminNavController = rememberNavController()
    val navBackStackEntry by adminNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val adminNavItems = listOf(
        BottomNavItemData("dashboard", Icons.Filled.Dashboard, "Home", activeColor = Color(0xFF3C2A21)),
        BottomNavItemData("orders", Icons.Filled.ShoppingBag, "Orders", activeColor = Color(0xFF3C2A21)),
        BottomNavItemData("restaurant", Icons.Outlined.Store, "Restaurants", activeColor = Color(0xFF3C2A21)),
        BottomNavItemData("profile", Icons.Filled.Person, "Profile", activeColor = Color(0xFF3C2A21))
    )

    val showBottomBar = when {
        currentRoute == "add_restaurant" -> false
        currentRoute?.startsWith("restaurant_details") == true -> false
        currentRoute?.startsWith("add_dish") == true -> false
        else -> true
    }

    Scaffold(
        containerColor = KoraBackground,
        bottomBar = {
            if (showBottomBar) {
                KoraBottomBar(
                    navController = adminNavController,
                    currentRoute = currentRoute,
                    items = adminNavItems
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = adminNavController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        )  {
            composable("dashboard") {
                AdminDashboard()
            }
            composable("orders") {
                AdminOrdersScreen()
            }
            composable("restaurant") {
                val viewModel: RestaurantViewModel = viewModel()
                AdminRestaurantScreen(
                    onAddRestaurantClick = { adminNavController.navigate("add_restaurant") },
                    onRestaurantClick = { restaurant ->
                        adminNavController.navigate("restaurant_details/${restaurant.id}")
                    }
                )
            }
            composable("add_restaurant") {
                AddRestaurantScreen(
                    onBackClick = { adminNavController.popBackStack() },
                    onSaveClick = { adminNavController.navigate("restaurant_details") }
                )
            }

            composable("restaurant_details/{restaurantId}") { backStackEntry ->
                val restaurantId = backStackEntry.arguments?.getString("restaurantId")

                RestaurantDetailsScreen(
                    restaurantId = restaurantId ?: "",
                    onBackClick = { adminNavController.popBackStack() },
                    onAddDishClick = { adminNavController.navigate("add_dish/$restaurantId") }
                )
            }

            composable("add_dish/{restaurantId}") { backStackEntry ->
                val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
                AddDishScreen(
                    restaurantId = restaurantId,
                    onBackClick = { adminNavController.popBackStack() },
                    onSaveClick = { adminNavController.popBackStack() }
                )
            }
            composable("profile") {
                ProfileScreen(
                    onLogout = {
                        adminNavController.navigate("login") {
                            popUpTo(0) { inclusive = true}
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AdminMainPreview() {
    AdminMainScreen(
        rootNavController = rememberNavController()
    )
}