package com.example.kora

import android.text.Layout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Moped
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.SoupKitchen
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kora.data.model.AppNotification
import com.example.kora.data.notifications.NotificationViewModel
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraCard
import com.example.kora.ui.theme.KoraPrimary
import com.example.kora.ui.theme.KoraText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationScreen(onBackClick: () -> Unit) {
    val viewModel: NotificationViewModel = viewModel()
    val notifications by viewModel.notifications.collectAsState()

    var selectedTab by remember { mutableStateOf("All") }
    val tabs = listOf("All", "Orders", "System")

    val unreadCount = notifications.count { !it.isRead }

    val filterNotifs = notifications.filter { notif ->
        when (selectedTab) {
            "Orders" -> notif.type in listOf("ORDER_ACCEPTED", "PREPARING", "OUT_FOR_DELIVERY", "DELIVERED")
            "System" -> notif.type in listOf("SYSTEM", "PAYMENT", "LOW_STOCK")
            else -> true
        }
    }

    val groupedNotifs = filterNotifs.groupBy { notif ->
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val dateStr = sdf.format(Date(notif.createdAt))
        val todayStr = sdf.format(Date())
        val yesterdayStr = sdf.format(Date(System.currentTimeMillis() - 86400000))

        when (dateStr) {
            todayStr -> "TODAY"
            yesterdayStr -> "YESTERDAY"
            else -> dateStr.uppercase(Locale.getDefault())
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KoraBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = KoraText
                )
            }
            Column {
                Text(
                    text = "Notifications",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = KoraText
                )
                if (unreadCount > 0) {
                    Text("You have $unreadCount unread messages", fontSize = 14.sp, color = Color.Gray)
                }
            }
            Text(
                text = "Mark all as read",
                color = KoraPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { viewModel.markAllAsRead() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            color = KoraCard,
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF5E8B89) else Color.Transparent)
                            .clickable { selectedTab = tab }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) KoraCard else KoraText,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            groupedNotifs.forEach { (header, items) ->
                item {
                    Text(
                        text = header,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                items(items) { notif ->
                    NotificationCard(notif)
                }
            }
        }
    }
}

@Composable
fun NotificationCard(notification: AppNotification) {
    val (icon, iconBgColor, iconTint) = when (notification.type) {
        "ORDER_ACCEPTED" -> Triple(Icons.Outlined.ReceiptLong, Color(0xFFEFE8DD), KoraText)
        "PREPARING" -> Triple(Icons.Outlined.SoupKitchen, Color(0xFFEFE8DD), KoraText)
        "OUT_FOR_DELIVERY" -> Triple(Icons.Outlined.Moped, Color(0xFFE0f2f1), Color(0xFF009688))
        "DELIVERED" -> Triple(Icons.Outlined.CheckCircle, Color(0xFFE0f2f1), Color(0xFF009688))
        "LOW_STOCK" -> Triple(Icons.Outlined.Payments, Color(0xFFE8F5E9), Color(0xFF4CAF50))
        else -> Triple(Icons.Outlined.Notifications, Color(0xFFEFE8DD), KoraText)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = KoraCard),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
//            crossAxisAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = KoraText
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val timeDiff = System.currentTimeMillis() - notification.createdAt
                        val timeStr = when {
                            timeDiff < 3600000 -> "${timeDiff / 60000}m ago"
                            timeDiff < 86400000 -> "${timeDiff / 3600000}h ago"
                            else -> "${timeDiff / 86400000}d ago"
                        }
                        Text(text = timeStr, fontSize = 12.sp, color = Color.Gray)

                        if (!notification.isRead) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(modifier = Modifier.size(8.dp).background(KoraButton, CircleShape))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )

                if (notification.referenceId.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = KoraCard,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Order #${notification.referenceId}",
                            fontSize = 12.sp,
                            color = KoraText,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}