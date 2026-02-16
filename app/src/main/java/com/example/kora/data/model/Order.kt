package com.example.kora.data.model

data class Order(
    val id: String = "",
    val restaurantId: String = "",
    val userId: String = "",
    val status: String = "Pending",
    val subtotal: Double = 0.0,
    val tax: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val total: Double = 0.0,
    val paymentStatus: String = "Unpaid",
    val paymentMethod: String = "",
    val deliveryAddress: Address? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val updatedBy: String = ""
)
