package com.example.kora

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kora.data.auth.AuthViewModel
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraText

@Composable
fun UserHomeScreen(onNotificationClick: () -> Unit) {
    val viewModel: AuthViewModel = viewModel()

    val user by viewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCurrentUser()
    }

    val userName = user?.firstName ?: "User"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KoraBackground)
            .padding(top = 1.dp)
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Welcome Back,", fontSize = 16.sp, color = KoraText)
                Text(text = userName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = KoraText)
            }
            IconButton(onClick = onNotificationClick) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notifications",
                    tint = KoraText,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Find A restaurant", color = KoraText.copy(alpha = 0.6f)) },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = KoraText.copy(alpha = 0.6f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
        )
    }
}