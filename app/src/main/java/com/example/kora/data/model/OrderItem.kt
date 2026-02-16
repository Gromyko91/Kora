package com.example.kora.data.model

data class OrderItem(
    val id: String = "",
    val orderId: String = "",
    val dishId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val imageUrl: String = ""
)
