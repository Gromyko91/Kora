package com.example.kora

import android.R.attr.onClick
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LunchDining
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SoupKitchen
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.kora.ui.theme.KoraAccent
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraPrimary
import com.example.kora.ui.theme.KoraText
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.kora.data.admin.AdminOrderUiState
import com.example.kora.data.model.Dish
import com.example.kora.data.model.Order
import com.example.kora.data.model.OrderItem
import com.example.kora.data.model.Restaurant
import com.example.kora.ui.theme.KoraBox
import com.example.kora.ui.theme.KoraCard
import com.example.kora.utils.TimeUtils
import java.nio.file.WatchEvent
import java.util.Locale
import java.util.jar.Manifest

@Composable
fun KoraTextField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = KoraText,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = KoraText.copy(alpha = 0.7f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = KoraBackground,
                unfocusedContainerColor = KoraBackground,
                focusedBorderColor = KoraText,
                unfocusedBorderColor = KoraText,
                errorBorderColor = MaterialTheme.colorScheme.error,
                cursorColor = KoraText,
                focusedTextColor = KoraText,
                unfocusedTextColor = KoraText
            ),
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = if (isPassword) {
                {
                    val image = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle Password", tint = KoraText)
                    }
                }
            } else null,
            singleLine = true
        )
    }
}

@Composable
fun PhoneInputRow(
    countryCode: String,
    onCountryCodeChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    isError: Boolean = false
) {
    val eastAfricanCountries = listOf(
        Country("Kenya", "+254"),
        Country("Uganda", "+256"),
        Country("Tanzania", "+255"),
        Country("Rwanda", "+250"),
        Country("Burundi", "+257"),
        Country("South Sudan", "+211"),
        Country("Ethiopia", "+251"),
        Country("Somalia", "+252"),
        Country("Djibouti", "+253"),
        Country("Eritrea", "+291")
    )

    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Phone",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = KoraText,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .padding(end = 8.dp)
            ) {
                OutlinedTextField(
                    value = countryCode,
                    onValueChange = {  },
                    readOnly = true,
                    placeholder = { Text("+254", color = KoraText.copy(alpha = 0.7f)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { expanded = true },
                    shape = RoundedCornerShape(12.dp),
                    isError = isError,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = KoraBackground,
                        unfocusedContainerColor = KoraBackground,
                        focusedBorderColor = KoraText,
                        unfocusedBorderColor = KoraText,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        cursorColor = KoraText,
                        focusedTextColor = KoraText,
                        unfocusedTextColor = KoraText
                    ),
                    singleLine = true,
                    enabled = true,
                    interactionSource = remember { MutableInteractionSource() }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(KoraCard)
                ) {
                    eastAfricanCountries.forEach { country ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "${country.name} (${country.code})",
                                    color = KoraText
                                )
                            },
                            onClick = {
                                onCountryCodeChange(country.code)
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                isError = isError,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = KoraBackground,
                    unfocusedContainerColor = KoraBackground,
                    focusedBorderColor = KoraText,
                    unfocusedBorderColor = KoraText,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    cursorColor = KoraText,
                    focusedTextColor = KoraText,
                    unfocusedTextColor = KoraText
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )
        }
    }
}

data class Country(
    val name: String,
    val code: String
)

// Cart Items
@Composable
fun CartItemRow(
    dish: Dish,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = KoraCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray.copy(alpha = 0.3f))
            ) {
                if (dish.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = dish.imageUrl,
                        contentDescription = dish.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = dish.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = KoraText)
                    Text(text = "Ksh ${dish.price}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText)
                }
                Text(text = dish.description, fontSize = 12.sp, color = Color.Gray, maxLines = 2, overflow = TextOverflow.Ellipsis )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    ) {
                        IconButton(onClick = onDecrease, enabled = quantity > 1, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Rounded.Remove, contentDescription = "Decrease", tint = KoraText, modifier = Modifier.size(16.dp))
                        }
                        Text(text = quantity.toString(), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText, modifier = Modifier.padding(horizontal = 8.dp))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(KoraAccent, RoundedCornerShape(8.dp))
                                .clickable { onIncrease() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = "Increase", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                    IconButton(onClick = onRemove) {
                        Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = KoraText, modifier = Modifier.size(24.dp))
                    }
                }
            }
        }
    }
}

