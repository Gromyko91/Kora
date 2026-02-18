package com.example.kora

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.kora.data.dishes.DishViewModel
import com.example.kora.data.dishes.UiState
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraBox
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraCard
import com.example.kora.ui.theme.KoraText
import kotlin.concurrent.timer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDishScreen(
    restaurantId: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val viewModel: DishViewModel = viewModel()
    val uiState by viewModel.addDishState.collectAsState()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var customization by remember { mutableStateOf("") }
    var isAvailableImmediately by remember { mutableStateOf(true) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Dropdown State
    var categoryExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Main Course") }
    var categories = listOf("Main Course", "Appetizer", "Drinks","Dessert", "Beverage", "Side", "Salad")

    var prepTimeExpanded by remember { mutableStateOf(false) }
    var selectedPrepTime by remember { mutableStateOf("") }
    val prepTimeOptions = (10..60 step 5).map { "$it mins" }

    var allergenOptions = listOf("Nuts", "Dairy", "Gluten", "Eggs", "Soy")
    val selectedAllergens = remember { mutableStateListOf<String>() }

    // Image Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    LaunchedEffect(uiState) {
        when(uiState) {
            is UiState.Success -> {
                Toast.makeText(context, "Dish Added!", Toast.LENGTH_LONG).show()
                viewModel.resetAddState()
                onSaveClick()
            }
            is UiState.Error -> {
                Toast.makeText(context, (uiState as UiState.Error).message, Toast.LENGTH_LONG).show()
                viewModel.resetAddState()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KoraBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Back", tint = KoraText)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Add New Item",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = KoraText
            )
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.size(48.dp))
        }
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
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
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
            Text("Dish Name", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("e.g., Spicy Basil Chicken", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = KoraBackground,
                    unfocusedContainerColor = KoraBackground,
                    focusedBorderColor = KoraText,
                    unfocusedBorderColor = KoraText.copy(alpha = 0.5f)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Category Dropdown
            Text("Category", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = KoraBackground,
                        unfocusedContainerColor = KoraBackground,
                        focusedBorderColor = KoraButton,
                        unfocusedBorderColor = KoraText.copy(alpha = 0.5f)
                    ),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) }
                )
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false },
                    modifier = Modifier.background(KoraCard)
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price
            Text("Price", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = price,
                onValueChange = { newValue ->
                    price  = newValue.filter { it.isDigit() }
                },
                leadingIcon = {
                    Text(
                        text = "Ksh",
                        color = KoraText,
                        modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                    )
                },
                placeholder = { Text("0", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = KoraBackground,
                    unfocusedContainerColor = KoraBackground,
                    focusedBorderColor = KoraText,
                    unfocusedBorderColor = KoraText.copy(alpha = 0.5f)
                )

            )
            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = prepTimeExpanded,
                onExpandedChange = { prepTimeExpanded = !prepTimeExpanded }
            ) {
                OutlinedTextField(
                    value = if(selectedPrepTime.isEmpty()) "Select Preparation Time" else selectedPrepTime,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Meal preparation Time") },
                    trailingIcon = { Icon(Icons.Rounded.KeyboardArrowDown, null) },
                    modifier  = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = prepTimeExpanded,
                    onDismissRequest = { prepTimeExpanded = false },
                    modifier = Modifier.background(KoraCard)
                ) {
                    prepTimeOptions.forEach { time ->
                        DropdownMenuItem(
                            text = {Text(time) },
                            onClick = { selectedPrepTime = time; prepTimeExpanded = false }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Allergens", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                allergenOptions.forEach { allergen ->
                    val isSelected = selectedAllergens.contains(allergen)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) selectedAllergens.remove(allergen)
                            else selectedAllergens.add(allergen)
                        },
                        label = { Text(allergen, color = if (isSelected) KoraBackground else KoraText) },
                        leadingIcon = if (isSelected) {
                            { Icon(Icons.Rounded.Check, null, modifier = Modifier.size(16.dp), tint = KoraBackground)}
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = KoraBackground,
                            selectedContainerColor = KoraBox
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Customization
            Text("Customization", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = customization,
                onValueChange = { customization = it },
                placeholder = { Text("Add options (comma separated)", color = KoraText.copy(alpha = 0.4f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = KoraBackground,
                    unfocusedContainerColor = KoraBackground,
                    focusedBorderColor = KoraText,
                    unfocusedBorderColor = KoraText
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Description", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = KoraText)
                Text("Optional", fontSize = 12.sp, color = KoraText.copy(alpha = 0.5f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Describe the ingredients, taste profile...", color = KoraText.copy(alpha = 0.3f)) },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = KoraBackground,
                    unfocusedContainerColor = KoraBackground,
                    focusedBorderColor = KoraText,
                    unfocusedBorderColor = KoraText
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = KoraCard),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Available Immediately", fontWeight = FontWeight.Bold, color = KoraText)
                    Switch(
                        checked = isAvailableImmediately,
                        onCheckedChange = { isAvailableImmediately = it },
                        colors = SwitchDefaults.colors(checkedTrackColor = KoraButton)
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (name.isNotEmpty() && price.isNotEmpty()) {
                        viewModel.saveDish(
                            restaurantId, name, selectedCategory, price, description, selectedPrepTime,
                            selectedAllergens.toList(), customization, isAvailableImmediately, selectedImageUri
                        )
                    } else {
                        Toast.makeText(context, "Name and Price are required", Toast.LENGTH_LONG).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = KoraButton),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Outlined.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Save & Publish", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}