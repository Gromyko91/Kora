package com.example.kora.data.admin

enum class ActivityType {
    ORDER, RESTAURANT, DISH, UNKNOWN
}

data class ActivityItem(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: Long,
    val type: ActivityType,
    val amount: Double? = null
)
