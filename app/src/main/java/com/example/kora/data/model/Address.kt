package com.example.kora.data.model

data class Address(
    val id: String = "",
    val userId: String = "",
    val nickname: String = "",
    val street: String = "",
    val city: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
