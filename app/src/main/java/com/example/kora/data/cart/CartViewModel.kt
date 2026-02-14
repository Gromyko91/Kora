package com.example.kora.data.cart

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.example.kora.data.model.Dish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<Map<Dish, Int>>(emptyMap())
    val cartItems = _cartItems.asStateFlow()

    val cartItemCount = _cartItems.map { it.values.sum() }
    val cartTotal = _cartItems.map { map ->
        map.entries.sumOf { (dish, qty) ->
            val cleanPrice = dish.price.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0
            cleanPrice * qty
        }
    }

    fun addToCart(dish: Dish) {
        val current = _cartItems.value.toMutableMap()
        current[dish] = (current[dish] ?: 0) + 1
        _cartItems.value = current
    }

    fun incrementQuantity(dish: Dish) {
        val current = _cartItems.value.toMutableMap()
        val qty = current[dish] ?: 0
        current[dish] = qty + 1
        _cartItems.value = current
    }

    fun decrementQuantity(dish: Dish) {
        val current = _cartItems.value.toMutableMap()
        val qty = current[dish] ?: 0
        if (qty > 1) {
            current[dish] = qty - 1
            _cartItems.value = current
        }
    }

    fun removeFromCart(dish: Dish) {
        val current = _cartItems.value.toMutableMap()
        current.remove(dish)
        _cartItems.value = current
    }

    fun clearCart() {
        _cartItems.value = emptyMap()
    }

    @SuppressLint("DefaultLocale")
    fun formatTotal(total: Double): String {
        return "Ksh ${String.format("%.2f", total)}"
    }
}