// Bottom Navigation Items
@Composable
fun KoraBottomBar(
    navController: NavHostController,
    currentRoute: String?,
    items: List<BottomNavItemData>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(KoraButton)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                BottomNavItem(
                    icon = item.icon,
                    label = item.label,
                    isSelected = currentRoute == item.route,
                    activeColor = item.activeColor,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) activeColor else KoraBackground,
            modifier = Modifier.size(28.dp)
        )
        if (isSelected) {
            Text(
                text = label,
                color = activeColor,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

// Admin Components
@Composable
fun AdminStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFAFA59B))
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = KoraText,
                modifier = Modifier.align(Alignment.TopEnd)
            )
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                Text(text = title, fontSize = 12.sp, color = KoraBackground)
                Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = KoraText)
            }
        }
    }
}

@Composable
fun QuickActionButton(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = KoraText)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Gray.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = text, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AdminOrderCard(
    orderData: AdminOrderUiState,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onViewDetails: () -> Unit
) {
    val status = orderData.order.status
    val isPending = status == "pending"
    val isCompleted = status in listOf("Delivered", "Completed", "Cancelled")

    val statusColor = when (status) {
        "Processing" -> Color(0xFF81C784)
        "Completed" -> Color(0xFF81C784)
        "Pending" -> Color(0xFFFFCC80)
        else -> Color.Gray
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable(enabled = !isCompleted) { onViewDetails() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = KoraCard),
        border = BorderStroke(1.dp, KoraText)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(text = "ORDER #${orderData.order.id}", fontSize = 12.sp, color = Color.Gray)
                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = status.uppercase(),
                        fontSize = 10.sp,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = orderData.customerName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = KoraText)

            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(text = "${orderData.restaurantName}  •  ${orderData.itemCount} Items  •  ${orderData.formattedTime}", fontSize = 12.sp, color = Color.Gray)
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = DividerDefaults.Thickness,
                color = Color.Gray.copy(alpha = 0.3f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Total Amount", fontSize = 10.sp, color = Color.Gray)
                    Text(text = "ksh ${orderData.order.total}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = KoraText)
                }
                if (status == "Pending") {
                    Row {
                        IconButton(
                            onClick = onReject,
                            modifier = Modifier.background(Color(0xFFFFCDD2), RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Rounded.Close, contentDescription = "Reject", tint = Color.Red)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onAccept,
                            colors = ButtonDefaults.buttonColors(containerColor = KoraText),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            Text("Accept", color = KoraBackground)
                        }
                    }
                } else {
                    Button(
                        onClick = onViewDetails,
                        colors = ButtonDefaults.buttonColors(containerColor = if(status == "Processing") Color(0xFF3E2C22) else KoraBackground),
                        border = if(status == "Processing") null else BorderStroke(1.dp, KoraText),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (status == "Processing") "Update Status" else "View Details",
                            color = if (status == "Processing") Color.White else KoraText
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusOptionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val borderColor = if (isSelected) KoraButton else Color.Transparent
    val backgroundColor = if (isSelected) Color(0xFFFFF5F2) else Color(0xFFF8F8F8)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onSelect() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = KoraText, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = KoraText)
            Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = KoraButton, unselectedColor = Color.Gray)
        )
    }
}

