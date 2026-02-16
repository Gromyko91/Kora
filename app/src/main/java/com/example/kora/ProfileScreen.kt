package com.example.kora

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kora.data.auth.AuthViewModel
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraCard
import com.example.kora.ui.theme.KoraText

@Composable
fun ProfileScreen(
    onLogout: () -> Unit
) {
    val viewModel: AuthViewModel = viewModel()
    val user by viewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCurrentUser()
    }

    val initials = if (user != null && user!!.firstName.isNotEmpty() && user!!.lastName.isNotEmpty()) {
        "${user!!.firstName.first()}${user!!.lastName.first()}".uppercase()
    } else {
        ""
    }

    val fullName = if (user != null) "${user!!.firstName} ${user!!.lastName}" else "Loading..."
    val email = user?.email ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KoraBackground)
            .verticalScroll(rememberScrollState())
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(24.dp))
            Text(text = "Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = KoraText)
            Icon(
                Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Logout",
                tint = KoraText,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        viewModel.logout()
                        onLogout()
                    }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(KoraText, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = initials, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = KoraBackground)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = fullName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = KoraText)
        Text(text = email, fontSize = 14.sp, color = KoraText.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = KoraButton),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.width(200.dp)
        ) {
            Text("Edit Profile")
        }
        Spacer(modifier = Modifier.height(32.dp ))
        ProfileOptionItem(icon = Icons.Default.ShoppingCart, title = "Orders", subtitle = "Track and Manage your deliveries")
        ProfileOptionItem(icon = Icons.Outlined.LocationOn, title = "Address", subtitle = "Manage Your Delivery Addresses")
        ProfileOptionItem(icon = Icons.Default.Notifications, title = "Notifications", subtitle = "Customize Your Alerts")
        ProfileOptionItem(icon = Icons.Default.HeadsetMic, title = "Help & Support", subtitle = "Get Assistance and find answers")
        ProfileOptionItem(icon = Icons.Default.Description, title = "Terms & Policies", subtitle = "Read Our Guidelines and Policies")
        ProfileOptionItem(icon = Icons.Default.ChatBubble, title = "Feedback", subtitle = "Share your experience with us")
        ProfileOptionItem(icon = Icons.Default.Lock, title = "Security & Privacy", subtitle = "Manage your security settings")
    }
}

@Composable
fun ProfileOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = KoraText, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = KoraText)
            Text(text = subtitle, fontSize = 12.sp, color = KoraText.copy(alpha = 0.7f))
        }
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = KoraText)
    }
}