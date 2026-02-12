package com.example.kora.data.model

data class Restaurant(
    val id: String = "",
    val name: String = "",
    val cuisine: String = "",
    val deliveryFee: String = "",
    val estTime: String = "",
    val openingTime: String = "",
    val closingTime: String = "",
    val imageUrl: String = "",
    val dishCount: Int = 0,

//    Audit Logs
    val createdBy: String = "",
    val createdAt: Long = 0,
    val updatedBy: String = "",
    val updatedAt: Long = 0,
)
