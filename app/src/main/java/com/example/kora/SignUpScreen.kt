package com.example.kora

import android.R
import android.util.Patterns
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kora.ui.theme.KoraAccent
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraText

@Composable
fun SignUpScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    onValidationError: (String) -> Unit
) {
//    var name by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }

//    Validation States
    var isEmailError by remember { mutableStateOf(false) }
    var isPhoneError by remember { mutableStateOf(false) }

    fun validateAndSignUp() {
        isEmailError = false
        isPhoneError = false

        val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isValidPhone = countryCode.isNotEmpty() && phoneNumber.isNotEmpty()

        if (!isValidEmail) {
            isEmailError = true
            onValidationError("Please enter a valid email address!")
            return
        }
        if (!isValidPhone) {
            isPhoneError = true
            onValidationError("Please complete the phone number.")
            return
        }

        if (password.length < 6) {
            onValidationError("Password must be at least 6 characters.")
            return
        }

        if (!termsAccepted) {
            onValidationError("Please accept the terms and conditions.")
            return
        }
        onSignUpClick
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KoraBackground)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(text = "Let’s Get Started", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = KoraText)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Your next meal is a few taps away. Sign up now", fontSize = 16.sp, color = KoraText)

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                KoraTextField(
                    label = "First Name",
                    placeholder = "e.g John",
                    value = firstName,
                    onValueChange = { firstName = it },
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                KoraTextField(
                    label = "last Name",
                    placeholder = "e.g Caroline",
                    value = lastName,
                    onValueChange = { lastName = it },
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        KoraTextField(
            label = "Email",
            placeholder = "Enter Your Email",
            value = email,
            onValueChange = {
                email = it
                isEmailError = false
            },
            keyboardType = KeyboardType.Email,
            isError = isEmailError
        )
        Spacer(modifier = Modifier.height(16.dp))

        PhoneInputRow(
            countryCode = countryCode,
            onCountryCodeChange = { countryCode = it },
            phoneNumber = phoneNumber,
            onPhoneNumberChange = { phoneNumber = it },
            isError = isPhoneError
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
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = {termsAccepted = it},
                    colors = CheckboxDefaults.colors(checkedColor = KoraButton, uncheckedColor = KoraText)
                )
            Text(text = "I agree to the terms of Service and Privacy policy")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { validateAndSignUp() },
            colors = ButtonDefaults.buttonColors(containerColor = KoraButton),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text="Sign me Up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already a member? ", color = KoraText, fontWeight = FontWeight.Bold)
            Text(
                text = "Sign In",
                color = KoraAccent,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onSignInClick() }
            )
        }
    }
}