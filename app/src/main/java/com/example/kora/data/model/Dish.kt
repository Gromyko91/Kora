package com.example.kora.data.model

data class Dish(
    val id: String = "",
    val restaurantId: String = "",
    val name: String = "",
    val category: String = "",
    val price: String = "",
    val description: String = "",
    val prepTime: String = "",
    val allergens: List<String> = emptyList(),
    val customization: String = "",
    val isAvailable: Boolean = true,
    val imageUrl: String = "",

    val createdBy: String = "",
    val createdAt: Long = 0,
    val updatedBy: String = "",
    val updatedAt: Long = 0,
)