@Composable
fun UpdateStatusSheetContent(
    orderId: String,
    customerName: String,
    currentStatus: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedStatus by remember { mutableStateOf(currentStatus) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.3f))
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Update Order Status",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = KoraText,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Order #$orderId • $customerName",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))
        StatusOptionRow(
            title = "Pending",
            subtitle = "Order received, waiting for action",
            icon = Icons.Outlined.Schedule,
            isSelected = selectedStatus == "Pending",
            onSelect = { selectedStatus = "Pending"}
        )
        StatusOptionRow(
            title = "Preparing",
            subtitle = "Kitchen is Working on the order",
            icon = Icons.Outlined.SoupKitchen,
            isSelected = selectedStatus == "Preparing",
            onSelect = { selectedStatus = "Preparing"}
        )
        StatusOptionRow(
            title = "Out for Delivery",
            subtitle = "Driver is on the way.",
            icon = Icons.Outlined.LocalShipping,
            isSelected = selectedStatus == "Out for Delivery",
            onSelect = { selectedStatus = "Out For Delivery"}
        )
        StatusOptionRow(
            title = "Delivered",
            subtitle = "Food has arrived Safely",
            icon = Icons.Outlined.CheckCircle,
            isSelected = selectedStatus == "Delivered",
            onSelect = { selectedStatus = "Delivered"}
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { onConfirm(selectedStatus) },
            colors = ButtonDefaults.buttonColors(containerColor = KoraButton),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = "Confirm Update", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel", color = Color.Gray, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// Admin Order Sheet Content
@Composable
fun OrderDetailsSheetContent(
    order: Order,
//    currentStatus: String,
    items: List<OrderItem>,
    onUpdateStatus: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val isReadOnly = order.status in listOf("Delivered", "Cancelled", "Completed")
    var selectedStatus by remember { mutableStateOf(order.status) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .heightIn(min = 300.dp, max = 600.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Order Details", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = KoraText)
                Text("Order #${order.id}", fontSize = 14.sp, color = Color.Gray)
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, null, tint = KoraText)
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Payment Method", fontSize = 12.sp, color = KoraAccent)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = when(order.paymentMethod) {
                                "Cash" -> Icons.Outlined.Money
                                "Mpesa" -> Icons.Outlined.Smartphone
                                else -> Icons.Outlined.CreditCard
                            },
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = KoraText
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(order.paymentMethod, fontWeight = FontWeight.Bold, color = KoraText)
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Payment Status", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = if (order.paymentStatus == "Paid") Color(0xFFE8F5E9) else Color(
                            0xFFFFEBEE
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = order.paymentStatus.uppercase(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (order.paymentStatus == "Paid") Color(0xFF2E7D32) else Color(
                                0xFFC62828
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Text("Items (${items.size}", fontWeight = FontWeight.Bold, color = KoraText, modifier = Modifier.padding(bottom = 8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(items) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray.copy(0.2f))
                    ) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.name, fontWeight = FontWeight.Bold, color = KoraText)
                        Text("${item.quantity}x", fontSize = 12.sp, color = Color.Gray)
                    }

                    Text(
                        text = "Ksh ${item.price * item.quantity}",
                        fontWeight = FontWeight.Bold,
                        color = KoraText
                    )
                }
            }
        }

        if (!isReadOnly) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            Text("Update Status", fontWeight = FontWeight.Bold, color = KoraText, modifier = Modifier.padding(bottom = 12.dp))

            val statuses = listOf("Preparing", "Out for Delivery", "Delivered")

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                statuses.forEach { status ->
                    val isSelected = selectedStatus == status

                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedStatus = status },
                        label = { Text(status) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = KoraButton,
                            selectedLabelColor = KoraBackground
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onUpdateStatus(selectedStatus) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KoraButton)
            ) {
                Text("Update Status")
            }
        }
    }
}


//Restaurants Screen - Admin
@Composable
fun RestaurantCard(
    name: String,
    cuisine: String,
    dishes: String,
    rating: String,
    imageUrl: String,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Gray.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Restaurant Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Store,
                            contentDescription = null,
                            tint = Color.Gray.copy(alpha = 0.5f),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = KoraText, maxLines = 1)

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFF2E7D32))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(text = rating, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                            }
                        }
                    }
                    Text(text = cuisine, fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.RestaurantMenu, contentDescription = null,tint = KoraText, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "$dishes Dishes", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = onEditClick,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Icon(Icons.Outlined.Edit, contentDescription = null, tint = KoraText, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit", color = KoraText)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = onDeleteClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Icon(Icons.Outlined.Delete, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun DottedUploadBox(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .background(Color.Transparent)
            .drawBehind {
                val strokeWidth = 4.dp.toPx()
                val stroke = Stroke(
                    width = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
                )
                val inset = strokeWidth / 2
                drawRoundRect(
                    color = Color.LightGray,
                    style = stroke,
                    cornerRadius = CornerRadius(24.dp.toPx()),
                    topLeft = Offset(inset, inset),
                    size = Size(size.width - strokeWidth, size.height - strokeWidth)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(KoraText, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = KoraBackground)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Cover Photo", fontWeight = FontWeight.Bold, color = KoraText)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun DishItemRow(
    name: String,
    category: String,
    price: String,
    description: String,
    imageUrl: String,
    allergens: List<String>,
    isAvailable: Boolean,
    onToggleAvailability: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(KoraPrimary.copy(alpha = 0.3f))
            ) {
                if (imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(0.2f)))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                if (category.isNotEmpty()) {
                    Text(
                        text = category.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = KoraButton.copy(alpha = 0.6f),
                        letterSpacing =  1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = KoraText)
                        Text(text = "Ksh $price", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraButton)
                    }
                    Switch(
                        checked = isAvailable,
                        onCheckedChange = { onToggleAvailability(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = KoraText,
                            checkedTrackColor = KoraButton,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.LightGray
                        ),
                        modifier = Modifier.scale(0.8f)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (allergens.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        allergens.forEach { allergen ->
                            Surface(
                                color = Color(0xFFFFF3E0),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = allergen,
                                    fontSize = 10.sp,
                                    color = Color(0xFFE65100),
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onEditClick, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Rounded.Edit, null, tint = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Rounded.Delete, null, tint = Color.Red.copy(alpha = 0.7f))
                    }
                }
            }
        }
    }
}

// Checkout Components
@Composable
fun PaymentMethodOption(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val borderColor = if (isSelected) KoraButton else Color.Transparent
    val backgroundColor = if (isSelected) Color.White else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = if (isSelected) BorderStroke(2.dp, KoraButton) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(KoraBackground, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = KoraText)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, color = KoraText)
                if (subtitle != null) {
                    Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
                }
            }

            if (isSelected) {
                Icon(Icons.Outlined.CheckCircleOutline, contentDescription = "Selected", tint = KoraButton)
            } else {
                RadioButton(selected = false, onClick = onSelect, colors = RadioButtonDefaults.colors(unselectedColor = Color.LightGray))
            }
        }
    }
}

