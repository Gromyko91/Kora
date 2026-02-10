package com.example.kora.data.model

data class User(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "user",
    val createdAt: Long = 0,
    val updatedAt: Long = 0
)
