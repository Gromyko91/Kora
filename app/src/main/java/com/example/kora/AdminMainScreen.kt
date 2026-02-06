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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

    val showBottomBar = when (currentRoute) {
        "add_restaurant" -> false
        "restaurant_details" -> false
        "add_dish" -> false
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
                AdminRestaurantScreen(
                    onAddRestaurantClick = { adminNavController.navigate("add_restaurant") }
                )
            }
            composable("add_restaurant") {
                AddRestaurantScreen(
                    onBackClick = { adminNavController.popBackStack() },
                    onSaveClick = { adminNavController.navigate("restaurant_details") }
                )
            }

            composable("restaurant_details") {
                RestaurantDetailsScreen(
                    onBackClick = { adminNavController.popBackStack() },
                    onAddDishClick = { adminNavController.navigate("add_dish") }
                )
            }

            composable("add_dish") {
                AddDishScreen(
                    onBackClick = { adminNavController.popBackStack() },
                    onSaveClick = { adminNavController.popBackStack() }
                )
            }
            composable("profile") {
                ProfileScreen()
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