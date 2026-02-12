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
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Store
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.kora.ui.theme.KoraCard
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
    Column {
        Text(
            text = "Phone",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = KoraText,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = countryCode,
                onValueChange = { if (it.length <= 4) onCountryCodeChange(it) },
                placeholder = { Text("+254", color = KoraText.copy(alpha = 0.7f)) },
                modifier = Modifier
                    .width(80.dp)
                    .padding(end = 8.dp),
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

// Cart Items
@Composable
fun CartItemRow(
    title: String,
    subtitle: String,
    price: String,
    quantity: Int,
    imageResId: Int? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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

            }
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = KoraText)
                    Text(text = price, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText)
                }
                Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
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
                        IconButton(onClick = {  }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Rounded.Remove, contentDescription = "Decrease", tint = KoraText, modifier = Modifier.size(16.dp))
                        }
                        Text(text = quantity.toString(), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText, modifier = Modifier.padding(horizontal = 8.dp))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(KoraAccent, RoundedCornerShape(8.dp))
                                .clickable {  },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = "Increase", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                    IconButton(onClick = {  }) {
                        Icon(Icons.TwoTone.Delete, contentDescription = "Delete", tint = KoraText, modifier = Modifier.size(24.dp))
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
        Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
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
    orderId: String,
    name: String,
    restaurant: String,
    items: String,
    time: String,
    amount: String,
    status: String,
    onActionClick: () -> Unit
) {
    val statusColor = when (status) {
        "Processing" -> Color(0xFF81C784)
        "Completed" -> Color(0xFF81C784)
        "Pending" -> Color(0xFFFFCC80)
        else -> Color.Gray
    }
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = KoraBackground),
        border = BorderStroke(1.dp, KoraText)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(text = "ORDER #$orderId", fontSize = 12.sp, color = Color.Gray)
                SuggestionChip(
                    onClick = {},
                    label = { Text(status, fontSize = 10.sp) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = KoraBackground,
                        labelColor = statusColor
                    ),
                    border = BorderStroke(1.dp, statusColor),
                    modifier = Modifier.height(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = KoraText)

            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(text = "$restaurant  •  $items  •  $time", fontSize = 12.sp, color = Color.Gray)
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
                    Text(text = amount, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = KoraText)
                }
                if (status == "Pending") {
                    Row {
                        IconButton(
                            onClick = { },
                            modifier = Modifier.background(Color(0xFFFFCDD2), RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Rounded.Close, contentDescription = "Reject", tint = Color.Red)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onActionClick,
                            colors = ButtonDefaults.buttonColors(containerColor = KoraText),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            Text("Accept", color = KoraBackground)
                        }
                    }
                } else {
                    Button(
                        onClick = onActionClick,
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
    onViewMealsClick: () -> Unit
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
                    modifier = Modifier.weight(1f).height(40.dp)
                ) {
                    Icon(Icons.Outlined.Edit, contentDescription = null, tint = KoraText, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit", color = KoraText)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = onViewMealsClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KoraAccent),
                    modifier = Modifier.weight(1f).height(40.dp)
                ) {
                    Icon(Icons.Outlined.LunchDining, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Meals", color = Color.White)
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
    price: String,
    description: String,
    isAvailable: Boolean,
    onToggleAvailability: (Boolean) -> Unit,
    onEditClick: () -> Unit
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
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = KoraText)
                        Text(text = price, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraButton)
                    }
                    Switch(
                        checked = isAvailable,
                        onCheckedChange = onToggleAvailability,
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
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
                    Icon(
                        Icons.Rounded.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp).clickable { onEditClick() }
                    )
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
            modifier = Modifier.fillMaxWidth().height(56.dp)
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
                onLocationReceived(location.latitude, location.latitude)
            }
        }
        .addOnFailureListener {
            it.printStackTrace()
        }
}












