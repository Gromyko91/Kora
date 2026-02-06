package com.example.kora

import android.R.attr.text
import androidx.compose.material3.AlertDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
//import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.kora.ui.theme.KoraAccent
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraCard
import com.example.kora.ui.theme.KoraText
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRestaurantScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var deliveryFee by remember { mutableStateOf("") }
    var estTime by remember { mutableStateOf("") }

//    Image Picker State
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

//    Cuisine Dropdown State
    var cuisineExpanded by remember { mutableStateOf(false) }
    var selectedCuisine by remember { mutableStateOf("") }
    val cuisineOptions = listOf("Local Kenyan", "Fast Food", "Chinese", "Indian", "Grills and BBQ", "Cafe & Breakfast", "Dessert & Bakery")

//    Time Picker State
    var openingTime by remember { mutableStateOf("09:00 AM") }
    var closingTime by remember { mutableStateOf("10:00 PM") }
    var showOpeningPicker by remember { mutableStateOf(false) }
    var showClosingPicker by remember { mutableStateOf(false) }

//    Helper to format Time
    val timeFormatter = remember { { hour: Int, min: Int ->
        val amPm = if (hour >= 12) "PM" else "AM"
        val hour12 = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
        String.format(Locale.getDefault(), "%02d:%02d %s", hour12, min, amPm)
    } }

//    Time Picker Dialog Logic
    if (showOpeningPicker) {
        val state = rememberTimePickerState()
        TimePickerDialog(
            onCancel = { showOpeningPicker = false },
            onConfirm = {
                openingTime = timeFormatter(state.hour, state.minute)
                showOpeningPicker = false
            }
        ) {
            TimePicker(state = state)
        }
    }

    if (showClosingPicker) {
        val state = rememberTimePickerState()
        TimePickerDialog(
            onCancel = { showClosingPicker = false },
            onConfirm = {
                closingTime = timeFormatter(state.hour, state.minute)
                showClosingPicker = false
            }
        ) {
            TimePicker(state = state)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KoraBackground)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back", tint = KoraText)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Add New Restaurant",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = KoraText
            )
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.size(48.dp))
        }
        Divider(color = KoraText.copy(alpha = 0.3f))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(KoraCard)
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            ) {
                if (selectedImageUri != null) {
                    Box(
                        modifier = Modifier.matchParentSize()
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = "Selected Cover",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(24.dp)),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { selectedImageUri = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(KoraText.copy(alpha = 0.6f), CircleShape)
                                .size(32.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 12.dp)
                                .background(KoraText.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Tap to Change photo", color = Color.White, fontSize = 12.sp)
                        }
                    }
                } else {
                    DottedUploadBox(
                        modifier = Modifier
                            .fillMaxSize(),
                        text = "Tap to upload a high-quality cover image"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Restaurant name", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("e.g., The Rustic Spoon", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = KoraBackground,
                    unfocusedContainerColor = KoraBackground,
                    focusedBorderColor = KoraText,
                    unfocusedBorderColor = KoraText
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Cuisine Type", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText)
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = cuisineExpanded,
                onExpandedChange = { cuisineExpanded = !cuisineExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = if (selectedCuisine.isEmpty()) "Select Cuisine type" else selectedCuisine,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            if (cuisineExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = KoraBackground,
                        unfocusedContainerColor = KoraBackground,
                        focusedBorderColor = KoraText,
                        unfocusedBorderColor = KoraText
                    )
                )
                ExposedDropdownMenu(
                    expanded = cuisineExpanded,
                    onDismissRequest = { cuisineExpanded = false },
                    modifier = Modifier.background(KoraBackground)
                ) {
                    cuisineOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, color = KoraText) },
                            onClick = {
                                selectedCuisine = option
                                cuisineExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

//        Operating hours
            Text("Operating Hours", fontWeight = FontWeight.Bold, fontSize = 14.sp,color = KoraText)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
//            Opening Time
                Box(modifier = Modifier.weight(1f)) {
                    TimePickerButton(
                        label = "Opening Hours",
                        time = openingTime,
                        onClick = { showOpeningPicker = true }
                    )
                }
//            Closing time
                Box(modifier = Modifier.weight(1f)) {
                    TimePickerButton(
                        label = "Closing Hours",
                        time = closingTime,
                        onClick = { showClosingPicker = true }
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Delivery fee", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = deliveryFee,
                        onValueChange = { deliveryFee = it },
                        placeholder = { Text("0", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        prefix = {
                            Text(
                                text = "KES",
                                color = if (deliveryFee.isNotEmpty()) KoraText else Color.Gray
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = KoraBackground,
                            unfocusedContainerColor = KoraBackground,
                            focusedBorderColor = KoraText,
                            unfocusedBorderColor = KoraText
                        )
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Est. Time", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = estTime,
                        onValueChange = { estTime = it },
                        placeholder = { Text("30-45 min", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = KoraBackground,
                            unfocusedContainerColor = KoraBackground,
                            focusedBorderColor = KoraText,
                            unfocusedBorderColor = KoraText
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onSaveClick,
                colors = ButtonDefaults.buttonColors(containerColor = KoraButton),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Save Restaurant", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Helpers
@Composable
fun TimePickerButton(
    label: String,
    time: String,
    onClick: () -> Unit
) {
    Column {
        Text(label, fontSize = 12.sp, color = KoraText)
        Spacer(modifier = Modifier.height(4.dp))
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = KoraBackground),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = time, color = KoraText, fontSize = 14.sp)
                Icon(Icons.Outlined.Schedule, contentDescription = null, tint = KoraButton, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun TimePickerDialog(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    // Use standard Dialog for custom content like TimePickers
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onCancel
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 6.dp,
            modifier = Modifier.wrapContentSize()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display the TimePicker content passed from the parent
                content()

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancel) {
                        Text("Cancel", color = KoraText)
                    }
                    TextButton(onClick = onConfirm) {
                        Text("OK", color = KoraButton, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}







