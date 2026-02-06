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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kora.ui.theme.KoraAccent
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraText

@Composable
fun AdminDashboard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KoraBackground)
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Welcome Back,", fontSize = 16.sp, color = KoraText)
                Text(text = "Admin", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = KoraText)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Outlined.Notifications, contentDescription = null, tint = KoraText)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AdminStatCard(
                title = "Pending Orders",
                value = "12",
                icon = Icons.Outlined.ShoppingBag,
                modifier = Modifier.weight(1f)
            )
            AdminStatCard(
                title = "Total Revenue",
                value = "kes 24, 330",
                icon = Icons.Outlined.AttachMoney,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Quick Actions", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = KoraText)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickActionButton(
                text = "Add Restaurant",
                icon = Icons.Rounded.Add,
                modifier = Modifier.weight(1f),
                onClick = {}
            )
            QuickActionButton(
                text = "Add Dish",
                icon = Icons.Rounded.Add,
                modifier = Modifier.weight(1f),
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Recent Activities", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = KoraText)
            Text(text = "View All", fontSize = 14.sp, color = KoraAccent)
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Preview
@Composable
fun AdminDashPreview() {
    AdminDashboard()
}