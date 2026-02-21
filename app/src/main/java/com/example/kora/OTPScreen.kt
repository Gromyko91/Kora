package com.example.kora

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kora.ui.theme.KoraAccent
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraPrimary
import com.example.kora.ui.theme.KoraText
import kotlinx.coroutines.delay

@Composable
fun OTPVerifyScreen() {
    val context = LocalContext.current

    val otpValues = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val focusManager = LocalFocusManager.current

    val otpCode = remember(otpValues) { otpValues.joinToString("") }
    val isOTPComplete = remember(otpCode) { otpCode.length == 6 }

    var showError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        focusRequesters[0].requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KoraBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                Text(
                    text = "Enter Verification Code",
                    fontSize = 24.sp,
                    color = KoraText,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "We've sent a 6-digit code to your phone",
                    fontSize = 14.sp,
                    color = KoraText.copy(0.7f)
                )

                Spacer(modifier = Modifier.height(32.dp))
                OTPInputFields(
                    otpValues = otpValues,
                    focusRequesters = focusRequesters,
                    focusManager = focusManager,
                    showError = showError,
                    onValueChange = { index, value ->
                        if (value.length <= 1) {
                            otpValues[index] = value

                            if (value.isNotEmpty() && index < 5) {
                                focusRequesters[index + 1].requestFocus()
                            }

                            if (showError) showError = false
                        }
                    },
                    onBackspace = { index ->
                        if (index > 0 && otpValues[index].isEmpty()) {
                            focusRequesters[index - 1].requestFocus()
                        }
                    }
                )
            }

            if (showError) {
                Text(
                    text = "Please enter all 6 digits",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (isOTPComplete) {
                            Toast.makeText(context, "OTP verifued successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            showError = true
                            Toast.makeText(context, "Please enter all 6 digits", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = KoraButton,
                        disabledContainerColor = KoraButton.copy(0.4f)
                    ),
                    enabled = isOTPComplete
                ) {
                    Text(
                        text = "Verify OTP",
                        fontSize = 16.sp,
                        color = KoraBackground
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = {
                        Toast.makeText(context, "OTP has been sent", Toast.LENGTH_LONG).show()
                        for (i in 0 until 6) {
                            otpValues[i] = ""
                        }
                        focusRequesters[0].requestFocus()
                    }
                ) {
                    Text(
                        text = "Resend Code",
                        color = KoraPrimary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun OTPInputFields(
    otpValues: List<String>,
    focusRequesters: List<FocusRequester>,
    focusManager: FocusManager,
    showError: Boolean,
    onValueChange: (Int, String) -> Unit,
    onBackspace: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (i in 0 until 6) {
            OTPTextField(
                value = otpValues[i],
                onValueChange = { newValue ->
                    onValueChange(i, newValue)
                },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .focusRequester(focusRequesters[i]),
                isError = showError && otpValues[i].isEmpty(),
                onBackspace = {
                    onBackspace(i)
                },
                focusManager = focusManager,
                index = i
            )
        }
    }
}

@Composable
fun OTPTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    onBackspace: () -> Unit,
    focusManager: FocusManager,
    index: Int
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = TextStyle(
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = if (index == 5) ImeAction.Done else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                if (index < 5) {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            },
            onDone = {
                focusManager.clearFocus()
            }
        ),
        decorationBox = { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = remember { MutableInteractionSource() },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = KoraButton,
                    unfocusedBorderColor = if (isError) Color.Red else KoraText,
                    errorBorderColor = Color.Red,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    cursorColor = KoraButton
                ),
                isError = isError,

            )
        }
    )
}

@Preview
@Composable
fun OTPPreview() {
    OTPVerifyScreen()
}