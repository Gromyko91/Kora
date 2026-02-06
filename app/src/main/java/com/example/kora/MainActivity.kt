package com.example.kora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kora.onboarding.OnboardingScreen1
import com.example.kora.onboarding.OnboardingScreen2
import com.example.kora.ui.theme.KoraTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
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
                        onLoginClick = {
                            navController.navigate("main") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onForgotPassword = { navController.navigate("forgot") },
                        onSignUpClick = { navController.navigate("signup") },
                        onAdminClick = { navController.navigate("admin_main") }
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