// --- SHEET: ADD NEW CARD ---
@Composable
fun AddCardSheetContent(
    onSave: (String, String) -> Unit, // Returns Number, Type
    onCancel: () -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var cardType by remember { mutableStateOf("Unknown") }

    // Card Detection Logic
    LaunchedEffect(cardNumber) {
        cardType = when {
            cardNumber.startsWith("4") -> "Visa"
            cardNumber.startsWith("5") || cardNumber.startsWith("2") -> "MasterCard"
            else -> "Unknown"
        }
    }

    // Card Number Formatting (Groups of 4)
    val cardFilter = object : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
            var out = ""
            for (i in trimmed.indices) {
                out += trimmed[i]
                if (i % 4 == 3 && i != 15) out += " "
            }

            val numberOffsetTranslator = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    if (offset <= 3) return offset
                    if (offset <= 7) return offset + 1
                    if (offset <= 11) return offset + 2
                    if (offset <= 16) return offset + 3
                    return out.length
                }
                override fun transformedToOriginal(offset: Int): Int {
                    if (offset <= 4) return offset
                    if (offset <= 9) return offset - 1
                    if (offset <= 14) return offset - 2
                    if (offset <= 19) return offset - 3
                    return trimmed.length
                }
            }
            return TransformedText(AnnotatedString(out), numberOffsetTranslator)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 24.dp) // Extra padding for safety
    ) {
        Text("Add New Card", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = KoraText)
        Spacer(modifier = Modifier.height(24.dp))

        // Card Number Input
        OutlinedTextField(
            value = cardNumber,
            onValueChange = { if (it.length <= 16) cardNumber = it.filter { char -> char.isDigit() } },
            label = { Text("Card Number") },
            trailingIcon = {
                // Show Card Icon based on type
                when (cardType) {
                    "Visa" -> Text("VISA", fontWeight = FontWeight.Bold, color = Color(0xFF1A1F71), modifier = Modifier.padding(end = 8.dp)) // Simple text for icon
                    "MasterCard" -> Text("MC", fontWeight = FontWeight.Bold, color = Color(0xFFEB001B), modifier = Modifier.padding(end = 8.dp))
                    else -> Icon(Icons.Outlined.CreditCard, contentDescription = null)
                }
            },
            visualTransformation = cardFilter,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = expiry,
                onValueChange = { if (it.length <= 4) expiry = it },
                label = { Text("MM/YY") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = cvv,
                onValueChange = { if (it.length <= 3) cvv = it },
                label = { Text("CVV") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onSave(cardNumber, cardType) },
            colors = ButtonDefaults.buttonColors(containerColor = KoraButton),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Save Card", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AddressSheetContent(
    onSave: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var nickname by remember { mutableStateOf("") }
    var detectedLocation by remember { mutableStateOf("Detecting Location...") }
    var isLocating by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation(fusedLocationClient) { lat, lng ->
                detectedLocation = "Lat: $lat, long: $lng"
                isLocating = false
            }
        } else {
            detectedLocation = "Location permission denied"
            isLocating = false
        }
    }

    fun detectLocation() {
        isLocating = true

        when {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {

                getCurrentLocation(fusedLocationClient) { lat, lng ->
                    detectedLocation = "Lat: $lat, Long: $lng"
                    isLocating = false
                }
            }

            else -> {
                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        Text(
            "Delivery Address",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = KoraText
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { detectLocation() },
            colors = ButtonDefaults.buttonColors(containerColor = KoraAccent),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                if (isLocating) "Locating..." else "Use Current Location",
                fontWeight = FontWeight.Bold
            )
        }

        if (detectedLocation.isNotEmpty() && !isLocating) {
            Text(
                text = detectedLocation,
                fontSize = 12.sp,
                color = KoraText,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("Address Nickname (e.g, Home, Work)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onSave(nickname, detectedLocation) },
            colors = ButtonDefaults.buttonColors(containerColor = KoraButton),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Save Address", fontWeight = FontWeight.Bold)
        }
    }
}

// Get Location Helper
@SuppressLint("MissingPermission")
fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (Double, Double) -> Unit
) {
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            if (location != null) {
                onLocationReceived(location.latitude, location.longitude)
            }
        }
        .addOnFailureListener {
            it.printStackTrace()
        }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserRestaurantCard(
    restaurant: Restaurant,
    isFavourite: Boolean,
    onRestaurantClick: (String) -> Unit,
    onToggleFavourite: (String) -> Unit
    ) {
    val isOpen = TimeUtils.isRestaurantOpen(restaurant.openingTime, restaurant.closingTime)

    val statusText = if (isOpen) {
        "Closes at ${restaurant.closingTime}"
    } else {
        "Opens at ${restaurant.openingTime}"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(bottom = 16.dp)
            .clickable(enabled = isOpen) {
                onRestaurantClick(restaurant.id)
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(restaurant.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = restaurant.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 100f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = { onToggleFavourite(restaurant.id) },
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.3f), CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Toggle Favorite",
                            tint = if (isFavourite) KoraAccent else Color.White
                        )
                    }
                }

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = restaurant.name,
                            color = KoraCard,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        Surface(
                            color = KoraButton,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    // Use restaurant.rating if available, else N/A
                                    text = "N/A",
                                    color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Icon(Icons.Filled.Star, null, tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = restaurant.cuisine, color = Color.LightGray, fontSize = 12.sp)
                        Text(text = " | ", color = Color.LightGray)
                        Text(text = TimeUtils.formatCurrency(restaurant.deliveryFee) + " Delivery", color = Color.LightGray, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = statusText,
                        color = KoraAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            if (!isOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CLOSED",
                        color = KoraBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 2.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FavouriteRestaurantCard(
    restaurant: Restaurant,
    onNavigateToDetails: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .padding(16.dp)
            .clickable { onNavigateToDetails() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(restaurant.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(Color.White, CircleShape)
                        .size(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favourite",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Star, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "N/A", // Replace with restaurant.rating when available
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = KoraText
                            )
                        }
                    }
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.AccessTime, null, tint = KoraText, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = restaurant.estTime,
                                fontSize = 12.sp,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = restaurant.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = KoraText
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            // Cuisine & Price
            Text(
                text = "${restaurant.cuisine} • $$",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Delivery Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Small Truck Icon or just text
                Text(text = restaurant.deliveryFee, fontSize = 12.sp, color = KoraText.copy(alpha = 0.7f))
                Text(text = "  •  ", color = Color.Gray)
                Text(text = "1.2 km away", fontSize = 12.sp, color = KoraText.copy(alpha = 0.7f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- BUTTONS ROW ---
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Order Again Button (Takes full available width minus remove button)
                Button(
                    onClick = onNavigateToDetails,
                    colors = ButtonDefaults.buttonColors(containerColor = KoraAccent),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(45.dp)
                ) {
                    Text("Order Again", color = Color.White, fontWeight = FontWeight.Bold)
                }

                // Remove Button
                OutlinedButton(
                    onClick = onRemoveClick,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .width(100.dp)
                        .height(45.dp)
                ) {
                    Text("Remove", color = KoraAccent, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun DishUserCard(
    dish: Dish,
    onAddClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = KoraCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(dish.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray.copy(0.1f))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dish.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = KoraText,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Ksh ${dish.price}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = KoraPrimary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dish.description,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        dish.allergens.take(3).forEach { allergen ->
                            Surface(
//                                color = Color(0xFFE0F2F1),
                                color = KoraBox,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = allergen,
//                                    color = Color(0xFF00695C),
                                    color = KoraBackground,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Button(
                        onClick = onAddClick,
                        colors = ButtonDefaults.buttonColors(containerColor = KoraButton),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                    ) {
                        Text("Add", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = KoraBackground)
                    }
                }
            }
        }
    }
}












