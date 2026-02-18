package com.example.kora

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kora.data.admin.ActivityType
import com.example.kora.data.admin.AdminDashboardViewModel
import com.example.kora.ui.theme.KoraBackground
import com.example.kora.ui.theme.KoraButton
import com.example.kora.ui.theme.KoraCard
import com.example.kora.ui.theme.KoraText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AllActivitesScreen(
    onBackClick: () -> Unit
) {
    val viewModel: AdminDashboardViewModel = viewModel()
    val allActivities by viewModel.activites.collectAsState()

    var selectedTypeFilter by remember { mutableStateOf<ActivityType?>(null) }
    val typeFilters = listOf("All", "Orders", "Restaurants")

    var showDateDropdown by remember { mutableStateOf(false) }
    var selectedDateRange by remember { mutableStateOf("All Time") }
    val dateRanges = listOf("1 Day", "1 Week", "1 Month", "All Time")

    val filteredActivities = allActivities.filter { activity ->
        val typeMatch = when (selectedTypeFilter) {
            null -> true
            ActivityType.ORDER -> activity.type == ActivityType.ORDER
            ActivityType.RESTAURANT -> activity.type == ActivityType.RESTAURANT
            else -> true
        }

        val now = System.currentTimeMillis()
        val oneDay = 24 * 60 * 60 * 1000L
        val dateMatch = when(selectedDateRange) {
            "1 Day" -> (now - activity.timestamp) <= oneDay
            "1 Week" -> (now - activity.timestamp) <= (oneDay * 7)
            "1 Month" -> (now - activity.timestamp) <= (oneDay * 30)
            else -> true
        }

        typeMatch && dateMatch
    }

    val groupedActivities = filteredActivities.groupBy { activity ->
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = Date(activity.timestamp)
        val today = sdf.format(Date())
        val actDate = sdf.format(date)

        when (actDate) {
            today -> "Today"
            else -> actDate
        }
    }

    Scaffold(
        containerColor = KoraBackground,
        topBar = {
            Column(modifier = Modifier.background(KoraBackground)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowLeft, contentDescription = "Back", tint = KoraText)
                    }
                    Text("All Activities", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = KoraText)

                    Box {
                        TextButton(onClick = { showDateDropdown = true }) {
                            Text(selectedDateRange, color = KoraButton)
                            Icon(Icons.Rounded.ArrowDropDown, null, tint = KoraButton)
                        }
                        DropdownMenu(
                            expanded = showDateDropdown,
                            onDismissRequest = { showDateDropdown = false },
                            modifier = Modifier.background(KoraCard)
                        ) {
                            dateRanges.forEach { range ->
                                DropdownMenuItem(
                                    text = { Text(range, color = KoraText) },
                                    onClick = {
                                        selectedDateRange = range
                                        showDateDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    items(typeFilters) { filter ->
                        val isSelected = (filter == "All" && selectedTypeFilter == null) ||
                                        (filter == "Orders" && selectedTypeFilter == ActivityType.ORDER) ||
                                        (filter == "Restaurants" && selectedTypeFilter == ActivityType.RESTAURANT)

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedTypeFilter = when(filter) {
                                    "Orders" -> ActivityType.ORDER
                                    "Restaurants" -> ActivityType.RESTAURANT
                                    else -> null
                                }
                            },
                            label = { Text(filter) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = KoraButton,
                                selectedLabelColor = KoraBackground
                            )
                        )
                    }
                }
                HorizontalDivider(color = Color.LightGray.copy(0.2f))
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            groupedActivities.forEach { (header, activities) ->
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(KoraBackground)
                            .padding(vertical = 12.dp)
                    ) {
                        Text(text = header, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    }
                }

                items(activities) { activity ->
                    ActivityRowItem(activity)
                    HorizontalDivider(color = KoraText.copy(0.1f))
                }
            }

            if (groupedActivities.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 50.dp), contentAlignment = Alignment.Center) {
                        Text("No activities found for this filter", color = Color.Gray)
                    }
                }
            }
        }
    }
}