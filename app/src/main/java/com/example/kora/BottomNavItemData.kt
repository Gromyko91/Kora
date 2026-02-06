package com.example.kora

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItemData(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val activeColor: Color
)
