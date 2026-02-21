package com.example.kora

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kora.onboarding.OnboardingScreen1
import com.example.kora.onboarding.OnboardingScreen2
import com.example.kora.ui.theme.KoraTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoraTheme {
//                OnboardingScreen2(onNext = {})
//                LoginScreen()
                KoraApp()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun KoraApp() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val showMessage: (String) -> Unit = { message ->
        scope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    val permissionsToRequest = remember {
        mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }.toTypedArray()
    }

    val permisionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val locationGranted = permissionsMap[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
        permissionsMap[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        val notificationsGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsMap[Manifest.permission.POST_NOTIFICATIONS] == true
        } else {
            true
        }

        if (!locationGranted || !notificationsGranted) {
            showMessage("Some features like adresses and alerts may not work fully.")
        }
    }

    LaunchedEffect(Unit) {
        permisionLauncher.launch(permissionsToRequest)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = "splash",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("splash") {
                    SplashScreen(onTimeout = {
                        navController.navigate("onboarding1") {
                            popUpTo("splash") {inclusive = true}
                        }
                    })
                }
                composable("onboarding1") {
                    OnboardingScreen1(onNext = {navController.navigate("onboarding2") })
                }
                composable("onboarding2") {
                    OnboardingScreen2(onNext = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    })
                }
                composable("login") {
                    LoginScreen(
                        onLoginClick = { role ->
                            val destination = if (role == "admin") "admin_main" else "main"
                            navController.navigate(destination ) {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onForgotPassword = { navController.navigate("forgot") },
                        onSignUpClick = { navController.navigate("signup") },
//                        onAdminClick = { navController.navigate("admin_main") }
                    )
                }
                composable("signup") {
                    SignUpScreen(
                        onSignUpClick = {
                            navController.navigate("main") {
                                popUpTo("signup") { inclusive = true }
                            }
                        },
                        onSignInClick = { navController.popBackStack() },
                        onValidationError = { showMessage(it) }
                    )
                }
                composable("forgot") {
                    ForgotPasswordScreen(
                        onBackClick = { navController.popBackStack() },
                        onSubmitClick = { showMessage("Password reset email Sent") },
                        onValidationError = { showMessage(it) }
                    )
                }
                composable("main") {
                    MainScreenUser(rootNavController = navController)
                }
                composable("admin_main") {
                    AdminMainScreen(rootNavController = navController)
                }
                composable("notifications") {
                    NotificationScreen(onBackClick = { navController.popBackStack() })
                }
            }
        }
    }
}
