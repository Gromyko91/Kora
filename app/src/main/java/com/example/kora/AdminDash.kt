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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kora.data.admin.ActivityItem
import com.example.kora.data.admin.ActivityType
import com.example.kora.data.admin.AdminDashboardViewModel
import com.example.kora.data.auth.AuthViewModel
import com.example.kora.ui.theme.KoraAccent
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraBox
import com.example.kora.ui.theme.KoraCard
import com.example.kora.ui.theme.KoraPrimary
import com.example.kora.ui.theme.KoraText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboard(
    onAddRestaurantClick: () -> Unit,
    onViewAllActivitiesClick: () -> Unit
) {
    val authViewModel: AuthViewModel = viewModel()
    val dashViewModel: AdminDashboardViewModel = viewModel()

    val user by authViewModel.currentUser.collectAsState()
    val pendingOrders by dashViewModel.pendingOrdersCount.collectAsState()
    val revenue by dashViewModel.totalRevenue.collectAsState()
    val activities by dashViewModel.activites.collectAsState()
    val isLoading by dashViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.fetchCurrentUser()
        dashViewModel.fetchDashboardData()
    }

    val adminName = user?.firstName ?: "Admin"

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
                Text(text = adminName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = KoraText)
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
                value = pendingOrders.toString(),
                icon = Icons.Outlined.ShoppingBag,
                modifier = Modifier.weight(1f)
            )
            AdminStatCard(
                title = "Total Revenue",
                value = "kes ${String.format("%,.0f", revenue)}",
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
                onClick = onAddRestaurantClick
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
            Text(text = "View All", fontSize = 14.sp, color = KoraAccent, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onViewAllActivitiesClick() })
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = KoraAccent)
            }
        } else {
            activities.take(7).forEach { activity ->
                ActivityRowItem(activity)
            }
            if (activities.isEmpty()) {
                Text("No recent activities", color = Color.Gray, fontSize = 14.sp)
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun ActivityRowItem(activity: ActivityItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(KoraCard, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            val icon = when(activity.type) {
                ActivityType.ORDER -> Icons.Outlined.ShoppingBag
                ActivityType.RESTAURANT -> Icons.Rounded.Store
                else -> Icons.Rounded.Notifications
            }
            val tint = when(activity.type) {
                ActivityType.ORDER -> KoraPrimary
                ActivityType.RESTAURANT -> KoraAccent
                else -> KoraBox
            }
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = activity.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText)
            Text(text = activity.description, fontSize = 12.sp, color = Color.Gray)
        }

        Column(horizontalAlignment = Alignment.End) {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            Text(text = sdf.format(Date(activity.timestamp)), fontSize = 12.sp, color = Color.Gray)

            if (activity.amount != null) {
                Text(
                    text = "+ Kes ${activity.amount.toInt()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = KoraPrimary
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun AdminDashPreview() {
//    AdminDashboard()
//}