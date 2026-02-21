package com.example.kora.data.model

data class AppNotification(
    val id: String = "",
    val targetUserId: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "SYSTEM",
    val referenceId: String = "",
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
