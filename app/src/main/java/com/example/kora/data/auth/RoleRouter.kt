package com.example.kora.data.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun RoleRouter(
    navController: NavHostController,
    viewModel: AuthViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
//        viewModel.fetchUserRole(
//            onSuccess = { role ->
//                val destination =
//                    if (role == "admin") "admin_main" else "main"
//
//                navController.navigate(destination) {
//                    popUpTo(0)
//                }
//            },
//            onError = {
//                navController.navigate("login") {
//                    popUpTo(0)
//                }
//            }
//        )
    }
    Box(modifier = Modifier.fillMaxSize()) {}
}