package com.example.kora

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kora.data.auth.AuthState
import com.example.kora.data.auth.AuthViewModel
import com.example.kora.ui.theme.KoraAccent
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraText

@Composable
fun LoginScreen(
    onLoginClick: (String) -> Unit,
    onForgotPassword: () -> Unit,
    onSignUpClick: () -> Unit
//    onAdminClick: () -> Unit
) {

    val viewModel: AuthViewModel = viewModel()
    val authState by viewModel.authState.collectAsState()

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember {mutableStateOf("")}

    var errorMessageShown by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                val role = (authState as AuthState.Success).role
                onLoginClick(role)
                viewModel.resetState()
                errorMessageShown = false
            }
            is AuthState.Error -> {
                if (!errorMessageShown) {
                    val errorMessage = (authState as AuthState.Error).message
                    Toast.makeText(
                        context,
                        "Wrong credentials provided",
                        Toast.LENGTH_LONG
                    ).show()
                    errorMessageShown = true
                }
                viewModel.resetState()
            }
            else -> {
                errorMessageShown = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KoraBackground)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Welcome Back", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = KoraText)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Sign In to Continue Feasting", fontSize = 16.sp, color = KoraText)

        Spacer(modifier = Modifier.height(32.dp))

        KoraTextField(
            label = "Email",
            placeholder = "Enter your email",
            value = email,
            onValueChange = { email = it },
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(16.dp))

        KoraTextField(
            label = "Password",
            placeholder = "Enter your password",
            value = password,
            onValueChange = { password = it },
            isPassword = true,
            keyboardType = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Text(
                text = "Forgot Password",
                color = KoraAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onForgotPassword() }
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { viewModel.signIn(email, password) },
            colors = ButtonDefaults.buttonColors(containerColor = KoraButton),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(color = KoraText, modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Sign In", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
//        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Don't have an account?", color = KoraText, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Sign Up",
                color = KoraAccent,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onSignUpClick() }
            )
        }
    }
}